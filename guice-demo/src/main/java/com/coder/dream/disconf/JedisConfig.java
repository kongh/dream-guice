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

    // 代表连接地址
    private String host;

    // 代表连接port
    private int port;

    @DisconfFileItem(name = "redis.host", associateField = "host")
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @DisconfFileItem(name = "redis.port", associateField = "port")
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "JedisConfig{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }

    @Override
    public void reload() throws Exception {
        System.out.println(this);
    }
}
