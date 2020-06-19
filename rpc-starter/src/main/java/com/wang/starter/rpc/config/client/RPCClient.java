package com.wang.starter.rpc.config.client;

import com.wang.starter.rpc.common.RequestId;
import com.wang.starter.rpc.common.rpc.RPCException;
import com.wang.starter.rpc.common.rpc.RpcFuture;
import com.wang.starter.rpc.common.rpc.RpcInvocation;
import com.wang.starter.rpc.config.client.handler.ClientMessageHandler;
import com.wang.starter.rpc.config.client.handler.decoder.RpcClientDecoder;
import com.wang.starter.rpc.config.client.handler.encoder.RpcClientEncoder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RPCClient {

    private String ip;

    private int port;

    private Bootstrap bootstrap;

    private EventLoopGroup group;

    private ClientMessageHandler clientMessageHandler;

    private static ConcurrentMap<String, RpcFuture<?>> pendingTasks = new ConcurrentHashMap<>();

    private boolean started;

    private boolean stopped;

    private Channel channel;

    private Throwable ConnectionClosed = new Exception("connection closed");

    public RPCClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.init();
    }

    public <T> RpcFuture<T> sendRpcInvocationAsync(RpcInvocation rpcInvocation) {
        if (!started) {
            connect();
            started = true;
        }
        rpcInvocation.setRequestId(RequestId.next());
        return sendMessage(rpcInvocation);
    }


    public <T> T send(RpcInvocation rpcInvocation) {
        RpcFuture<T> future = sendRpcInvocationAsync(rpcInvocation);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RPCException(e);
        }
    }

    public void init() {
        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup(1);
        bootstrap.group(group);
        clientMessageHandler = new ClientMessageHandler(this, pendingTasks);
        bootstrap.channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline pipe = ch.pipeline();
                pipe.addLast(new ReadTimeoutHandler(60));

                //inbound
                pipe.addLast(new RpcClientDecoder());

                //outbound
                pipe.addLast(new RpcClientEncoder());

                //inbound
                pipe.addLast(clientMessageHandler);
            }

        });
        bootstrap.option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_KEEPALIVE, true);
    }

    public void connect() {
        try {
            ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
            channel = channelFuture.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void reconnect() {
        if (stopped) {
            return;
        }
        bootstrap.connect(ip, port).addListener(future -> {
            if (future.isSuccess()) {
                return;
            }
            if (!stopped) {
                group.schedule(this::reconnect, 1, TimeUnit.SECONDS);
            }
            log.error("connect {}:{} failure", ip, port, future.cause());
        });
    }

    public void close() {
        stopped = true;
        clientMessageHandler.close();
        group.shutdownGracefully(0, 5000, TimeUnit.SECONDS);
    }

    public <T> RpcFuture<T> sendMessage(RpcInvocation rpcInvocation) {
        RpcFuture<T> future = new RpcFuture<T>();
        if (channel != null) {
            channel.eventLoop().execute(() -> {
                pendingTasks.put(rpcInvocation.getRequestId(), future);
                channel.writeAndFlush(rpcInvocation);
            });
        } else {
            future.fail(ConnectionClosed);
        }
        return future;
    }

}
