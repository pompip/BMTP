/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip.adb;

import cn.pompip.utils.Log;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class ADBServer {
    HashMap<String, AndroidDevice> deviceMap = new HashMap<>();

    private ADBServer(){}
    static class Inner {
        static ADBServer adbServer = new ADBServer();
    }

    public static ADBServer getInstance() {
        return Inner.adbServer;
    }

    public HashMap<String, AndroidDevice> getIDeviceList(){
        return deviceMap;
    }

    public void start() {
        AndroidDebugBridge.init(true);
        AndroidDebugBridge adb = AndroidDebugBridge.createBridge();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        AndroidDebugBridge.addDeviceChangeListener(new AndroidDebugBridge.IDeviceChangeListener() {
            @Override
            public void deviceConnected(IDevice device) {
                deviceMap.put(device.getSerialNumber(),new AndroidDevice(device));
                countDownLatch.countDown();
            }

            @Override
            public void deviceDisconnected(IDevice device) {
                deviceMap.remove(device.getSerialNumber());
                countDownLatch.countDown();
            }

            @Override
            public void deviceChanged(IDevice device, int changeMask) {
                deviceMap.put(device.getSerialNumber(),new AndroidDevice(device));
                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();
            Log.i(this,"init success");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
