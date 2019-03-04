/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip.androidcontrol;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.base.Strings;
import cn.pompip.adb.AdbDevice;

/**
 * Created by harry on 2017/4/21.
 *
 * 用于网络传输，会被转换成json
 */
public class DeviceInfo {

    /**
     * serialNumber
     */
    private String sn, brand, model;

    private int width, height;

    public DeviceInfo(AdbDevice device) {

        sn = device.getSerialNumber();
        
        String str = device.findPropertyCahe(AdbDevice.SCREEN_SIZE);
        if (!Strings.isNullOrEmpty(str)) {
            String[] sizeStr = str.split("x");
            width = Integer.parseInt(sizeStr[0].trim());
            height = Integer.parseInt(sizeStr[1].trim());
        }
        
    }

    @JSONField(name = "sn")
    public String getSn() {
        return sn;
    }

    @JSONField(name = "w")
    public int getWidth() {
        return width;
    }

    @JSONField(name = "h")
    public int getHeight() {
        return height;
    }

    @JSONField(name = "brand")
    public String getBrand() {
        return brand;
    }

    @JSONField(name = "model")
    public String getModel() {
        return model;
    }
}
