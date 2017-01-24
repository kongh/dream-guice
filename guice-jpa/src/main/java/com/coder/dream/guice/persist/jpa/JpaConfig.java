package com.coder.dream.guice.persist.jpa;

/**
 * Created by konghang on 2017/1/23.
 */
public interface JpaConfig {

    String getPersistenceUnitName();

    String getConnectionUrl();

    String getConnectionUsername();

    String getConnectionPassword();
}
