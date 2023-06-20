package org.acme.hibernate.orm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class FruitsTest {

    @Inject
    EntityManager entityManager;

    @Test
    public void testUpdateFruits() {
        var id = insert("lemon", "citric");
        assertNotEquals(0, id);

        update(id, "lemon", "special");

        // entity will be detached, do not update on this instance!
        var fruit = get(id);
        assertEquals("lemon", fruit.getName());
        assertEquals("special", fruit.getType());
    }

    @Transactional
    int insert(String name, String name2) {
        var fruit = new Fruit(name, name2);
        entityManager.persist(fruit);
        return fruit.getId();
    }

    @Transactional
    void update(int id, String name, String type) {
        var fruit = entityManager.find(Fruit.class, id);
        fruit.update(name, type);
    }

    @Transactional
    Fruit get(int id) {
        return entityManager.find(Fruit.class, id);
    }
}
