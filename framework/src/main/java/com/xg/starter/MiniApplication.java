package com.xg.starter;

import com.xg.web.server.TomcatServer;

import org.apache.catalina.LifecycleException;

public class MiniApplication
{
    /**
     * 框架入口类
     * @param clazz 应用的入口类
     * @param args 应用入口类的启动参数 数组
     */
    public static void run(Class<?> clazz, String[] args)
    {
        System.out.println("fuck mini");
        TomcatServer tomcatServer=new TomcatServer(args);
        try {
            tomcatServer.startServer();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }
}
