/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;


public class WSHandler extends SimpleChannelInboundHandler<Object> {
    private static Logger logger = Logger.getLogger(WSHandler.class);
    private static boolean autorelease = false;

    /**
     * Websocket 连接帧
     */
    WebSocketServerHandshaker handshaker = null;
    /**
     * 该Handler对应的
     */
    IWebsocketEvent eventListener;

    /**
     * 处理分批发送过来的二进制数据
     */
    byte binaryCache[] = new byte[0];
    
    public WSHandler(IWebsocketEvent eventListener) {
        super(autorelease);
        this.eventListener = eventListener;
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest req = (FullHttpRequest) msg;
            
            if (!req.getDecoderResult().isSuccess() ||
                    (!"websocket".equals(req.headers().get("Upgrade")))) {
                ctx.fireChannelRead(req);
                return;
            }
            handleWSConnect(ctx, req);
        } else if (msg instanceof WebSocketFrame) {
            WebSocketFrame frame = (WebSocketFrame) msg;
            handlerWebSocketFrame(ctx, frame);
        }
        if (!autorelease)
            ReferenceCountUtil.release(msg);
    }

    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // 判断是否关闭链路的指令
        if (frame instanceof CloseWebSocketFrame && handshaker != null) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            if (eventListener != null) {
                eventListener.onDisconnect(ctx);
            }
            return;
        }
        // 判断是否ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        if (frame instanceof TextWebSocketFrame) {
            if (eventListener != null) {
                eventListener.onTextMessage(ctx, ((TextWebSocketFrame) frame).text());
            }
        } else if (frame instanceof BinaryWebSocketFrame || frame instanceof ContinuationWebSocketFrame) {
            ByteBuf byteBuf = frame.content();
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.getBytes(0, bytes);
            binaryCache = ArrayUtils.addAll(binaryCache, bytes);
            if (frame.isFinalFragment()) {
                byte[] b = binaryCache;
                binaryCache = new byte[0];
                if (eventListener != null) {
                    eventListener.onBinaryMessage(ctx, b);
                }
            }
        }
    }
    
    private void handleWSConnect(ChannelHandlerContext ctx, FullHttpRequest msg) {
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                null, null, true, Integer.MAX_VALUE, true);
        handshaker = wsFactory.newHandshaker(msg);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), msg);
        }
    }
}
