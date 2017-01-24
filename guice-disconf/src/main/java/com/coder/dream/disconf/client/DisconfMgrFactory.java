package com.coder.dream.disconf.client;

import com.baidu.disconf.client.store.inner.DisconfCenterHostFilesStore;
import com.baidu.disconf.client.support.registry.Registry;
import com.baidu.disconf.client.support.utils.StringUtil;
import com.coder.dream.disconf.client.support.registry.impl.GuiceSimpleRegistry;
import com.coder.dream.guice.lifecycle.Dispose;
import com.google.inject.Binder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by konghang on 2016/11/28.
 */
public class DisconfMgrFactory implements DisconfService, GuiceBindService{

    private String scanPackage = null;

    public final static String SCAN_SPLIT_TOKEN = ",";

    private Registry registry;

    private GuiceBindService guiceBindService;

    public DisconfMgrFactory(String scanPackage) {
        this.scanPackage = scanPackage;

        GuiceSimpleRegistry registry = new GuiceSimpleRegistry();
        this.registry = registry;
        this.guiceBindService = registry;
    }

    public void start(){
        // 为了做兼容
        DisconfCenterHostFilesStore.getInstance().addJustHostFileSet(fileList);

        List<String> scanPackList = StringUtil.parseStringToStringList(scanPackage, SCAN_SPLIT_TOKEN);
        // unique
        Set<String> hs = new HashSet<String>();
        hs.addAll(scanPackList);
        scanPackList.clear();
        scanPackList.addAll(hs);

        // 进行扫描
        DisconfMgr.getInstance(registry).firstScan(scanPackList);

        DisconfMgr.getInstance(registry).secondScan();
    }

    @Dispose
    public void stop(){
        DisconfMgr.getInstance(registry).close();
    }

    /*
     * 已经废弃了，不推荐使用
     */
    @Deprecated
    private Set<String> fileList = new HashSet<String>();

    @Deprecated
    public Set<String> getFileList() {
        return fileList;
    }

    @Deprecated
    public void setFileList(Set<String> fileList) {
        this.fileList = fileList;
    }

    @Override
    public <T> T findConfig(Class<T> clazz) {
        return registry.findByType(clazz, Boolean.FALSE).get(0);
    }

    @Override
    public void bindInstances(Binder binder) {
        binder.bind(DisconfMgrFactory.class).toInstance(this);
        guiceBindService.bindInstances(binder);
    }
}
