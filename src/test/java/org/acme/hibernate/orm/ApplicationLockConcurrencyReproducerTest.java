package org.acme.hibernate.orm;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

import org.eclipse.microprofile.context.ManagedExecutor;
import org.example.entity.example.Application;
import org.example.entity.example.ApplicationId;
import org.example.entity.example.ApplicationLock;
import org.example.entity.example.ApplicationLockId;
import org.example.entity.example.AppUser;
import org.example.entity.example.AppUserId;
import org.junit.jupiter.api.RepeatedTest;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class ApplicationLockConcurrencyReproducerTest {

    private static final int CONCURRENT_CALL_COUNT = 10;

    @Inject
    ManagedExecutor managedExecutor;

    @Inject
    EntityManager em;

    @RepeatedTest(500) // fails in about 1% of times
    public void tryLockEhba_concurrent() throws Throwable {
        final var userId = createUserInNewTx();
        final var appId = createApplicationInNewTx();

        // every user tries to get a lock, in parallel
        final var lockFutures = Stream.generate(
                () -> managedExecutor.submit(() -> {
                    callLockBuilderNewTx(appId, userId);
                }))
                .limit(CONCURRENT_CALL_COUNT)
                .toList();

        for (final var f : lockFutures) {
            try {
                f.get();
            } catch (final ExecutionException e) {
                throw e.getCause();
            }
        }
    }

    @Transactional(TxType.REQUIRES_NEW)
    AppUserId createUserInNewTx() {
        final var user = new AppUser(new AppUserId(UUID.randomUUID()), "Max");
        em.persist(user);
        return user.getId();
    }

    @Transactional(TxType.REQUIRES_NEW)
    void callLockBuilderNewTx(final ApplicationId appId, final AppUserId userId) {
        var user = em.find(AppUser.class, userId);

        // CompositeOwnerTracker.java:30..31 error happens in EhbaApplicationLock.<init>
        new ApplicationLock(new ApplicationLockId(UUID.randomUUID()), LocalDateTime.now(), user, appId);

        // FYI normally we would try to save the lock now, with a constraint-violation-catching-logic for 'is already locked' cases, based on an unique index
    }

    @Transactional(TxType.REQUIRES_NEW)
    ApplicationId createApplicationInNewTx() {
        final var app = new Application(new ApplicationId(UUID.randomUUID()), LocalDateTime.now());
        em.persist(app);
        return app.getId();
    }
}
