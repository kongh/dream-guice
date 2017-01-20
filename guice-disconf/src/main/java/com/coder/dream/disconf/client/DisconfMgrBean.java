package com.coder.dream.disconf.client;

import com.baidu.disconf.client.store.inner.DisconfCenterHostFilesStore;
import com.baidu.disconf.client.support.registry.Registry;
import com.baidu.disconf.client.support.utils.StringUtil;
import com.coder.dream.disconf.client.support.registry.impl.GuiceRegistry;
import com.coder.dream.guice.lifecycle.Dispose;
import com.coder.dream.guice.lifecycle.Start;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by konghang on 2016/11/28.
 */
@Singleton
public class DisconfMgrBean {

    @Inject
    @Named(value = "disconf.scan.package")
    private String scanPackage = null;

    public final static String SCAN_SPLIT_TOKEN = ",";

    private Registry registry;

    @Inject
    public DisconfMgrBean(Injector injector) {
        this.registry = new GuiceRegistry(injector);
    }

    /**
     * 当且仅当运行在启动guice-lifecycle时生效,
     * 如果你需要在其他框架中使用生命周期来管理启动,那么请集成改类
     * 或者你可以选择在生命周期的启动和停止时候手动调用
     * start
     */
    @Start
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

    /**
     * 当且仅当运行在guice-lifecycle时生效,
     * 如果你需要在其他框架中使用生命周期来管理启动,那么请集成改类
     * 或者你可以选择在生命周期的启动和停止时候手动调用
     * stop
     */
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
}
