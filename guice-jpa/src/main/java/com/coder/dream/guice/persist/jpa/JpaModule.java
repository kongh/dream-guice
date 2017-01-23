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

    private JpaConfig jpaConfig;

    public JpaModule(JpaConfig jpaConfig) {
        this.jpaConfig = jpaConfig;
    }

    @Override
    protected void configure() {
        String persistenceUnitName = jpaConfig == null ? null : jpaConfig.getPersistenceUnitName();
        if (persistenceUnitName != null) {

            // Get the connection credentials from application.conf
            String connectionUrl = jpaConfig.getConnectionUrl();
            String connectionUsername = jpaConfig.getConnectionUsername();
            String connectionPassword = jpaConfig.getConnectionPassword();

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
