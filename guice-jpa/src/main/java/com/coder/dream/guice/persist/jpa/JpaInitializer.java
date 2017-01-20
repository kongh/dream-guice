package com.coder.dream.guice.persist.jpa;

import com.coder.dream.guice.lifecycle.Dispose;
import com.coder.dream.guice.lifecycle.Start;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistService;

/**
 * Created by konghang on 2017/1/20.
 */
@Singleton
public class JpaInitializer {
    private PersistService persistService;

    @Inject
    JpaInitializer(PersistService persistService) {
        this.persistService = persistService;
    }

    @Start(
            order = 10
    )
    public void start() {
        this.persistService.start();
    }

    @Dispose(
            order = 10
    )
    public void stop() {
        this.persistService.stop();
    }
}
