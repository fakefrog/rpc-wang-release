package com.wang.starter.rpc.common.rpc;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcServerRegistry {

    /**
     * 接口名和bean的映射
     */
    private Map<String, ServerInterfaceInfo> registryClasses = new ConcurrentHashMap<>();

    public void register(Class<?> type, Object object) {
        ServerInterfaceInfo serverInterfaceInfo = new ServerInterfaceInfo();
        ServerInterfaceInfo old = registryClasses.putIfAbsent(type.getCanonicalName(), serverInterfaceInfo);
        if (null != old) {
            log.error("多次注入同接口的实现类");
        } else {
            serverInterfaceInfo.setInterfaceClass(type);
            serverInterfaceInfo.setTargetObject(object);
            serverInterfaceInfo.setInterfaceName(type.getCanonicalName());
            serverInterfaceInfo.setMethodMap(new ConcurrentHashMap<>());
            Method[] declaredMethods = type.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                declaredMethod.setAccessible(true);
                Parameter[] parameters = declaredMethod.getParameters();
                StringBuilder sb = new StringBuilder();
                sb.append(String.format(RpcConstants.METHOD_SIGN_FORMAT, declaredMethod.getName()));
                for (Parameter parameter : parameters) {
                    sb.append(parameter.getType().getCanonicalName());
                    sb.append(RpcConstants.SEPARATOR);
                }
                serverInterfaceInfo.getMethodMap().put(sb.toString(), declaredMethod);
            }
        }
    }

    public ServerInterfaceInfo get(String type) {
        ServerInterfaceInfo serverInterfaceInfo = registryClasses.get(type);
        //暴露完毕才能使用
        return serverInterfaceInfo;
    }

}
