package org.example.entity.catalog;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("FEDERAL_STATE")
public class FederalState extends CatalogValue {
}
