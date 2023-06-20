package org.example.entity.catalog;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Cacheable;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.PostLoad;

import org.example.entity.base.BaseEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

@Entity(name = "catalog_value")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Immutable
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "catalog_type")
public abstract class CatalogValue extends BaseEntity<CatalogValueId> implements Comparable<CatalogValue> {

    @EmbeddedId
    @AttributeOverride(name = "uuid", column = @Column(name = "id", updatable = false, nullable = false, columnDefinition = "CHAR(36)"))
    private CatalogValueId id; // don't make final to not break hibernate

    @Column(name = "`key`", nullable = false)
    private String key;

    @Column(nullable = false)
    private String displayName;

    @Immutable
    @ElementCollection
    @CollectionTable(name = "catalog_value_metadata", joinColumns = {
            @JoinColumn(name = "catalog_value_id", referencedColumnName = "id") })
    @MapKeyColumn(name = "`key`")
    @AttributeOverride(name = "value.value", column = @Column(name = "value"))
    @AttributeOverride(name = "value.isPublic", column = @Column(name = "is_public"))
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private Map<String, MetadataValue> metadata = new HashMap<>();

    @Column(nullable = false)
    private Integer orderValue = 0;

    @PostLoad
    void postLoad() {
        /*
         * fix metadata caching bug by simulating eager fetching.
         * We do not use real eager fetching, because metadata would become part of queries. By fetching it here, cached values
         * are taken into account.
         */
        // metadata.size();
    }

    public CatalogValueId getId() {
        return id;
    }

    public void setId(CatalogValueId id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, MetadataValue> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, MetadataValue> metadata) {
        this.metadata = metadata;
    }

    public Integer getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(Integer orderValue) {
        this.orderValue = orderValue;
    }

    @Override
    public int compareTo(final CatalogValue otherCatalogValue) {
        return Comparator.comparing((final CatalogValue c) -> c.getClass().getName())
                .thenComparingInt(c -> c.orderValue)
                .compare(this, otherCatalogValue);
    }

    /**
     * A key to get specific mandatory metadata using {@link CatalogValue#getMandatoryMetadata(MetadataKey, RoleCategory)}.
     */
    protected interface MetadataKey extends Serializable {
        /**
         * Return name of key for metadata map.
         *
         * @return name of key
         */
        String name();
    }

    /**
     * Wrapper for metadata value.
     */
    @Embeddable
    public static class MetadataValue {
        @Column(nullable = false)
        private String value;

        @Column
        private boolean isPublic;

        public boolean isIsPublic() { // for hibernate
            return isPublic;
        }

        public boolean isPublic() {
            return isPublic;
        }

        public void setPublic(boolean isPublic) {
            this.isPublic = isPublic;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
