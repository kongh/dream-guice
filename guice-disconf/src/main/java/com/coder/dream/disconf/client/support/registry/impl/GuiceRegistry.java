package com.coder.dream.disconf.client.support.registry.impl;

import com.baidu.disconf.client.support.registry.impl.SimpleRegistry;
import com.google.inject.Injector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by konghang on 2016/11/28.
 */
public class GuiceRegistry extends SimpleRegistry {

    private Injector injector;

    public GuiceRegistry(Injector injector) {
        this.injector = injector;
    }

    public <T> List<T> findByType(Class<T> type, boolean newInstance){
        List<T> ret = new ArrayList<T>(1);
        T instance = injector.getInstance(type);
        ret.add(instance);
        return ret;
    }
}
