package com.wang.starter.rpc.config.server;

import com.wang.starter.rpc.common.rpc.RpcServerDecoder;
import com.wang.starter.rpc.common.rpc.RpcServerEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RPCServer {

    private String ip;

    private int port;

    private int ioThreads;

    private EventLoopGroup group;

    private ServerMessageCollector collector;

    private Channel serverChannel;

    public RPCServer(String ip, int port, int ioThreads, ServerMessageCollector collector) {
        this.ip = ip;
        this.port = port;
        this.ioThreads = ioThreads;
        this.collector = collector;
    }

    void start() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        group = new NioEventLoopGroup(ioThreads);
        bootstrap.group(group).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
                ChannelPipeline pipe = ch.pipeline();
                pipe.addLast(new ReadTimeoutHandler(60));
                pipe.addLast(new RpcServerDecoder());
                pipe.addLast(new RpcServerEncoder());
                pipe.addLast(collector);
            }
        }).option(ChannelOption.SO_BACKLOG, 100).option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.TCP_NODELAY, true).childOption(ChannelOption.SO_KEEPALIVE, true);
        serverChannel = bootstrap.bind(this.ip, this.port).channel();
        log.warn("rpc server started @ {}:{}\n", ip, port);
    }

    public void stop() {
        // 先关闭服务端套件字
        serverChannel.close();
        // 再斩断消息来源，停止io线程池
        group.shutdownGracefully();
        // 最后停止业务线程
        collector.closeGracefully();
    }

}