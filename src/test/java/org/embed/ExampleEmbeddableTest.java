package org.embed;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.blazebit.persistence.CriteriaBuilderFactory;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@Transactional
class ExampleEmbeddableTest {

    @Inject
    EntityManager em;

    @Inject
    CriteriaBuilderFactory cbf;

    @BeforeEach
    void setup() {
        final var referenced = new Referenced();
        referenced.setValue("foo");
        em.persist(referenced);
        final var example = new ExampleEntity();
        final var embed = new ExampleEmbeddable();
        embed.setExampleRef(referenced);
        example.setEmbed(embed);
        example.setName("bar");
        em.persist(example);
    }

    @AfterEach
    void cleanup() {
        cbf.delete(em, ExampleEntity.class).executeUpdate();
        cbf.delete(em, Referenced.class).executeUpdate();
    }

    @Test
    void fails() {
        final var result = cbf.create(em, ExampleEntity.class)
                .groupBy("id")
                .page(0, 10)
                .orderByAsc("name")
                .orderByAsc("id")
                .where("name").eq().value("bar")
                .getResultList();
        assertEquals(1, result.size());
    }

    @Test
    void works1() {
        final var result = cbf.create(em, ExampleEntity.class)
                // .groupBy("id")
                .page(0, 10)
                .orderByAsc("name")
                .orderByAsc("id")
                .where("name").eq().value("bar")
                .getResultList();
        assertEquals(1, result.size());
    }

    @Test
    void works2() {
        final var result = cbf.create(em, ExampleEntity.class)
                .groupBy("id")
                .page(0, 10)
                .orderByAsc("name")
                .orderByAsc("id")
                // .where("name").eq().value("bar")
                .getResultList();
        assertEquals(1, result.size());
    }

    @Test
    void works3() {
        final var result = cbf.create(em, ExampleEntity.class)
                .groupBy("id")
                // .page(0, 10)
                .orderByAsc("name")
                .orderByAsc("id")
                .where("name").eq().value("bar")
                .getResultList();
        assertEquals(1, result.size());
    }
}
