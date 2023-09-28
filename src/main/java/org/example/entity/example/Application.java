package org.example.entity.example;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Entity
public class Application {

    @EmbeddedId
    private ApplicationId id;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    Application() {
    }

    public Application(ApplicationId id, LocalDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public ApplicationId getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
