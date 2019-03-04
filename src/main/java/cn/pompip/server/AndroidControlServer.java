/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip.server;


import cn.pompip.server.handler.HTTPHandler;
import cn.pompip.server.handler.TCPHandler;
import cn.pompip.server.handler.WSHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class AndroidControlServer extends BaseServer {

    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    public void listen(int port) throws InterruptedException {
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("tcp", new TCPHandler());
                            ch.pipeline().addLast("http-codec",new HttpServerCodec());
                            ch.pipeline().addLast("aggregator",new HttpObjectAggregator(65536));
                            ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
                            ch.pipeline().addLast("websocket", new WSHandler(new WSServer()));
                            ch.pipeline().addLast("http", new HTTPHandler(new HttpServer()));
                        }
                    });
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
