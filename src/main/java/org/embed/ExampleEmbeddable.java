package org.embed;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class ExampleEmbeddable {

    @ManyToOne(optional = false)
    private Referenced exampleRef;

    public Referenced getExampleRef() {
        return exampleRef;
    }

    public void setExampleRef(final Referenced exampleRef) {
        this.exampleRef = exampleRef;
    }
}
