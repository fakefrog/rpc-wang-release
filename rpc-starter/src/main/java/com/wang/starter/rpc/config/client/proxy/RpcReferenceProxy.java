package com.wang.starter.rpc.config.client.proxy;

import com.wang.starter.rpc.common.rpc.RpcClientRegistry;
import com.wang.starter.rpc.common.rpc.RpcInvocation;
import com.wang.starter.rpc.config.client.RpcClient;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Package:com.wang.starter.rpc.config.client</p>
 * <p>Description: </p>
 * <p>Company: com.2dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/16 18:53
 */
@Slf4j
public class RpcReferenceProxy implements InvocationHandler, Serializable {

    private static final long serialVersionUID = -4467164789570764661L;

    private Class<?> interfaceClazz;

    @SuppressWarnings("unchecked")
    public <T> T newProxy(Class<T> myInterfaces) {
        ClassLoader classLoader = myInterfaces.getClassLoader();
        Class<?>[] interfaces = new Class[]{myInterfaces};
        //JdkProxy proxy =new JdkProxy();
        interfaceClazz = myInterfaces;
        return (T) Proxy.newProxyInstance(classLoader, interfaces, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        if(method.getDeclaringClass().equals(Object.class)){
            try {
                return method.invoke(interfaceClazz);
            } catch (Exception e) {
                log.error("找不到方法");
            }
        }
        RpcClient rpcClient = RpcClientRegistry.addRpcClient("localhost", 8888);
        RpcInvocation rpcInvocation = new RpcInvocation();
        try {
            rpcInvocation.setMethod(method.getName());
            rpcInvocation.setInterfaceName(interfaceClazz.getCanonicalName());
            Class<?>[] parameterTypes = method.getParameterTypes();
            rpcInvocation.setArgs(args);
            String[] argTypes = new String[parameterTypes.length];
            for (int i = 0; i < argTypes.length; i++) {
                argTypes[i] = parameterTypes[i].getCanonicalName();
            }
            rpcInvocation.setArgsType(argTypes);
        } catch (Exception e) {
            log.error("error,", e);
        }
        return rpcClient.send(rpcInvocation);
    }

}
