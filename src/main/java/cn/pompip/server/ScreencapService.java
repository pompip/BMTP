/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip.server;

import cn.pompip.adb.AdbDevice;
import cn.pompip.minicap.ScreencapBase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ScreencapService extends HashMap<AdbDevice, ScreencapBase> {
    
    public List<ScreencapBase> filterWithDevice(AdbDevice device) {
        List<ScreencapBase> lst = new LinkedList<>();
        Iterator iter = entrySet().iterator();
        
        while (iter.hasNext()) {
            Entry entry = (Entry) iter.next();

            AdbDevice key = (AdbDevice) entry.getKey();
            if (key.equals(device)) {
                lst.add((ScreencapBase) entry.getValue());
            }
        }
        return lst;
    }
    
    
    
}
