package com.wang.starter.rpc.common.util;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Package:com.wang.starter.rpc.common.util</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/14 3:00
 */
@Slf4j
public class CtxUtil {

    public static void closeCtx(ChannelHandlerContext channelHandlerContext, String reason, Exception e) {
        channelHandlerContext.close();
        log.error("channelHandlerContext closed,{}", reason, e);
    }

    public static void closeCtx(ChannelHandlerContext channelHandlerContext, String reason) {
        channelHandlerContext.close();
        log.info("channelHandlerContext closed,{}", reason);
    }

}
