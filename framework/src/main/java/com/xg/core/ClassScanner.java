package com.xg.core;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *  类扫描器
 */
public class ClassScanner
{
    /**
     *  扫描出给定包名下的所有类 (目前仅处理了jar 因为是在终端中启动内嵌的tomcat)
     * @param packageName 包名 如com.xg.handsome
     * @return 此包下的所有class的列表
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static List<Class<?>> scanClass(String packageName) throws IOException, ClassNotFoundException
    {
        List<Class<?>> classList=new ArrayList<>();
        // 将包名转为路径
        String path=packageName.replace(".", "/");
        // 获取当前的类加载器
        ClassLoader defaultClassLoader=Thread.currentThread().getContextClassLoader();
        // 传入路径 让类加载器加载此路径 获取资源的枚举对象
        Enumeration<URL> resources=defaultClassLoader.getResources(path);
        // 遍历枚举对象 对不同protocol的资源做不同的操作 都追加到classList中
        while(resources.hasMoreElements()){
            URL resource=resources.nextElement();
            if (resource.getProtocol().contains("jar")){
                // 如果资源类型为 jar
                JarURLConnection jarURLConnection= (JarURLConnection) resource.openConnection();
                String jarFilePath=jarURLConnection.getJarFile().getName();
                classList.addAll(getClassesFromJar(jarFilePath, path));
            }else{
                // 其他资源类型暂无操作
            }
        }
        return classList;
    }

    /**
     *  递归获取jar包中指定包名下的全部class
     * @param jarFilePath jar文件路径
     * @param path 当前扫描的包路径
     * @return jar文件中指定包中的类列表
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static List<Class<?>> getClassesFromJar(String jarFilePath, String path) throws IOException, ClassNotFoundException
    {
        List<Class<?>> classList=new ArrayList<>();
        JarFile jarFile=new JarFile(jarFilePath);
        Enumeration<JarEntry> jarEntryEnumeration=jarFile.entries();
        while(jarEntryEnumeration.hasMoreElements()){
            JarEntry jarEntry=jarEntryEnumeration.nextElement();
            String entryName=jarEntry.getName();
            if (entryName.startsWith(path) && entryName.endsWith(".class")){
                String classFullName=entryName.replace("/", ".").substring(0, entryName.length()-6);
                classList.add(Class.forName(classFullName));
            }
        }
        return classList;
    }
}
