package com.wang.starter.rpc.config.client;

import org.springframework.beans.factory.FactoryBean;

import lombok.Data;

/**
 * <p>Package:com.wang.starter.rpc.config.client</p>
 * <p>Description: </p>
 * <p>Company: com.2dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/16 18:43
 */
@Data
public class RpcReferenceFactoryBean implements FactoryBean {

    private Class needProxyInterface;

    private Class clazz;

    @Override
    public Object getObject() throws Exception {
        return getProxy().newProxy(needProxyInterface);
    }

    @Override
    public Class<?> getObjectType() {
        return clazz;
    }

    RpcReferenceProxy getProxy(){
        return new RpcReferenceProxy();
    }

}
