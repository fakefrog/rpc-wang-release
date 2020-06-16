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
 * @date 2020/6/13 16:37
 */
public class RpcClientDecoder extends ReplayingDecoder<RpcResult> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        String content = readStr(in);
        RpcResult rpcResult = JSON.parseObject(content, RpcResult.class);
        try {
            Class<?> aClass = Class.forName(rpcResult.getResultType());
            if(aClass.equals(String.class)){
                rpcResult.setResult(rpcResult.getResult().toString());
            }else {
                Object object = JSON.parseObject(rpcResult.getResult().toString(), aClass);
                rpcResult.setResult(object);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        out.add(rpcResult);
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

