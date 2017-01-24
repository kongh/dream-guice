package com.coder.dream.disconf;

import com.coder.dream.disconf.client.DisconfMgrFactory;
import com.coder.dream.disconf.client.DisconfService;
import com.coder.dream.guice.lifecycle.LifecycleService;
import com.coder.dream.guice.lifecycle.LifecycleSupport;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by konghang on 2017/1/22.
 */
public class Main2 {

    public static void main(String[] args) {
        List<Module> modulesToload = new ArrayList<Module>();
        modulesToload.add(new AbstractModule() {
            @Override
            protected void configure() {
                //启动disconf
                DisconfMgrFactory factory = new DisconfMgrFactory("com");
                DisconfService disconfService = factory;
                factory.start();
                factory.bindInstances(binder());

                JedisConfig config = disconfService.findConfig(JedisConfig.class);
                System.out.println(config);
            }
        });
        modulesToload.add(LifecycleSupport.getModule());

        Injector injector = Guice.createInjector(modulesToload);
        LifecycleService lifecycleService = injector.getInstance(LifecycleService.class);
        lifecycleService.start();

        JedisConfig instance = injector.getInstance(JedisConfig.class);
        System.out.println(instance);
        try {
            Thread.sleep(1000000000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                System.out.println(12);
                lifecycleService.stop();
            }
        });

    }
}
