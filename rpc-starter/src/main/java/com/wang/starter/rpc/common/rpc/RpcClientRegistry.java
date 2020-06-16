package com.wang.starter.rpc.common.rpc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcClientRegistry {

    private Map<Class<?>, Object> registryClasses = new ConcurrentHashMap<>();

    public void register(Class<?> type, Object object) {
        registryClasses.put(type, object);
    }

    public Object get(Class<?> type) {
        return registryClasses.get(type);
    }

}
