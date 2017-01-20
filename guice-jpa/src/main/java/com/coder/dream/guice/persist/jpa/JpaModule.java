package com.coder.dream.guice.persist.jpa;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;

import java.util.Properties;

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;

/**
 * Created by konghang on 2017/1/19.
 */
public class JpaModule extends AbstractModule {

    @Override
    protected void configure() {
        String persistenceUnitName = "dev_unit";

//        db.connection.url=jdbc:mysql://rdsb8hxo9g93fmcjeme2.mysql.rds.aliyuncs.com:3306/xiaoyage_bi?useUnicode=true&charset=utf8mb4&autoReconnect=true
//        db.connection.username=xiaoyage_bi
//        db.connection.password=xiaoyage123
        if (persistenceUnitName != null) {

            // Get the connection credentials from application.conf
            String connectionUrl = "jdbc:mysql://rdsb8hxo9g93fmcjeme2.mysql.rds.aliyuncs.com:3306/xiaoyage_bi?useUnicode=true&charset=utf8mb4&autoReconnect=true";
            String connectionUsername = "xiaoyage_bi";
            String connectionPassword = "xiaoyage123";

            Properties jpaProperties = new Properties();

            // We are using Hibernate, so we can set the connections stuff
            // via system properties:
            if (connectionUrl != null) {
                jpaProperties.put("hibernate.connection.url", connectionUrl);
            }

            if (connectionUsername != null) {
                jpaProperties.put("hibernate.connection.username", connectionUsername);
            }

            if (connectionPassword != null) {
                jpaProperties.put("hibernate.connection.password", connectionPassword);
            }

            // Now - it may be the case the neither connection.url, connection.username nor
            // connection.password is set. But this may be okay e.g. when using JDNI to
            // configure your datasources...
            install(new JpaPersistModule(persistenceUnitName).properties(jpaProperties));
//            Properties jpaProperties2 = new Properties();
//            jpaProperties2.put("hibernate.connection.url", "jdbc:mysql://localhost:3306/xiaoyage_dev9?useUnicode=true&charset=utf8mb4&autoReconnect=true");
//            jpaProperties2.put("hibernate.connection.username", "root");
//            jpaProperties2.put("hibernate.connection.password", "");
//            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(null, jpaProperties2);
//            EntityManager entityManager = entityManagerFactory.createEntityManager();

            UnitOfWorkInterceptor unitOfWorkInterceptor = new UnitOfWorkInterceptor();

            requestInjection(unitOfWorkInterceptor);

            // class-level @UnitOfWork
            bindInterceptor(
                    annotatedWith(UnitOfWork.class),
                    UnitOfWorkMatcher.INSTANCE.and(any()),
                    unitOfWorkInterceptor);

            // method-level @UnitOfWork
            bindInterceptor(
                    any(),
                    annotatedWith(UnitOfWork.class),
                    unitOfWorkInterceptor);


            bind(JpaInitializer.class).asEagerSingleton();



        }
    }
}
