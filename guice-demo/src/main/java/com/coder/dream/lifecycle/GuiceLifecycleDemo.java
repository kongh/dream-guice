package com.coder.dream.lifecycle;

import com.coder.dream.guice.lifecycle.LifecycleService;
import com.coder.dream.guice.lifecycle.LifecycleSupport;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by konghang on 2017/1/23.
 */
public class GuiceLifecycleDemo {

    public static void main(String[] args) {
        List<Module> modulesToload = new ArrayList<Module>();
        modulesToload.add(LifecycleSupport.getModule());

        Injector injector = Guice.createInjector(modulesToload);
        LifecycleService lifecycleService = injector.getInstance(LifecycleService.class);
        lifecycleService.start();

        LifecycleBean lifecycleBean = injector.getInstance(LifecycleBean.class);
        System.out.println(lifecycleBean);
        try {
            Thread.sleep(1000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        lifecycleService.stop();
    }
}
