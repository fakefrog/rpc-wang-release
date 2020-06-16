package com.wang.starter.rpc.common.rpc;

import com.alibaba.fastjson.JSON;
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
 * @date 2020/6/13 15:24
 */
public class RpcServerDecoder extends ReplayingDecoder<RpcInvocation> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        String content = RpcByteBufUtil.readStr(in);
        RpcInvocation rpcInvocation = JSON.parseObject(content, RpcInvocation.class);
        out.add(rpcInvocation);
    }

}
