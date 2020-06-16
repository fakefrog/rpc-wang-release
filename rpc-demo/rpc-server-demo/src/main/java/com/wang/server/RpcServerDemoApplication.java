package com.wang.server;

import com.wang.starter.rpc.config.annotation.EnableRpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRpc
public class RpcServerDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcServerDemoApplication.class, args);
    }

}
