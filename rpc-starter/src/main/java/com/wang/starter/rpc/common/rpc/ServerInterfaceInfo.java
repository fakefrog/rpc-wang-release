package com.wang.starter.rpc.common.rpc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Data;

/**
 * <p>Package:com.wang.starter.rpc.common.rpc</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/14 3:14
 */
@Data
public class ServerInterfaceInfo {

    private Class interfaceClass;

    private String interfaceName;

    private Object targetObject;

    private ConcurrentHashMap<String, Method> methodMap;

    public Method getSuitableMethod(RpcInvocation rpcInvocation) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(RpcConstants.METHOD_SIGN_FORMAT, rpcInvocation.getMethod()));
        String[] argsType = rpcInvocation.getArgsType();
        for (String type : argsType) {
            sb.append(type);
            sb.append(RpcConstants.SEPARATOR);
        }
        return methodMap.get(sb.toString());
    }
}
