package cn.pompip.utils;

import java.util.Arrays;

public class ArrayUtil {
    public static byte[] mergeArray(byte[] a1, byte[] a2) {
        byte[] arr = Arrays.copyOf(a1, a1.length + a2.length);
        System.arraycopy(a2, 0, arr, a1.length, a2.length);
        return arr;
    }
}
