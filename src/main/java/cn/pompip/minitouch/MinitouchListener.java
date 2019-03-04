/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip.minitouch;

/**
 * Created by harry on 2017/4/19.
 */
public interface MinitouchListener {
    // minitouch启动完毕后
    public void onStartup(Minitouch minitouch, boolean success);
    // minitouch关闭后
    public void onClose(Minitouch minitouch);
}
