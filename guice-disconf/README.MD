#guice-disconf

disconf的guice集成实现，为guice提供分布式系统配置管理能力

###怎么启动guice-disconf？
    //设定disconf扫描包
    Properties properties = new Properties();
    properties.put("disconf.scan.package", "com");
    
    //设定加载的Module,DisconfModule是disconf的启动核心
    List<Module> modulesToload = new ArrayList<Module>();
    modulesToload.add(new DisconfModule(properties));
    modulesToload.add(LifecycleSupport.getModule());

    //创建guice容器
    Injector injector = Guice.createInjector(modulesToload);
    
    //运用guice生命周期特性
    LifecycleService lifecycleService = injector.getInstance(LifecycleService.class);
    lifecycleService.start();

    //查看disconf注入的JedisConfig实例
    JedisConfig instance = injector.getInstance(JedisConfig.class);
    System.out.println(instance);
    try {
        Thread.sleep(1000000000L);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    
    //程序退出时，通知容器已经停止，简单程序，正确/好的方式是注册虚拟机钩子函数
    lifecycleService.stop();
disconf的服务端参考http://disconf.readthedocs.io/zh_CN/latest/ 进行部署    
disconf的配置参考disconf.properties
