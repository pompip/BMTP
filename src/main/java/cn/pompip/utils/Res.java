/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip.utils;

import java.io.File;
import java.net.URL;

public class Res {
    public static File get(String name) {
        return new File(getPath(name));
    }

    public static String getPath(String name) {
        URL resource = Res.class.getResource(name);
        if (resource != null) {
            return resource.getFile();
        } else {
            throw new RuntimeException(name + " not found");
        }
    }
}
