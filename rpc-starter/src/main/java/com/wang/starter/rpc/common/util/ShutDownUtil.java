package com.wang.starter.rpc.common.util;

/**
 * <p>Package:com.wang.starter.rpc.common.util</p>
 * <p>Description: </p>
 * <p>Company: com.2dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/19 11:11
 */
public class ShutDownUtil {

    public static void addShutHook(Runnable r){
        Runtime.getRuntime().addShutdownHook(new Thread(r));
    }

}
