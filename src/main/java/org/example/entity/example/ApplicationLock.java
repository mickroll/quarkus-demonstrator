package org.example.entity.example;

import java.time.LocalDateTime;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class ApplicationLock {

    @EmbeddedId
    private ApplicationLockId id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime lockedAt;

    @ManyToOne(optional = false)
    private AppUser lockedBy;

    @Basic
    @AttributeOverride(name = "id", column = @Column(name = "ehba_application_id", updatable = false, nullable = false, columnDefinition = "CHAR(36)"))
    private ApplicationId applicationId;

    ApplicationLock() {
    }

    public ApplicationLock(ApplicationLockId id, LocalDateTime lockedAt, AppUser lockedBy, ApplicationId applicationId) {
        this.id = id;
        this.lockedAt = lockedAt;
        this.lockedBy = lockedBy;
        this.applicationId = applicationId;
    }

    public ApplicationLockId getId() {
        return id;
    }

    public LocalDateTime getLockedAt() {
        return lockedAt;
    }

    public AppUser getLockedBy() {
        return lockedBy;
    }

    public ApplicationId getApplicationId() {
        return applicationId;
    }
}
