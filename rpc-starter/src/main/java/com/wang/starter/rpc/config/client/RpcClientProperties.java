package com.wang.starter.rpc.config.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * <p>Package:com.wang.springbootstarter.auto_config</p>
 * <p>Description: </p>
 * <p>Company: com.lowan</p>
 *
 * @author wjj
 * @date 2019/3/1 0:47
 */
@ConfigurationProperties(prefix = "rpc.starter.client")
@Data
public class RpcClientProperties {

    private static final String DEFAULT_IP = "127.0.0.1";

    private static final int DEFAULT_PORT = 8888;

    private static final int DEFAULT_IO_THREADS = 10;

    private static final int DEFAULT_WORKER_THREADS = 16;

//    private static final int DEFAULT_SIDE = 0b00;

    private String ip = DEFAULT_IP;

    private int port = DEFAULT_PORT;

    private int ioThreads = DEFAULT_IO_THREADS;

    private int workerThreads = DEFAULT_WORKER_THREADS;


}
