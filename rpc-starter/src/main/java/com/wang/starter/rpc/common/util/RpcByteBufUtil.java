package com.wang.starter.rpc.common.util;

import com.wang.starter.rpc.common.Charsets;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;

/**
 * <p>Package:com.wang.starter.rpc.common.util</p>
 * <p>Description: </p>
 * <p>Company: com.2dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/17 2:42
 */
public class RpcByteBufUtil {

    public static String readStr(ByteBuf in) {
        int len = in.readInt();
        if (len < 0 || len > (1 << 20)) {
            throw new DecoderException("string too long len=" + len);
        }
        byte[] bytes = new byte[len];
        in.readBytes(bytes);
        return new String(bytes, Charsets.UTF8);
    }

    public static void writeStr(ByteBuf buf, String s) {
        byte[] bytes = s.getBytes(Charsets.UTF8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }
    
}
