/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip.server.handler;

import cn.pompip.server.HttpServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

public class HTTPHandler extends SimpleChannelInboundHandler<Object> {
    
    HttpServer server;
    
    public HTTPHandler(HttpServer server) {
        this.server = server;
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof HttpRequest)) {
            throw new IllegalArgumentException("Not a http request!");
        }
        
        HttpRequest request = (HttpRequest) msg;

        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        boolean isKeepAlive = HttpUtil.isKeepAlive(request);

        
        response.headers().add("Access-Control-Allow-Origin", "*");
        response.headers().add("Server", "Yeetor");
        
        server.onRequest(ctx, request, response);
        
//        HttpResponse response = server.onRequest(ctx, request);
//        if (response == null) {
//            return;
//        }
        
//        if (isKeepAlive) {
//            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
//        } else {
//            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
//        }
//        ctx.writeAndFlush(response);
    }
}
 
