package com.coder.dream.jpa;

import com.coder.dream.guice.lifecycle.LifecycleService;
import com.coder.dream.guice.lifecycle.LifecycleSupport;
import com.coder.dream.guice.persist.jpa.JpaConfig;
import com.coder.dream.guice.persist.jpa.JpaModule;
import com.coder.dream.guice.persist.jpa.JpaTxnModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by konghang on 2017/1/23.
 */
public class JpaMain {

    public static void main(String[] args) {
        JpaConfig jpaConfig = new JpaConfig(){
            @Override
            public String getPersistenceUnitName() {
                return "dev_unit";
            }

            @Override
            public String getConnectionUrl() {
                return "jdbc:mysql://localhost:3306/Test_jpa?useUnicode=true&charset=utf8mb4&autoReconnect=true";
            }

            @Override
            public String getConnectionUsername() {
                return "root";
            }

            @Override
            public String getConnectionPassword() {
                return "";
            }
        };

        List<Module> modulesToload = new ArrayList<Module>();
        modulesToload.add(new JpaModule(jpaConfig));
        modulesToload.add(new JpaTxnModule("com.coder.dream.jpa"));
        modulesToload.add(LifecycleSupport.getModule());

        Injector injector = Guice.createInjector(modulesToload);
        LifecycleService lifecycleService = injector.getInstance(LifecycleService.class);
        lifecycleService.start();

        UserDao userDao = injector.getInstance(UserDao.class);

        User toSave = new User();
        toSave.setName("user1");
        userDao.save(toSave);

        User load = userDao.findOne(toSave.getId());
        System.out.println(load);

        try {
            Thread.sleep(1000000000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        lifecycleService.stop();
    }
}
