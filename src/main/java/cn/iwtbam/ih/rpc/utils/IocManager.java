package cn.iwtbam.ih.rpc.utils;

import cn.iwtbam.ih.rpc.RpcServer;

import cn.iwtbam.ih.rpc.annotations.RpcScan;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class IocManager {

    public static <T> Map<String, Class<?>>  loadClasses(String path, String packageName ,Class<T> annotation) {

        File file = new File(path);
        Map<String, Class<?>> beans = new HashMap<>();

        for(File subfile : file.listFiles()){
            if(!subfile.isDirectory())
                continue;
            loadClasses(subfile.getPath(), packageName + "." + subfile.getName(), annotation, beans);
        }
        return beans;
    }

    private static <T> void loadClasses(String path, String packages, Class<T> annotaion, Map<String, Class<?>> beans){
        File file = new File(path);
        File[] subFiles = file.listFiles();

        if(subFiles == null)
            return;

        for(File subFile : subFiles){
            if(!subFile.isDirectory()){
                if(subFile.getName().endsWith(".class")){
                    String name = subFile.getName();
                    String className = packages + "." + name.substring(0, name.length() - 6);
                    if(beans.containsKey(className))
                        continue;
                    try {
                        Class cls = Class.forName(className);
                        if(cls.getAnnotation(annotaion) != null){
                            beans.put(className, cls);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }

                }
            }
            loadClasses(subFile.getAbsolutePath(), packages + "." + subFile.getName(), annotaion, beans);
        }
    }

    public static void main(String[] args){
//        Map<String, Object> beans = loadClasses(RpcBootstrap.class, RpcConsumer.class);

        Class<?> cls = RpcServer.class;

        System.out.println(cls.getPackageName());
        System.out.println(cls.getResource("").getPath());

        RpcScan rs = RpcServer.class.getAnnotation(RpcScan.class);
        System.out.println(rs.getClass().getName());
//        System.out.println(RpcScan.class);
        //
//        Map<String, Object> beans = loadClasses(cls.getResource("").getPath(), cls.getPackageName(), rs.targets()[0]);
//
//
//        for(String classname : beans.keySet()){
//            System.out.println(classname + " : " + beans.get(classname));
//        }
    }

}
