package com.coder.dream.disconf;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf.client.common.update.IDisconfUpdate;
import com.google.inject.Singleton;

/**
 * Created by konghang on 2017/1/22.
 */
@DisconfFile(filename = "redis.properties")
@DisconfUpdateService(classes = {JedisConfig.class})
@Singleton
public class JedisConfig implements IDisconfUpdate{

    private String persistenceUnitName;
    private String connectionUrl;
    private String connectionUsername;
    private String connectionPassword;

    @DisconfFileItem(name = "ninja.jpa.persistence_unit_name", associateField = "persistenceUnitName")
    public String getPersistenceUnitName() {
        return persistenceUnitName;
    }

    public void setPersistenceUnitName(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }

    @DisconfFileItem(name = "db.connection.url", associateField = "connectionUrl")
    public String getConnectionUrl() {
        return connectionUrl;
    }

    public void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }

    @DisconfFileItem(name = "db.connection.username", associateField = "connectionUsername")
    public String getConnectionUsername() {
        return connectionUsername;
    }

    public void setConnectionUsername(String connectionUsername) {
        this.connectionUsername = connectionUsername;
    }

    @DisconfFileItem(name = "db.connection.password", associateField = "connectionPassword")
    public String getConnectionPassword() {
        return connectionPassword;
    }

    public void setConnectionPassword(String connectionPassword) {
        this.connectionPassword = connectionPassword;
    }

    @Override
    public String toString() {
        return "JedisConfig{" +
                "persistenceUnitName='" + persistenceUnitName + '\'' +
                ", connectionUrl='" + connectionUrl + '\'' +
                ", connectionUsername='" + connectionUsername + '\'' +
                ", connectionPassword='" + connectionPassword + '\'' +
                '}';
    }

    @Override
    public void reload() throws Exception {
        System.out.println(this);
    }
}
