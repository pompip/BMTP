/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip.adb;

import cn.pompip.utils.ArrayUtil;
import com.android.ddmlib.IShellOutputReceiver;

import java.util.Arrays;

/**
 * Created by harry on 2017/4/17.
 */
public class BinaryOutputReceiver implements IShellOutputReceiver {

    private byte[] output = new byte[0];

    @Override
    public void addOutput(byte[] bytes, int offest, int len) {
        byte[] b = Arrays.copyOfRange(bytes, offest, offest + len);
        output = ArrayUtil.mergeArray(output, b);
//        System.out.println(len + ":" + output);
//        System.out.println(new String(bytes, offest, len));
    }

    @Override
    public void flush() {
        System.out.println("flush");
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    public byte[] getOutput() {
        return output;
    }
}
