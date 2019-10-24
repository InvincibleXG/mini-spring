package com.xg.web.servlet;

import com.xg.web.handler.HandlerManager;
import com.xg.web.handler.MappingHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 *  Dispatcher Servlet
 */
public class MyServlet implements Servlet
{
    @Override
    public void init(ServletConfig config) throws ServletException
    {

    }

    @Override
    public ServletConfig getServletConfig()
    {
        return null;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException
    {
        // 自定义响应
//        res.getWriter().println("Hello MyServlet");
        System.out.println(req.getRemoteHost()+"请求"+((HttpServletRequest)req).getRequestURL());
        for (MappingHandler mappingHandler:HandlerManager.mappingHandlerList){
            try {
                if (mappingHandler.handle(req, res)){
                    return;
                }
                System.out.println(" ——没有Servlet可响应");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getServletInfo()
    {
        return null;
    }

    @Override
    public void destroy()
    {

    }
}
