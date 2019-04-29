/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip.adb;

import cn.pompip.utils.Log;
import com.android.ddmlib.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AndroidDevice {
    private IDevice iDevice;

    public AndroidDevice(IDevice iDevice) {
        this.iDevice = iDevice;
    }

    public void installApp(App app) {
        try {
            iDevice.installPackage(app.getURL().getPath(), true, new InstallReceiver());
            app.setClient(iDevice.getClient(app.packageName));
            Log.i(this, "安装完成");
        } catch (InstallException e) {
            e.printStackTrace();
        }
    }

    public Client getClient(App app) {
        Client client = iDevice.getClient(app.packageName);
        app.setClient(client);
        return client;
    }

    public void dumpsysPackage(App app) {

        ResultReceiver resultReceiver = new ResultReceiver();
        try {
            iDevice.executeShellCommand("dumpsys package " + app.packageName, resultReceiver);
            List<String> lineList = resultReceiver.getResult();
            for (String line : lineList) {
                if (line.contains("launcher")) {
                    Log.i(this, line);
                    break;

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void findLauncherActivity(App app) {
        try {
            ResultReceiver resultReceiver = new ResultReceiver();
            iDevice.executeShellCommand("dumpsys package " + app.packageName, resultReceiver);
            List<String> lineList = resultReceiver.getResult();
            boolean startRecord = false;
            a:
            for (String line : lineList) {
                if (line.contains("android.intent.action.MAIN")) {
                    startRecord = true;
                    continue;
                }
                if (startRecord) {
                    Matcher matcher = Pattern.compile(app.packageName + "/[\\w.]*").matcher(line);
                    while (matcher.find()) {
                        String group = matcher.group();
                        Log.i(this, group);
                        if (group != null) {
                            break a;
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void exeCommandNoResult(String command) {
        try {
            NullOutputReceiver resultReceiver = new NullOutputReceiver();
            iDevice.executeShellCommand(command, resultReceiver);

        } catch (TimeoutException | AdbCommandRejectedException | IOException | ShellCommandUnresponsiveException e) {
            e.printStackTrace();
        }
    }

    public void exeCommand(String command) {
        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            CollectingOutputReceiver resultReceiver = new CollectingOutputReceiver(countDownLatch);
            iDevice.executeShellCommand(command, resultReceiver);

            countDownLatch.await();
            Log.i(this, resultReceiver.getOutput());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void root() {
        try {
            iDevice.root();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (AdbCommandRejectedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ShellCommandUnresponsiveException e) {
            e.printStackTrace();
        }
    }

    public ByteArrayInputStream generateCap() {
        try {
            OutputStreamReceiver outputReceiver = new OutputStreamReceiver();
            iDevice.executeShellCommand("screencap -p", outputReceiver);
            byte[] cap = outputReceiver.getOutputStream().toByteArray();
            ;
            System.out.println(cap.length);
            return new ByteArrayInputStream(outputReceiver.getOutputStream().toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
