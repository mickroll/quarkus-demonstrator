package org.acme;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ListEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private State state;

    @Embedded
    private SomeKey someKey;

    @ManyToOne(optional = false)
    private Organization organization;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(final State state) {
        this.state = state;
    }

    public SomeKey getSomeKey() {
        return someKey;
    }

    public void setSomeKey(final SomeKey someKey) {
        this.someKey = someKey;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(final Organization organization) {
        this.organization = organization;
    }

}
