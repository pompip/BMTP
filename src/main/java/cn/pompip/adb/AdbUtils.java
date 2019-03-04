/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip.adb;

import com.alibaba.fastjson.JSON;
import cn.pompip.androidcontrol.DeviceInfo;

import java.util.ArrayList;
import java.util.List;

public class AdbUtils {

    /**
     * 将当前连接的设备列表转换为json
     * @return
     */
    public static String devices2JSON() {
        return devices2JSON(AdbServer.server().getDevices());
    }
    
    public static String devices2JSON(List<AdbDevice> devices) {
        ArrayList<DeviceInfo> list = new ArrayList<DeviceInfo>();
        for (AdbDevice device : devices) {
            list.add(new DeviceInfo(device));
        }
        return JSON.toJSONString(list);
    }
    
}
