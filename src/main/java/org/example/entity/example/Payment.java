package org.example.entity.example;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

import org.example.entity.base.BaseEntity;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

@Entity
@DynamicUpdate
@OptimisticLocking(type = OptimisticLockType.DIRTY)
public class Payment extends BaseEntity<PaymentId> {

    @EmbeddedId
    @AttributeOverride(name = "id", column = @Column(name = "id", updatable = false, nullable = false, columnDefinition = "CHAR(36)"))
    private PaymentId id; // don't make final to not break hibernate

    @Column(nullable = false)
    private BigDecimal fee;

    @Column
    private LocalDate paidAt;

    @Column(length = 100)
    private String transactionNumber;

    void confirmPayment(LocalDate when) {
        this.paidAt = when;
    }

    public PaymentId getId() {
        return id;
    }

    public void setId(PaymentId id) {
        this.id = id;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public LocalDate getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDate paidAt) {
        this.paidAt = paidAt;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }
}
