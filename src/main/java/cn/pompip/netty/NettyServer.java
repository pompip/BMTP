/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip.netty;

import cn.pompip.adb.AndroidDevice;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.stream.ChunkedWriteHandler;
import net.coobird.thumbnailator.Thumbnails;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;


public class NettyServer {
    private NettyServer() {
    }

    static class Inner {
        static NettyServer nettyServer = new NettyServer();
    }

    public static NettyServer getInstance() {
        return Inner.nettyServer;
    }

    public void start() {
        try {
            new ServerBootstrap()
                    .group(new NioEventLoopGroup(), new NioEventLoopGroup())
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new MyChannel())
                    .bind(8000).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    AndroidDevice androidDevice;
    public void setAndroidDevice(AndroidDevice androidDevice){
        this.androidDevice = androidDevice;
    }

     class MyChannel extends ChannelInitializer<NioSocketChannel> {

        @Override
        protected void initChannel(NioSocketChannel ch) throws Exception {
//                    .addLast(new LoggingHandler(LogLevel.INFO))

            ch.pipeline()
                    .addLast("httpServerCodec", new HttpServerCodec())
                    .addLast(new ChunkedWriteHandler())
//                    .addLast("decoder", new HttpRequestDecoder())
//                    .addLast("encoder", new HttpResponseEncoder())
                    .addLast(new HttpObjectAggregator(8192))
                    .addLast(new WebSocketServerProtocolHandler("/ws"))


//                    .addLast(new SimpleChannelInboundHandler<HttpRequest>() {
//                        @Override
//                        protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {
//                            System.out.println(msg.uri());
//                            System.out.println(msg.method());
//                            System.out.println(msg.getClass());
//                            if (msg.uri().equals("/favicon.ico")) {
//                                return;
//                            }
//
//                            ChunkedFile file = new ChunkedFile(Res.get("/web/index.html"));
//                            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
//                                    HttpResponseStatus.OK);
//
//                            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8")
//                                    .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
//                                    .add(HttpHeaderNames.CONTENT_LENGTH, file.length());
//
//                            ctx.write(response);
//                            ctx.write(file);
//
//                            ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
//
//                            future.addListener(ChannelFutureListener.CLOSE);
//
//                        }
//                    })
                    .addLast(new SimpleChannelInboundHandler<TextWebSocketFrame>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
                            System.out.println(msg.text());

                            ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间: " + LocalDateTime.now()));

//                            File file = new File("C:\\Users\\chong\\Desktop\\1.jpg");
//                            FileInputStream fileInputStream = new FileInputStream(file);
//                            byte[] bytes = new byte[fileInputStream.available()];
//                            int read = fileInputStream.read(bytes);
//                            System.out.println(read);
//                            fileInputStream.close();
                            ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间: " + LocalDateTime.now()));
                            while (true){
                                System.out.println("start");

                                ByteArrayOutputStream os = new ByteArrayOutputStream();
                                Thumbnails.of(androidDevice.generateCap()).scale(0.1f).outputQuality(0.1f) .toOutputStream(os);
                                byte[] bytes = os.toByteArray();
                                System.out.println(bytes.length);
                                ctx.channel().writeAndFlush(new BinaryWebSocketFrame(Unpooled.copiedBuffer(bytes)));
                            }
                        }

                    })
                    .addLast(new SimpleChannelInboundHandler<ContinuationWebSocketFrame>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, ContinuationWebSocketFrame msg) throws Exception {
                            System.out.println(msg.text());
                        }
                    })
                    .addLast(new SimpleChannelInboundHandler<CloseWebSocketFrame>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, CloseWebSocketFrame msg) throws Exception {
                            System.out.println(msg.reasonText());
                        }
                    });
        }
    }
}
