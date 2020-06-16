package com.wang.starter.rpc.common.rpc;

import com.alibaba.fastjson.JSON;
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
public class RpcClientEncoder extends MessageToMessageEncoder<RpcInvocation> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcInvocation rpcInvocation, List<Object> out) {
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer();
        String json = JSON.toJSONString(rpcInvocation);
        RpcByteBufUtil.writeStr(buf, json);
        out.add(buf);
    }

}
