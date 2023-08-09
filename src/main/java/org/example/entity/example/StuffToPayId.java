package org.example.entity.example;

import java.util.UUID;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public final class StuffToPayId {

    @Column(updatable = false, nullable = false, columnDefinition = "CHAR(36)") // no explicit name for direct id usage as foreign key, named after property
    @Basic
    private UUID uuid;

    public StuffToPayId(final UUID uuid) {
        this.uuid = uuid;
    }

    protected StuffToPayId() { // for hibernate
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StuffToPayId) {
            return uuid.equals(((StuffToPayId) obj).getUuid());
        }
        return false;
    }

    protected void setUuid(UUID uuid) { // for hibernate
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "StuffToPayId(" + uuid + ")";
    }
}
