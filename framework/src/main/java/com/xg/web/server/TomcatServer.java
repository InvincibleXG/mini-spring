package com.xg.web.server;

import com.xg.web.servlet.MyServlet;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

public class TomcatServer
{
    private Tomcat tomcat;
    private String[] args;

    public TomcatServer(String[] args)
    {
        this.args=args;
    }

    public void startServer() throws LifecycleException
    {
        // 实例化 Tomcat
        tomcat=new Tomcat();
        tomcat.setPort(6699);
        tomcat.start();
        // 创建标准上下文 并配置默认的 Tomcat监听器
        Context context=new StandardContext();
        context.setPath("");
        context.addLifecycleListener(new Tomcat.FixContextListener());
        // 实例化自定义 Servlet 并配置到Tomcat上下文中  此 Servlet 响应新增路由 /test.json
        MyServlet servlet=new MyServlet();
        Tomcat.addServlet(context, "MyServlet", servlet).setAsyncSupported(true);
        context.addServletMappingDecoded("/", "MyServlet");
        tomcat.getHost().addChild(context);
        // 配置Tomcat等待线程 防止自动关闭
        Thread awaitThread=new Thread("tomcat_await_thread")
        {
            @Override
            public void run()
            {
                TomcatServer.this.tomcat.getServer().await();
            }
        };
        awaitThread.setDaemon(false);
        awaitThread.start();
    }
}
