package com.xg.web.handler;

import com.xg.web.mvc.Controller;
import com.xg.web.mvc.RequestMapping;
import com.xg.web.mvc.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 *  Handler 管理器 (实质上是 Servlet(controller的method) 的(反射)注册中心)
 */
public class HandlerManager
{
    /**
     *  保存所有 handler(controller的method)
     */
    public static List<MappingHandler> mappingHandlerList=new ArrayList<>();

    public static void resolveMappingHandler(List<Class<?>> clazzList)
    {
        for (Class<?> clazz:clazzList){
            if (clazz.isAnnotationPresent(Controller.class)){
                // 如果此类有Controller注解 则转为 “handler” 收集管理起来
                parseHandlerFromController(clazz);
            }
        }
    }

    private static void parseHandlerFromController(Class<?> clazz)
    {
        Method[] methods=clazz.getDeclaredMethods();
        for (Method method:methods){
            if (!method.isAnnotationPresent(RequestMapping.class)){
                continue;
            }
            String uri=method.getDeclaredAnnotation(RequestMapping.class).value();
            List<String> paramNameList=new ArrayList<>();
            Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                if (parameter.isAnnotationPresent(RequestParam.class)) {
                    paramNameList.add(parameter.getDeclaredAnnotation(RequestParam.class).value());
                }
            }
            String[] params=paramNameList.toArray(new String[paramNameList.size()]);
            MappingHandler mappingHandler=new MappingHandler(uri, clazz, method, params);
            mappingHandlerList.add(mappingHandler);
        }
    }
}
