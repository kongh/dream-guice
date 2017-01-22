package com.coder.dream.disconf;

import com.coder.dream.disconf.client.DisconfModule;
import com.coder.dream.guice.lifecycle.LifecycleService;
import com.coder.dream.guice.lifecycle.LifecycleSupport;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by konghang on 2017/1/22.
 */
public class Main {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("disconf.scan.package", "com");

        List<Module> modulesToload = new ArrayList<Module>();
        modulesToload.add(new DisconfModule(properties));
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

        lifecycleService.stop();
    }
}
