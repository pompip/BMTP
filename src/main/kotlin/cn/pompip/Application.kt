/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip

import cn.pompip.adb.AdbServer
import cn.pompip.server.AndroidControlServer

fun main() {
    AdbServer.server().listenADB();
    AdbServer.server().listenUSB()
    AndroidControlServer().listen(8081)
}