package org.acme.hibernate.orm;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.QueryHint;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.OptimisticLockType;

@Entity
@Table
@Cacheable
@DynamicUpdate
@OptimisticLocking(type = OptimisticLockType.DIRTY)
public class Fruit {

    @Id
    @SequenceGenerator(name = "fruitSequence", sequenceName = "fruit_id_seq", allocationSize = 1, initialValue = 10)
    @GeneratedValue(generator = "fruitSequence")
    private Integer id;

    @Column(length = 40)
    private String name;

    @Column(length = 40)
    private String type;

    protected Fruit() {
    }

    public Fruit(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void update(String name, String type) {
        this.name = name;
        this.type = type;
    }
}
