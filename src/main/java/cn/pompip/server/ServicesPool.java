/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip.server;

public class ServicesPool {
    
    private static ServicesPool instance = null;

    ScreencapService screencapService = null;
    
    public static ServicesPool getInstance() {
        if (instance == null) {
            instance = new ServicesPool();
        }
        return instance;
    }
    
    private ServicesPool() {
        screencapService = new ScreencapService();
    }


    public ScreencapService getScreencapService() {
        return screencapService;
    }
}
