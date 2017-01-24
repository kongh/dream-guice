package com.coder.dream.disconf.client.support.registry.impl;

import com.baidu.disconf.client.support.registry.impl.SimpleRegistry;
import com.coder.dream.disconf.client.DisconfService;
import com.coder.dream.disconf.client.GuiceBindService;
import com.google.inject.Binder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by konghang on 2016/11/28.
 */
public class GuiceSimpleRegistry extends SimpleRegistry implements GuiceBindService {

    private Map<Class, Object> instances = new ConcurrentHashMap<>();

    public GuiceSimpleRegistry() {
    }

    public <T> List<T> findByType(Class<T> type, boolean newInstance){
        List<T> ret = new ArrayList<T>(1);
        T instance = findByType(type);
        ret.add(instance);
        return ret;
    }

    private <T> T findByType(Class<T> type){
        Object instance = instances.get(type);
        if(instance == null){
            instance = newInstance(type);
            instances.put(type, instance);
        }
        return (T)instance;
    }

    private <T> T newInstance(Class<T> type){
        try {
            return type.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将实体托管给guice容器
     *
     * @param binder
     */
    @Override
    public void bindInstances(Binder binder){
        binder.bind(GuiceSimpleRegistry.class).toInstance(this);

        if(instances.size() == 0){
            return;
        }
        for(Map.Entry<Class, Object> entry : instances.entrySet()){
            binder.bind(entry.getKey()).toInstance(entry.getValue());
        }
    }
}
