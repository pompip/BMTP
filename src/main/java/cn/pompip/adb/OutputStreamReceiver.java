/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip.adb;

import com.android.ddmlib.IShellOutputReceiver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class OutputStreamReceiver implements IShellOutputReceiver {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    CountDownLatch countDownLatch = new CountDownLatch(1);

    @Override
    public void addOutput(byte[] data, int offset, int length) {
        bos.write(data,offset,length);
        try {
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void flush() {

    }

    @Override
    public boolean isCancelled() {
        countDownLatch.countDown();
        return false;
    }

    public ByteArrayOutputStream getOutputStream(){
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return bos;

    }
}
