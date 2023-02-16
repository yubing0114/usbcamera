//package com.yslz.usbcamera.utils;
//
//import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//
//import java.util.Properties;
//
///**
// * <p>
// * 配置文件读取工具类
// * <p>
// *
// * @author: yb
// * @date: 2022/1/17 16:48
// */
//public class PropertyUtil {
//
//    // yml文件名
//    private static String PROPERTY_NAME = "application.yml";
//
//    /**
//     * 根据yml配置文件的key获取对应的Value
//     *
//     * @param: key
//     * @return: java.lang.String
//     */
//    public static String getValue(String key) {
//        Resource resource = new ClassPathResource(PROPERTY_NAME);
//        Properties properties;
//        try {
//            YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
//            yamlFactory.setResources(resource);
//            properties = yamlFactory.getObject();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        return properties.getProperty(key);
//    }
//
//
//
//}
//
