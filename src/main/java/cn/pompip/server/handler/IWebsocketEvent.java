/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip.server.handler;

import io.netty.channel.ChannelHandlerContext;

public interface IWebsocketEvent {
    void onConnect(ChannelHandlerContext ctx);
    void onDisconnect(ChannelHandlerContext ctx);
    void onTextMessage(ChannelHandlerContext ctx, String text);
    void onBinaryMessage(ChannelHandlerContext ctx, byte[] data);
}
