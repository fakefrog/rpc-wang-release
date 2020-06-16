package com.wang.starter.rpc.config.client;

import com.wang.starter.rpc.config.annotation.RpcReference;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

/**
 * <p>Package:com.wang.starter.rpc.config.client</p>
 * <p>Description: </p>
 * <p>Company: com.2dfire</p>
 * ConfigurationClassPostProcessor用来注册beandefination
 *
 * @author baiyundou
 * @date 2020/6/16 18:46
 */
public class RpcReferenceRegistry implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        //先试做一个TestService的
        String[] beanDefinitionNames = registry.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanDefinitionName);
            String beanClassName = beanDefinition.getBeanClassName();
            if(StringUtils.isEmpty(beanClassName)){
                continue;
            }
            Class clazz = null;
            try {
                clazz = Class.forName(beanClassName);
            } catch (Exception e) {
                System.out.println(1);
            }
            if (null != clazz && !(FactoryBean.class.isAssignableFrom(clazz))) {
//                if (!(null == clazz || FactoryBean.class.isAssignableFrom(clazz))) {
                //且换或也行
                //factoryBean不做处理
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    RpcReference rpcReference = field.getAnnotation(RpcReference.class);
                    if (null != rpcReference) {
                        Class<?> type = field.getType();
                        String name = field.getName();
                        GenericBeanDefinition newBeanDefinition = new GenericBeanDefinition();
                        //设置bean class对象交给BeanFactory 进行创建，会根据beanclass进行对象的初始化
                        newBeanDefinition.setBeanClass(RpcReferenceFactoryBean.class);
                        //对象初始化赋值操作，给bean 的变量属性的赋值，要有属性的set方法才可以成功的赋值，底层是ArrayList
                        newBeanDefinition.getPropertyValues().addPropertyValue("needProxyInterface", type);
                        newBeanDefinition.getPropertyValues().addPropertyValue("clazz", type);
                        //注册到bean工厂中将bean，这一步完成后Spring就可以初始化bean 对象了；
                        registry.registerBeanDefinition(name, newBeanDefinition);
                    }
                }
            }
        }
    }


}
