package com.xg.starter;

import com.xg.core.ClassScanner;
import com.xg.web.handler.HandlerManager;
import com.xg.web.server.TomcatServer;

import org.apache.catalina.LifecycleException;

import java.util.List;

public class MiniApplication
{
    /**
     * 框架入口类
     * @param clazz 应用的入口类
     * @param args 应用入口类的启动参数 数组
     */
    public static void run(Class<?> clazz, String[] args)
    {
        TomcatServer tomcatServer=new TomcatServer(args);
        try {
            tomcatServer.startServer();
            // 通过启动类获取根包
            String packageName=clazz.getPackage().getName();
            // 扫描根包得到所有的类
            List<Class<?>> classList=ClassScanner.scanClass(packageName);
//            classList.forEach(it->System.out.println(it.getName()));
            // 对使用@Controller注解的类进行注册 之后DispatcherServlet将据此分配对应的请求
            HandlerManager.resolveMappingHandler(classList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
