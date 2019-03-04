

/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip;

import cn.pompip.adb.AdbServer;
import cn.pompip.server.AndroidControlServer;
import org.apache.log4j.Logger;

/**
 * Created by harry on 2017/4/15.
 */
public class Main {
    private static Logger logger = Logger.getLogger(Main.class);
    
    /**
     * [server port]
     * [client ip port serialNumber]
     * [client ip port]
     */
    public static void main(String[] args) throws Exception {
        
        // nc 127.0.0.1 4433 进入命令行交互界面
        // Console.getInstance().listenOnTCP(4433);
        
        // 监听USB的变化
        AdbServer.server().listenUSB();
        
        // 同步ADB的设备列表
        AdbServer.server().listenADB();


        AndroidControlServer server = new AndroidControlServer();
        server.listen(6655);
    }
}
