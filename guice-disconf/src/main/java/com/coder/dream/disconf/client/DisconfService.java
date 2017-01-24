package com.coder.dream.disconf.client;

/**
 * Created by konghang on 2017/1/24.
 */
public interface DisconfService {

    /**
     * 查找配置
     *
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T findConfig(Class<T> clazz);
}
