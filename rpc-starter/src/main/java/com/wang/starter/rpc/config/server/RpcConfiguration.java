package com.wang.starter.rpc.config.server;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>Package:com.wang.starter.starter.starter</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/12 0:07
 */
@Configuration
@EnableConfigurationProperties(RpcServerProperties.class)
public class RpcConfiguration {

    @Autowired
    private RpcServerProperties mRpcServerProperties;

    @Bean
    @ConditionalOnProperty(prefix = "rpc.starter", name = "provider", havingValue = "true")
    public RPCServer rpcServer() {
        RPCServer rpcServer = new RPCServer(mRpcServerProperties.getIp(), mRpcServerProperties.getPort(),
                mRpcServerProperties.getIoThreads(), messageCollector());
        rpcServer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                rpcServer.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        return rpcServer;
    }

    /*    @Bean
        public RpcBeanPostProcessor rpcBeanPostProcessor() {
            return new RpcBeanPostProcessor(messageCollector());
        }*/
    @Bean
    @ConditionalOnProperty(prefix = "rpc.starter", name = "provider", havingValue = "true")
    public RpcBeanPostProcessor rpcBeanPostProcessor() {
        return new RpcBeanPostProcessor();
    }

    @Bean
    @ConditionalOnProperty(prefix = "rpc.starter", name = "provider", havingValue = "true")
    public ServerMessageCollector messageCollector() {
        return new ServerMessageCollector(rpcThreadPoolExecutor());
    }

    @Bean
    @ConditionalOnProperty(prefix = "rpc.starter", name = "provider", havingValue = "true")
    public ThreadPoolExecutor rpcThreadPoolExecutor() {
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1000);
        return new ThreadPoolExecutor(1, mRpcServerProperties.getWorkerThreads(), 30, TimeUnit.SECONDS, queue, new RpcThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

}
