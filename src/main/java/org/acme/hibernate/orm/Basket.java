package org.acme.hibernate.orm;

import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

@Entity
@Table(name = "basket")
@Cacheable
@DynamicUpdate
@OptimisticLocking(type = OptimisticLockType.DIRTY)
public class Basket {
    @Id
    @SequenceGenerator(name = "basketSequence", sequenceName = "basket_id_seq", allocationSize = 1, initialValue = 10)
    @GeneratedValue(generator = "basketSequence")
    private Integer id;

    @Column(length = 40)
    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Fruit fruit;

    protected Basket() {
    }

    public Basket(String name, Fruit fruit) {
        this.name = name;
        this.fruit = fruit;
    }

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setFruit(Fruit fruit) {
        this.fruit = fruit;
    }

    public Fruit getFruit() {
        return fruit;
    }
}
