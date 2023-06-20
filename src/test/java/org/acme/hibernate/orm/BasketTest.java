package org.acme.hibernate.orm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class BasketTest {

    @Inject
    EntityManager entityManager;

    @Test
    public void testUpdateFruitInBasket() {
        var basketId = insert("my Basket", "orange", "citric");
        assertNotEquals(0, basketId);

        update(basketId, "orange", "special");

        // entity will be detached, do not update on this instance!
        var basket = get(basketId);
        var fruit = basket.getFruit();
        assertEquals("orange", fruit.getName());
        assertEquals("special", fruit.getType());
    }

    @Transactional
    int insert(String basketName, String fruitName, String fruitType) {
        var fruit = new Fruit(fruitName, fruitType);
        var basket = new Basket(basketName, fruit);
        entityManager.persist(basket);
        return basket.getId();
    }

    @Transactional
    void update(int basketId, String fruitName, String fruitType) {
        var basket = entityManager.find(Basket.class, basketId);
        basket.getFruit().update(fruitName, fruitType);
    }

    @Transactional
    Basket get(int id) {
        var basket = entityManager.find(Basket.class, id);
        return basket;
    }
}
