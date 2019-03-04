/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip.androidcontrol.server;

import com.alibaba.fastjson.JSON;
import cn.pompip.adb.AdbDevice;
import cn.pompip.adb.AdbServer;
import cn.pompip.androidcontrol.DeviceInfo;

import java.util.ArrayList;

public class BaseServer {

    /**
     * 获取设备信息的JSON数据
     * @return json
     */
    public static String getDevicesJSON() {
        ArrayList<DeviceInfo> list = new ArrayList<DeviceInfo>();
        for (AdbDevice device : AdbServer.server().getDevices()) {
            list.add(new DeviceInfo(device)); // TODO 耗时长，需优化
        }
        return JSON.toJSONString(list);
    }





}
