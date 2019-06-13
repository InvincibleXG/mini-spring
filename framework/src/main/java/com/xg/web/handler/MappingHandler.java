package com.xg.web.handler;

import com.xg.beans.BeanFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 *  通过反射+注解匹配获取所有Servlet(Controller方法)的URI、Class、Method、Parameters
 */
public class MappingHandler
{
    private String uri;
    private Class<?> controllerClazz;
    private Method method;
    private String[] args;

    public MappingHandler(String uri, Class<?> clazz, Method method, String[] args)
    {
        this.uri=uri;
        this.controllerClazz=clazz;
        this.method=method;
        this.args=args;
    }

    /**
     *  判断此 handler 是否可以处理对应的请求
     * @param req ServletRequest
     * @param res ServletResponse
     * @return true - 请求路由与handler的uri匹配
     *              false - 不匹配的请求
     */
    public boolean handle(ServletRequest req, ServletResponse res) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException
    {
        String requestURI=((HttpServletRequest)req).getRequestURI();
        if (!uri.equals(requestURI)) return false;
        // 通过参数名 依次从request中取出对应参数
        Object[] parameters=new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            parameters[i]=req.getParameter(args[i]);
        }
        // 反射实例化一个 controller 对象
//        Object controller=controllerClazz.newInstance();
        Object controller=BeanFactory.getBean(controllerClazz);
        // 反射调用 controller的handler方法 也就是 servlet 方法响应匹配的请求
        Object response=method.invoke(controller, parameters);
        // 将请求处理的结果进行响应
        res.getWriter().println(response.toString());
        return true;
    }
}
