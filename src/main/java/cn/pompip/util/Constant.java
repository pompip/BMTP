/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip.util;

import java.io.*;
import java.util.Properties;

/**
 * Created by harry on 2017/4/17.
 */
public class Constant {

    public static final String PROP_ABI = "ro.product.cpu.abi";
    public static final String PROP_SDK = "ro.build.version.sdk";
    public static final String PROPERTIES_FILE = "yeetor.properties";
    
    private static Properties properties = null;
    
    /**
     * 这里初始化配置文件......yeetor.properties
     * 
     * 优先级：同级目录 > 包内部默认文件
     */
    static {
        properties = new Properties();
        if (!loadPropertiesWithFileName(JarTool.getJarDir() + File.separator + PROPERTIES_FILE)) {
            InputStream inputStream = ClassLoader.getSystemResourceAsStream(PROPERTIES_FILE);
            if (!loadPropertiesWithStream(inputStream)) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    static boolean loadPropertiesWithFileName(String fileName) {
        File file = new File(fileName);
        if (file.isHidden() || !file.exists()) {
            return false;
        }
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            loadPropertiesWithStream(inputStream);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
    
    static boolean loadPropertiesWithStream(InputStream inputStream) {
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    
    public static File getResourceDir() {
        File resources = new File(properties.getProperty("resource.root"));
        return resources;
    }

    public static File getResourceFile(String name) {
        File dir = getResourceDir();
        return new File(dir, name);
    }

    public static File getMinicap(String abi) {
        File resources = getResourceDir();
        if (resources.exists()) {
            return new File(resources, "minicap" + File.separator + "bin" +
                    File.separator + abi + File.separator + "minicap");
        }
        return null;
    }

    public static File getMinicapSo(String abi, String sdk) {
        File resources = getResourceDir();
        if (resources.exists()) {
            return new File(resources, "minicap" + File.separator + "shared" +
                    File.separator + "android-" + sdk + File.separator + abi + File.separator + "minicap.so");
        }
        return null;
    }

    public static File getMinitouchBin(String abi) {
        File resources = getResourceDir();
        if (resources.exists()) {
            return new File(resources, "minitouch" + File.separator +
                    File.separator + abi + File.separator + "minitouch");
        }
        return null;
    }

    public static File getTmpFile(String fileName) {
        String tmpdir = System.getProperty("java.io.tmpdir");
        File tmp = new File(tmpdir);
        tmp = new File(tmp, "AndroidControl");
        if (!tmp.exists()) {
            tmp.mkdirs();
        }
        return new File(tmp, fileName);
    }

}
