package org.acme.hibernate.orm;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.function.Supplier;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import org.example.entity.example.Payment;
import org.example.entity.example.PaymentId;
import org.example.entity.example.StuffToPay;
import org.example.entity.example.StuffToPayId;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class ReproducerTest {

    @Inject
    EntityManager entityManager;

    @Test
    void reproducer() {
        var stpId = inTx(() -> {
            var stuffToPay = new StuffToPay();
            stuffToPay.setId(new StuffToPayId(UUID.randomUUID()));
            var payment = new Payment();
            payment.setId(new PaymentId(UUID.randomUUID()));
            payment.setFee(new BigDecimal(42));
            payment.setPaidAt(null);
            payment.setTransactionNumber("1234567890");
            stuffToPay.setPayment(payment);
            entityManager.persist(stuffToPay);
            return stuffToPay.getId();
        });

        inTx(() -> {
            var stuffToPay = entityManager.find(StuffToPay.class, stpId);
            stuffToPay.confirmPayment();
            stuffToPay.getPayment().getTransactionNumber(); // AIOOBE while lazy fetching the payment
            return null;
        });
    }

    @Transactional
    <T> T inTx(Supplier<T> callback) {
        return callback.get();
    }
}
