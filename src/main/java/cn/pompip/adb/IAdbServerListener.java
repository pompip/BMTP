/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip.adb;

/**
 * Created by harry on 2017/8/4.
 */
public interface IAdbServerListener {
    void onAdbDeviceConnected(AdbDevice device);
    void onAdbDeviceDisConnected(AdbDevice device);
}
