package com.wang.starter.rpc.config.client;

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

    private static ConcurrentHashMap<String, RPCClient> rpcClientMap = new ConcurrentHashMap<>();

    public static synchronized RPCClient addRpcClient(String host, int port) {
        String key = host + "_" + port;
        RPCClient rpcClient = rpcClientMap.computeIfAbsent(key, k -> {
            RPCClient client = new RPCClient(host, port);
            try {
                RPCClient clientTmp = client;
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    try {
                        clientTmp.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return client;
        });
        return rpcClient;
    }

}
