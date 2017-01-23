package com.coder.dream.lifecycle;

import com.coder.dream.guice.lifecycle.Dispose;
import com.coder.dream.guice.lifecycle.Start;
import com.google.inject.Singleton;

/**
 * Created by konghang on 2017/1/23.
 */
@Singleton
public class LifecycleBean {

    @Start
    public void init(){
        System.out.println("lifecycle bean init ...");
    }

    @Dispose
    public void destroy(){
        System.out.println("lifecycle bean destroy ...");
    }
}
