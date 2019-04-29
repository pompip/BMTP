package cn.pompip.utils;


public class Log {
    public static void i(Object tag, Object message) {
        String t = tag instanceof String ? (String) tag : tag.getClass().getName();
        System.out.println(t + "  :  " + message);

    }

    public static void thread() {
        i("current thread", Thread.currentThread());
    }
}
