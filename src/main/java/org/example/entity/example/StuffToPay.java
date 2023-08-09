package org.example.entity.example;

import java.time.LocalDate;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;

import org.example.entity.base.BaseEntity;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

@Entity
@DynamicUpdate
@OptimisticLocking(type = OptimisticLockType.DIRTY)
public class StuffToPay extends BaseEntity<StuffToPayId> {

    @EmbeddedId
    @AttributeOverride(name = "id", column = @Column(name = "id", updatable = false, nullable = false, columnDefinition = "CHAR(36)"))
    private StuffToPayId id; // don't make final to not break hibernate

    /**
     * Payment details, if any. Fetching should be lazy.
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Payment payment;

    public void confirmPayment() {
        payment.confirmPayment(LocalDate.now());
    }

    public StuffToPayId getId() {
        return id;
    }

    public void setId(StuffToPayId id) {
        this.id = id;
    }
    

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
