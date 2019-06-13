package com.xg.beans;

import com.xg.web.mvc.Controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory
{
    private static Map<Class<?>, Object> clazzToBean=new ConcurrentHashMap<>();

    public static Object getBean(Class<?> clazz)
    {
        return clazzToBean.get(clazz);
    }

    public static void initBean(List<Class<?>> classList) throws Exception
    {
        List<Class<?>> toCreate=new ArrayList<>(classList);
        while(toCreate.size()>0){
            int remainSize=toCreate.size(); //记录本次循环开始时的class数量

            for (int i=0; i<toCreate.size(); i++){
                // 判断是否成功创建bean 创建完成后移出classList的副本 所以每次循环都重新获取长度
                if (finishCreate(toCreate.get(i))) {
                    toCreate.remove(i);
                }
            }
            // 如果循环结束bean一个都没有创建 那么抛出循环依赖异常
            if (remainSize==toCreate.size()) throw new Exception("Cycle dependency");
        }
    }

    private static boolean finishCreate(Class<?> clazz) throws IllegalAccessException, InstantiationException
    {
        // 如果没有@bean注解和@controller注解 直接返回
        if (!clazz.isAnnotationPresent(Bean.class) && !clazz.isAnnotationPresent(Controller.class)){
            return true;
        }
        // 注解匹配成功 则创建实例
        Object bean=clazz.newInstance();
        // 对字段进行检查 注入依赖 如果依赖还未创建 则返回false表示bean创建失败
        for (Field field:clazz.getDeclaredFields()){
            if (field.isAnnotationPresent(AutoWired.class)){
                Class<?> fieldType=field.getType();
                Object reliantBean=BeanFactory.getBean(fieldType);
                if (reliantBean==null) return false;
                field.setAccessible(true); // 允许修改字段
                field.set(bean, reliantBean);
            }
        }
        clazzToBean.put(clazz, bean);
        return true;
    }
}
