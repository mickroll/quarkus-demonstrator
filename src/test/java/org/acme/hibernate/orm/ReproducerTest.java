package org.acme.hibernate.orm;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import org.example.entity.catalog.CatalogValue;
import org.example.entity.catalog.Country;
import org.example.entity.catalog.FederalState;
import org.junit.jupiter.api.Test;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@TestTransaction
class ReproducerTest {

    @Inject
    EntityManager entityManager;

    @Test
    void metadataCachingBug() {
        final var bav = findByKey(FederalState.class, "DE-BY");
        findByKey(Country.class, "DE");
        bav.getMetadata().size();
    }

    <T extends CatalogValue> T findByKey(final Class<T> type, final String key) {
        final var cb = entityManager.getCriteriaBuilder();
        final var query = cb.createQuery(type);
        final var root = query.from(type);
        query.where(cb.equal(root.get("key"), key));
        final var result = entityManager.createQuery(query).getResultList();
        if (result.isEmpty()) {
            throw new IllegalStateException("mandatory catalog value not found " + type + key);
        }
        if (result.size() == 1) {
            return result.get(0);
        }
        throw new IllegalStateException("multiple entities foun for the same key " + type + key);
    }
}
