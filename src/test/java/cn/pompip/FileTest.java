/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip;

import cn.pompip.util.Constant;
import org.junit.Test;

import java.io.File;

public class FileTest {
    @Test
    public void testFilePath(){
        File minitouch_bin = Constant.getMinitouchBin("arm64-v8a");
        System.out.println( minitouch_bin.exists());
        System.out.println(minitouch_bin.getAbsolutePath());;
    }
}
