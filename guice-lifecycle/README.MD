#guice-lifecycle
guice生命周期

###怎么使用?

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
    
    List<Module> modulesToload = new ArrayList<Module>();
    modulesToload.add(LifecycleSupport.getModule());
    
    Injector injector = Guice.createInjector(modulesToload);
    LifecycleService lifecycleService = injector.getInstance(LifecycleService.class);
    lifecycleService.start();
    
    LifecycleBean lifecycleBean = injector.getInstance(LifecycleBean.class);
    System.out.println(lifecycleBean);
    try {
           Thread.sleep(1000l);
        } catch (InterruptedException e) {
           e.printStackTrace();
        }
    
    lifecycleService.stop();
    
完整的例子：参考com.coder.dream.lifecycle.GuiceLifecycleDemo