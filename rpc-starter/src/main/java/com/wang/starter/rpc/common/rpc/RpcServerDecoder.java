package com.wang.starter.rpc.common.rpc;

import com.alibaba.fastjson.JSON;
import com.wang.starter.rpc.common.Charsets;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
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
//        String requestId = readStr(in);
//        String type = readStr(in);
//        String content = readStr(in);
//        out.add(new MessageInput(type, rquestId, content));
        String content = readStr(in);
        RpcInvocation rpcInvocation = JSON.parseObject(content, RpcInvocation.class);
//        RpcInvocation rpcInvocation = JSON.parseObject(content, RpcInvocation.class);
        out.add(rpcInvocation);
    }

    private String readStr(ByteBuf in) {
        int len = in.readInt();
        if (len < 0 || len > (1 << 20)) {
            throw new DecoderException("string too long len=" + len);
        }
        byte[] bytes = new byte[len];
        in.readBytes(bytes);
        return new String(bytes, Charsets.UTF8);
    }

}
