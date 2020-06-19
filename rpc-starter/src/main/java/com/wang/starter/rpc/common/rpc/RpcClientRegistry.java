package com.wang.starter.rpc.common.rpc;

import com.wang.starter.rpc.common.util.ShutDownUtil;
import com.wang.starter.rpc.config.client.RpcClient;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Package:com.wang.starter.rpc.config.client</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/16 22:45
 */
public class RpcClientRegistry {

    private static ConcurrentHashMap<String, RpcClient> rpcClientMap = new ConcurrentHashMap<>();

    public static synchronized RpcClient addRpcClient(String host, int port) {
        String key = host + "_" + port;
        RpcClient rpcClient = rpcClientMap.computeIfAbsent(key, k -> {
            RpcClient client = new RpcClient(host, port);
            try {
                RpcClient clientTmp = client;
                ShutDownUtil.addShutHook(() -> {
                    try {
                        clientTmp.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
            }
            return client;
        });
        return rpcClient;
    }

}
