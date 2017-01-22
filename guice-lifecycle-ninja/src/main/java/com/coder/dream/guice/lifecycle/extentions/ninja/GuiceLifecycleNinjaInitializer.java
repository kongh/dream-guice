package com.coder.dream.guice.lifecycle.extentions.ninja;

import com.coder.dream.guice.lifecycle.LifecycleService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.lifecycle.Dispose;
import ninja.lifecycle.Start;

/**
 * Guice生命周期管理
 *
 * ninja拓展
 * Created by konghang on 2017/1/22.
 */
@Singleton
public class GuiceLifecycleNinjaInitializer {

    private LifecycleService lifecycleService;

    @Inject
    GuiceLifecycleNinjaInitializer(LifecycleService lifecycleService) {
        this.lifecycleService = lifecycleService;
    }

    @Start(
            order = 10
    )
    public void start() {
        this.lifecycleService.start();
    }

    @Dispose(
            order = 10
    )
    public void stop() {
        this.lifecycleService.stop();
    }

}
