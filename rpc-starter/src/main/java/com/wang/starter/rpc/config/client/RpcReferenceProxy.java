package com.wang.starter.rpc.config.client;

import com.wang.common.demo.ITestService;
import com.wang.common.demo.domain.ExpRequest;
import com.wang.common.demo.domain.ExpResponse;
import com.wang.starter.rpc.common.rpc.RpcInvocation;

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

    private Class interfaceClazz;

    @SuppressWarnings("unchecked")
    public <T> T newProxy(Class<T> myInterfaces) {
        ClassLoader classLoader = myInterfaces.getClassLoader();
        Class<?>[] interfaces = new Class[]{myInterfaces};
        //JdkProxy proxy =new JdkProxy();
        interfaceClazz = myInterfaces;
        return (T) Proxy.newProxyInstance(classLoader, interfaces, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("toString")) {
            return interfaceClazz.toString();
        }
        RPCClient rpcClient = RpcClientRegistry.addRpcClient("localhost", 8888);
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
        Object result = rpcClient.send(rpcInvocation);
        return result;
    }

}
