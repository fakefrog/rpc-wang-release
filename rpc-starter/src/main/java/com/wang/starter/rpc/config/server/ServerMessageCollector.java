package com.wang.starter.rpc.config.server;

import com.alibaba.fastjson.JSON;
import com.wang.starter.rpc.common.rpc.RpcInvocation;
import com.wang.starter.rpc.common.rpc.RpcResult;
import com.wang.starter.rpc.common.rpc.RpcServerRegistry;
import com.wang.starter.rpc.common.rpc.ServerInterfaceInfo;
import com.wang.starter.rpc.common.util.CtxUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UnknownFormatConversionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Sharable
@Slf4j
public class ServerMessageCollector extends ChannelInboundHandlerAdapter {

    private ThreadPoolExecutor executor;

    /**
     * 可以做安全控制
     */
    private RpcServerRegistry rpcRegistry = new RpcServerRegistry();

    public ServerMessageCollector(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

    void registerBean(Object bean) {
        Class<?>[] interfaces = bean.getClass().getInterfaces();
        //注册接口名
        for (Class<?> anInterface : interfaces) {
            rpcRegistry.register(anInterface, bean);
        }
    }

    void closeGracefully() {
        this.executor.shutdown();
        try {
            this.executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {

        }
        this.executor.shutdownNow();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.debug("connection comes");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.debug("connection leaves");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof RpcInvocation) {
            this.executor.execute(() -> this.handleRpcMessage(ctx, (RpcInvocation) msg));
        }
    }

    private void handleRpcMessage(ChannelHandlerContext ctx, RpcInvocation rpcInvocation) {
        // 业务逻辑在这里
        ServerInterfaceInfo serverInterfaceInfo = rpcRegistry.get(rpcInvocation.getInterfaceName());
        if (serverInterfaceInfo == null) {
            CtxUtil.closeCtx(ctx, "无对应接口");
            return;
        }
        try {
            Object[] args = rpcInvocation.getArgs();
            Object[] realArgs = rpcInvocation.getArgs();
            Class[] params = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                params[i] = Class.forName(rpcInvocation.getArgsType()[i]);
                if(params[i].equals(String.class)){
                    realArgs[i] = args[i].toString();
                }else {
                    realArgs[i] = JSON.parseObject(args[i].toString(), params[i]);
                }
            }
            Method suitableMethod = serverInterfaceInfo.getSuitableMethod(rpcInvocation);
            RpcResult rpcResult = new RpcResult();
            if(null != suitableMethod){
                Object result = serverInterfaceInfo.getSuitableMethod(rpcInvocation).invoke(serverInterfaceInfo.getTargetObject(), realArgs);
                rpcResult.setResult(result);
                rpcResult.setRequestId(rpcInvocation.getRequestId());
                rpcResult.setAttachments(rpcInvocation.getAttachments());
                ctx.writeAndFlush(rpcResult);
            }else{
                CtxUtil.closeCtx(ctx, "找不到对应的方法");
            }
        } catch (ClassNotFoundException e) {
            CtxUtil.closeCtx(ctx, "找不到对应的类", e);
        } catch (IllegalAccessException | InvocationTargetException e) {
            CtxUtil.closeCtx(ctx, "调用出错", e);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.warn("connection error", cause);
    }

}
