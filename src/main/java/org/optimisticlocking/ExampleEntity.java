package org.optimisticlocking;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

@Entity
@OptimisticLocking(type = OptimisticLockType.DIRTY)
@DynamicUpdate
public class ExampleEntity {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent")
    private final List<LogEntry> logEntries = new ArrayList<>();

    @OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE })
    private LogEntry lastLogEntry;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public List<LogEntry> getLogEntries() {
        return logEntries;
    }

    public LogEntry getLastLogEntry() {
        return lastLogEntry;
    }

    public void setLastLogEntry(final LogEntry lastLogEntry) {
        this.lastLogEntry = lastLogEntry;
    }
}
