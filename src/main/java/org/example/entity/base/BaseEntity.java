package org.example.entity.base;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Transient;

@MappedSuperclass
public abstract class BaseEntity<ID> implements Persistable<ID> {

    /**
     * "Enity is new"-state tracking for {@link Persistable#isNew()}.
     *
     * @see <a href="https://quarkus.io/guides/spring-data-jpa#what-is-currently-unsupported">Quarkus spring-data:
     *      what-is-currently-unsupported</a>
     * @see <a href=
     *      "https://github.com/spring-projects/spring-data-jpa/blob/2.3.1.RELEASE/src/main/asciidoc/jpa.adoc#entity-state-detection-strategies">
     *      entity-state-detection</a>
     */
    @Transient
    // WTF this member cannot be named "isNew", otherwise metamodel will contain a member "new", which is a reserved keyword.
    private boolean isNewEntity = true;

    @PrePersist
    @PostLoad
    void markNotNew() {
        this.isNewEntity = false;
    }

    @Override
    @Transient
    public boolean isNew() {
        return isNewEntity;
    }

    @Override
    public abstract ID getId();
}
