package com.coder.dream.disconf.client;

import com.google.inject.Binder;

/**
 * Created by konghang on 2017/1/24.
 */
public interface GuiceBindService {

    /**
     * 绑定实体
     *
     * @param binder
     */
    void bindInstances(Binder binder);
}
