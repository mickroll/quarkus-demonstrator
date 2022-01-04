package org.acme;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SomeKey {
    @Column(name = "some_key", updatable = false, unique = true, columnDefinition = "CHAR(8)", length = 8)
    private String value;

    protected SomeKey() {
    }

    public SomeKey(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
