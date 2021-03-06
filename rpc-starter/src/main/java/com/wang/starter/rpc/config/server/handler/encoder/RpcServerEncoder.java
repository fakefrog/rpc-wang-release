package com.wang.starter.rpc.config.server.handler.encoder;

import com.alibaba.fastjson.JSON;
import com.wang.starter.rpc.common.rpc.RpcResult;
import com.wang.starter.rpc.common.util.RpcByteBufUtil;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * <p>Package:com.wang.starter.rpc.config.common.rpc</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/13 16:31
 */
public class RpcServerEncoder extends MessageToMessageEncoder<RpcResult> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcResult rpcResult, List<Object> out) {
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer();
        rpcResult.setResultType(rpcResult.getResult().getClass().getCanonicalName());
        String json = JSON.toJSONString(rpcResult);
        RpcByteBufUtil.writeStr(buf, json);
        out.add(buf);
    }

}
