package com.coder.dream.guice.lifecycle.extentions.ninja;

import com.coder.dream.guice.lifecycle.LifecycleService;
import com.coder.dream.guice.lifecycle.LifecycleSupport;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.lifecycle.Dispose;
import ninja.lifecycle.Start;


public class GuiceLifecycleNinjaModule extends AbstractModule {

    @Override
    protected void configure() {
        install(LifecycleSupport.getModule());
        bind(GuiceLifecycleNinjaInitializer.class).asEagerSingleton();
    }
}
