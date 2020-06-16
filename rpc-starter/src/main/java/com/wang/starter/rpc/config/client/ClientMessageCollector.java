package com.wang.starter.rpc.config.client;

import com.wang.starter.rpc.common.rpc.RpcInvocation;
import com.wang.starter.rpc.common.rpc.RpcResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Sharable
public class ClientMessageCollector extends ChannelInboundHandlerAdapter {

    private RPCClient client;

    private volatile ChannelHandlerContext context;

    private static ConcurrentMap<String, RpcFuture<?>> pendingTasks = new ConcurrentHashMap<>();

    private Throwable ConnectionClosed = new Exception("starter connection not active error");

    public ClientMessageCollector(RPCClient client,ConcurrentMap<String, RpcFuture<?>> pendingTasks) {
        this.client = client;
        this.pendingTasks = pendingTasks;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.context = ctx;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        this.context = null;
        pendingTasks.forEach((__, future) -> {
            future.fail(ConnectionClosed);
        });
        pendingTasks.clear();
        // 尝试重连
        ctx.channel().eventLoop().schedule(() -> {
            client.reconnect();
        }, 1, TimeUnit.SECONDS);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof RpcResult) {
            RpcResult rpcResult = (RpcResult) msg;
            // 业务逻辑在这里
            Object o = rpcResult.getResult();
            @SuppressWarnings("unchecked")
            RpcFuture<Object> future = (RpcFuture<Object>) pendingTasks.remove(rpcResult.getRequestId());
            future.success(o);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

    }

    public void close() {
        ChannelHandlerContext ctx = context;
        if (ctx != null) {
            ctx.close();
        }
    }

/*    public <T> RpcFuture<T> send(RpcInvocation rpcInvocation) {
        ChannelHandlerContext ctx = context;
        RpcFuture<T> future = new RpcFuture<T>();
        if (ctx != null) {
            ctx.channel().eventLoop().execute(() -> {
                pendingTasks.put(rpcInvocation.getRequestId(), future);
                ctx.writeAndFlush(rpcInvocation);
            });
        } else {
            future.fail(ConnectionClosed);
        }
        return future;
    }*/
}
