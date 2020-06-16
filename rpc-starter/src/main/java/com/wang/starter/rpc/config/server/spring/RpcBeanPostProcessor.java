package com.wang.starter.rpc.config.server.spring;

import com.wang.starter.rpc.config.annotation.RpcComponent;
import com.wang.starter.rpc.config.server.handler.ServerMessageHandler;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;

import javax.annotation.Resource;

/**
 * <p>Package:com.wang.starter.starter.starter</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/12 3:08
 */
public class RpcBeanPostProcessor implements BeanPostProcessor {

    @Resource
    private ServerMessageHandler serverMessageHandler;

    public RpcBeanPostProcessor(ServerMessageHandler serverMessageHandler) {
        this.serverMessageHandler = serverMessageHandler;
    }

    public RpcBeanPostProcessor() {
    }

    @Nullable
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz;
//        if(isProxyBean()){
        clazz = AopUtils.getTargetClass(bean);
//        }
        RpcComponent annotation = clazz.getAnnotation(RpcComponent.class);
        if (annotation != null) {
            serverMessageHandler.registerBean(bean);
        }
        return bean;
    }

}
