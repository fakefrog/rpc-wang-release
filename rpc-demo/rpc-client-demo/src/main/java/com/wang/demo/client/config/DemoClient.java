package com.wang.demo.client.config;


import com.wang.common.demo.ITestService;
import com.wang.common.demo.domain.ExpRequest;
import com.wang.common.demo.domain.ExpResponse;
import com.wang.starter.rpc.config.client.RPCClient;
import com.wang.starter.rpc.common.rpc.RpcInvocation;

import java.lang.reflect.Method;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DemoClient {

    private RPCClient client;

    public DemoClient(RPCClient client) {
        this.client = client;
        this.client.rpc("fib_res", Long.class).rpc("exp_res", ExpResponse.class);
    }

/*    public ExpResponse sendInvocation() {
        RpcInvocation rpcInvocation = new RpcInvocation();
        try {
            rpcInvocation.setMethod("exp");
            rpcInvocation.setInterfaceName(ITestService.class.getCanonicalName());
            ExpRequest expRequest = new ExpRequest(2, 1);
            Object[] objects = new Object[1];
            objects[0] = expRequest;
            rpcInvocation.setArgs(objects);
            String[] args = new String[1];
            args[0] = ExpRequest.class.getCanonicalName();
            rpcInvocation.setArgsType(args);
        } catch (Exception e) {
            log.error("error,", e);
        }
        return (ExpResponse) client.send(rpcInvocation);
    }*/

    public static void main(String[] args) throws InterruptedException {
        RPCClient client = new RPCClient("localhost", 8888);
        DemoClient demo = new DemoClient(client);
//        ExpResponse expResponse = demo.sendInvocation();
//        System.out.println(expResponse);
        client.close();
    }

}
