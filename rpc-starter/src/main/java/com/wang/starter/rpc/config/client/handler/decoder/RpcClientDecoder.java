package com.wang.starter.rpc.config.client.handler.decoder;

import com.alibaba.fastjson.JSON;
import com.wang.starter.rpc.common.rpc.RpcResult;
import com.wang.starter.rpc.common.util.RpcByteBufUtil;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

/**
 * <p>Package:com.wang.starter.rpc.config.common.rpc</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/13 16:37
 */
public class RpcClientDecoder extends ReplayingDecoder<RpcResult> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        String content = RpcByteBufUtil.readStr(in);
        RpcResult rpcResult = JSON.parseObject(content, RpcResult.class);
        try {
            Class<?> aClass = Class.forName(rpcResult.getResultType());
            if (aClass.equals(String.class)) {
                rpcResult.setResult(rpcResult.getResult().toString());
            } else {
                Object object = JSON.parseObject(rpcResult.getResult().toString(), aClass);
                rpcResult.setResult(object);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        out.add(rpcResult);
    }


}

