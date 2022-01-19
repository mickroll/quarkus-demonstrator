package org.embed;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ExampleEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private ExampleEmbeddable embed;

    @Column(nullable = false)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public ExampleEmbeddable getEmbed() {
        return embed;
    }

    public void setEmbed(final ExampleEmbeddable embed) {
        this.embed = embed;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
