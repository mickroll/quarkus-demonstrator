package org.optimisticlocking;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class LogEntry {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private ExampleEntity parent;

    @Column(nullable = false)
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public ExampleEntity getParent() {
        return parent;
    }

    public void setParent(final ExampleEntity parent) {
        this.parent = parent;
    }
}
