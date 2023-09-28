package org.example.entity.example;

import java.sql.Types;
import java.util.UUID;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import org.hibernate.annotations.JdbcTypeCode;

@Embeddable
public final class ApplicationId {
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "CHAR(36)") // explicit name, so we can omit AttributeOverrides
    @Basic
    @JdbcTypeCode(Types.CHAR)
    private UUID id;

    public ApplicationId(final UUID id) {
        this.id = id;
    }

    protected ApplicationId() { // for hibernate
    }

    public UUID getId() {
        return id;
    }

    void setId(UUID id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ApplicationId) {
            return id.equals(((ApplicationId) obj).getId());
        }
        return false;
    }
}
