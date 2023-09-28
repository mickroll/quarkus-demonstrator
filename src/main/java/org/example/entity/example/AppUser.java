package org.example.entity.example;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Entity
public class AppUser {

    @EmbeddedId
    private AppUserId id;

    @Column(nullable = false)
    private String firstName;

    AppUser() {
    }

    public AppUser(AppUserId id, String firstName) {
        this.id = id;
        this.firstName = firstName;
    }

    public AppUserId getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }
}
