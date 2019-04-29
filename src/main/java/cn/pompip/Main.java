/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip;

import cn.pompip.adb.ADBServer;
import cn.pompip.adb.AndroidDevice;
import cn.pompip.netty.NettyServer;

import java.util.HashMap;

public class Main {
    public static void main(String[] args){
        ADBServer adbServer = ADBServer.getInstance();
        adbServer.start();

        NettyServer.getInstance().start();
        HashMap<String, AndroidDevice> iDeviceList = adbServer.getIDeviceList();
        iDeviceList.values().forEach(androidDevice -> {
            NettyServer.getInstance().setAndroidDevice(androidDevice);
        });


    }
}
