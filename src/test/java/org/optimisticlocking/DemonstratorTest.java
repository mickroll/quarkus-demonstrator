package org.optimisticlocking;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@Transactional(Transactional.TxType.NOT_SUPPORTED)
class DemonstratorTest {

    @Inject
    ExampleEntityRepository repo;
    @Inject
    EntityManager em;

    @AfterEach
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    void cleanup() throws Exception {
        // preventWrongDirtyDelete(); // this is a workaround

        repo.deleteAll(); // FAILS
    }

    @Test
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    void create() {
        final var parent = new ExampleEntity();
        repo.save(parent);
        final var logEntry = new LogEntry();
        logEntry.setParent(parent);
        logEntry.setValue("foo");
        parent.getLogEntries().add(logEntry);
        parent.setLastLogEntry(logEntry);
        repo.save(parent);
    }

    /**
     * Running {@code repo.deleteAll()} without this workaround leads to:
     * <ol>
     * <li>OK: breaking the circular reference: {@code update `example_entity` set `last_log_entry_id`=null where `id`='...'}
     * <li>OK: deleting the log entry: {@code delete from `log_entry` where `id`='...'}
     * <li>WRONG: deleting the ExampleEntity, referencing the old log entry id instead of {@code null}:
     * {@code delete from `example_entity` where `id`='...' and `last_log_entry_id`='...'} <br/>
     * <b>should be</b>: {@code delete from `example_entity` where `id`='...' and `last_log_entry_id` IS NULL}
     * </ol>
     * Another option is to annotate {@link ExampleEntity#lastLogEntry} with {@code @OptimisticLock(excluded = true)}, so the attribute is excluded. But then,
     * it is missing in update-queries as well!
     */
    private void preventWrongDirtyDelete() {
        final var cb = em.getCriteriaBuilder();
        final var update = cb.createCriteriaUpdate(ExampleEntity.class);
        update.from(ExampleEntity.class);
        update.set("lastLogEntry", null);
        em.createQuery(update).executeUpdate();
    }
}
