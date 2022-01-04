package org.acme;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@Transactional
class DemonstratorTest {

    private static final int PAGE_SIZE = 2;

    @Inject
    EntityManager em;

    @Inject
    CriteriaBuilderFactory cbf;

    private Organization org1;
    private State state1;
    private List<ListEntity> items;

    @BeforeEach
    void setup() {
        org1 = createOrganization("A");
        state1 = createState("foo");

        /* create 3 items, so later 3 pages (with maximum 2 entries each) should be found:
         * - first with 2 items
         * - second with 1 item
         * - third with 0 items
         *
         * NOTE: every created item has organization org1. Later, a filter is applied for org1, which should not change the resulting list.
         */
        items = Stream.generate(() -> createItem(org1, state1))
                .limit(PAGE_SIZE + 1)
                .collect(Collectors.toList());
    }

    @AfterEach
    void cleanup() {
        cbf.delete(em, ListEntity.class).executeUpdate();
        cbf.delete(em, Organization.class).executeUpdate();
        cbf.delete(em, State.class).executeUpdate();
    }

    /////////////////////////////////////
    // test using setMaxResults().afterKeyset() and manual keyset data extraction
    /////////////////////////////////////

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void demonstrator_CriteriaBuilder(final boolean enableOrganizationFilter) {
        final var firstPage = getPageOld(enableOrganizationFilter, null);
        assertFalse(firstPage.isEmpty());

        final var secondPage = getPageOld(enableOrganizationFilter, extractKeyset(firstPage));
        assertFalse(secondPage.isEmpty()); // fails here for enableOrganizationFilter=true

        final var thirdPage = getPageOld(enableOrganizationFilter, extractKeyset(secondPage));
        assertTrue(thirdPage.isEmpty());

        final var allFound = new ArrayList<ListEntity>();
        allFound.addAll(firstPage);
        allFound.addAll(secondPage);
        allFound.addAll(thirdPage);
        assertEquals(items.size(), allFound.size());
    }

    private List<ListEntity> getPageOld(final boolean enableOrganizationFilter, final Serializable[] lastKeyset) {
        final var query = cbf.create(em, ListEntity.class)
                // workaround: disable query optimization :-/
                // .setProperty(ConfigurationProperties.OPTIMIZED_KEYSET_PREDICATE_RENDERING, "false")
                .from(ListEntity.class)
                .setMaxResults(PAGE_SIZE)
                .orderBy("state.id", false)
                .orderBy("someKey", false)
                .orderBy("id", true);
        if (enableOrganizationFilter) {
            query.where("organization.id").eq().literal(org1.getId());
        }
        if (lastKeyset != null) {
            query.afterKeyset(lastKeyset);
        }
        return query.getResultList();
    }

    private Serializable[] extractKeyset(final List<ListEntity> list) {
        if (list.isEmpty()) {
            return null;
        }
        final var lastItem = list.get(list.size() - 1);
        return new Serializable[] { lastItem.getState().getId(), lastItem.getSomeKey().getValue(), lastItem.getId() };
    }

    /////////////////////////////////////
    // test using page().withKeysetExtraction().afterKeyset()
    /////////////////////////////////////

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void demonstrator_PaginatedCriteriaBuilder(final boolean enableOrganizationFilter) {
        final var firstPage = getPage(enableOrganizationFilter, null);
        assertFalse(firstPage.isEmpty());
        assertNotNull(firstPage.getKeysetPage());

        final var secondPage = getPage(enableOrganizationFilter, firstPage.getKeysetPage().getHighest().getTuple());
        assertFalse(secondPage.isEmpty()); // fails here for enableOrganizationFilter=true
        assertNotNull(secondPage.getKeysetPage());

        final var thirdPage = getPage(enableOrganizationFilter, secondPage.getKeysetPage().getHighest().getTuple());
        assertTrue(thirdPage.isEmpty());
        assertNull(thirdPage.getKeysetPage());

        final var allFound = new ArrayList<ListEntity>();
        allFound.addAll(firstPage);
        allFound.addAll(secondPage);
        allFound.addAll(thirdPage);
        assertEquals(items.size(), allFound.size());
    }

    private PagedList<ListEntity> getPage(final boolean enableOrganizationFilter, final Serializable[] lastKeyset) {
        final var query = cbf.create(em, ListEntity.class)
                // workaround: disable query optimization :-/
                // .setProperty(ConfigurationProperties.OPTIMIZED_KEYSET_PREDICATE_RENDERING, "false")
                .from(ListEntity.class)
                .page(0, PAGE_SIZE)
                .withKeysetExtraction(true)
                .orderBy("state.id", false)
                .orderBy("someKey", false)
                .orderBy("id", true);
        if (enableOrganizationFilter) {
            query.where("organization.id").eq().literal(org1.getId());
        }
        if (lastKeyset != null) {
            query.afterKeyset(lastKeyset);
        }
        return query.getResultList();
    }

    /////////////////////////////////////
    // data setup methods
    /////////////////////////////////////

    private Organization createOrganization(final String name) {
        final var result = new Organization();
        result.setName(name);
        em.persist(result);
        return result;
    }

    private State createState(final String displayName) {
        final var result = new State();
        result.setDisplayName(displayName);
        em.persist(result);
        return result;
    }

    private ListEntity createItem(final Organization organization, final State state) {
        final var result = new ListEntity();
        result.setSomeKey(new SomeKey(RandomStringUtils.randomAlphanumeric(8)));
        result.setState(state);
        result.setOrganization(organization);
        em.persist(result);
        return result;
    }
}
