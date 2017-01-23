package com.coder.dream.guice.persist.jpa;

/**
 * Created by konghang on 2017/1/23.
 */
public class JpaConfig {

    private String persistenceUnitName;

    private String connectionUrl;

    private String connectionUsername;

    private String connectionPassword;

    public String getPersistenceUnitName() {
        return persistenceUnitName;
    }

    public void setPersistenceUnitName(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }

    public String getConnectionUsername() {
        return connectionUsername;
    }

    public void setConnectionUsername(String connectionUsername) {
        this.connectionUsername = connectionUsername;
    }

    public String getConnectionPassword() {
        return connectionPassword;
    }

    public void setConnectionPassword(String connectionPassword) {
        this.connectionPassword = connectionPassword;
    }

    @Override
    public String toString() {
        return "JpaConfig{" +
                "persistenceUnitName='" + persistenceUnitName + '\'' +
                ", connectionUrl='" + connectionUrl + '\'' +
                ", connectionUsername='" + connectionUsername + '\'' +
                ", connectionPassword='" + connectionPassword + '\'' +
                '}';
    }
}
