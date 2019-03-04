/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip.engine;

import com.google.common.util.concurrent.SettableFuture;
import cn.pompip.adb.AdbDevice;
import cn.pompip.adb.AdbServer;
import cn.pompip.minitouch.Minitouch;
import cn.pompip.minitouch.MinitouchListener;

import java.util.concurrent.ExecutionException;

/**
 * Created by harry on 2017/5/11.
 */
public class EngineDevice {

    private AdbDevice device;
    private Minitouch minitouch;
    private boolean minitouchOpen = false;

    public static EngineDevice getDevice(String serialNumber) {
        AdbDevice iDevice = AdbServer.server().getDevice(serialNumber);
        if (iDevice != null) {
            EngineDevice device = new EngineDevice(iDevice);
            if (device.isMinitouchOpen()) {
                return device;
            }
        }
        return null;
    }

    public EngineDevice(AdbDevice iDevice) {
        this.device = iDevice;
        minitouch = new Minitouch(iDevice);
        SettableFuture future = SettableFuture.create();

        minitouch.addEventListener(new MinitouchListener() {
            @Override
            public void onStartup(Minitouch minitouch, boolean success) {
                future.set(success);
            }

            @Override
            public void onClose(Minitouch minitouch) {
                minitouchOpen = false;
            }
        });
        minitouch.start();

        // 等待，指导Minitouch启动完毕
        try {
            Boolean success = (Boolean)future.get();
            if (success) {
                minitouchOpen = true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    protected boolean isMinitouchOpen() {
        return minitouchOpen;
    }

    public void touchDown(int x, int y) {
        minitouch.sendEvent("d 0 " + x + " " + y + " 50\nc\n");
    }

    public void touchMove(int x, int y) {
        minitouch.sendEvent("m 0 " + x + " " + y + " 50\nc\n");
    }

    public void touchUp() {
        minitouch.sendEvent("u 0\nc\n");

    }

    public String executeShellAndGetString(String command) {
        return AdbServer.executeShellCommand(device.getIDevice(), command);
    }

    public void startApp(String str) {
        AdbServer.executeShellCommand(device.getIDevice(), "am start " + str);
    }

}
