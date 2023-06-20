package org.example.entity.catalog;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("COUNTRY")
public class Country extends CatalogValue {
}
