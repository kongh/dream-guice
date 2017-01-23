package com.coder.dream.guice.persist.jpa;

import com.google.inject.AbstractModule;

/**
 * Jpa local Txn 基于Aop 配置
 * Created by konghang on 2017/1/23.
 */
public class JpaTxnModule extends AbstractModule {

    //例：^com.abc..*.daos.impls.*
    private String jpaScanPackage;

    public JpaTxnModule(String jpaScanPackage) {
        this.jpaScanPackage = jpaScanPackage;
    }

    @Override
    protected void configure() {
        if(jpaScanPackage != null){
            // dao 的事务和连接回收注入
            JpaLocalTxnInterceptor jpaLocalTxnInterceptor = new JpaLocalTxnInterceptor();
            requestInjection(jpaLocalTxnInterceptor);
            bindInterceptor(NinjaMatchers.inSubpackageScan(jpaScanPackage),
                    NinjaMatchers.jpaTxnMatcher(),
                    jpaLocalTxnInterceptor);
        }
    }
}
