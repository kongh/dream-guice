package com.coder.dream.disconf.client;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import java.util.Properties;

/**
 * Created by konghang on 2016/11/28.
 */
public class DisconfModule extends AbstractModule{

    private Properties properties;

    public DisconfModule(Properties properties) {
        this.properties = properties;
    }

    @Override
    protected void configure() {
        Names.bindProperties(binder(), properties);
        bind(DisconfMgrBean.class).asEagerSingleton();
    }
}
