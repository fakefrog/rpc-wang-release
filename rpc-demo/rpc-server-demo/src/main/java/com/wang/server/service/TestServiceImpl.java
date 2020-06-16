package com.wang.server.service;

import com.wang.common.demo.ITestService;
import com.wang.common.demo.domain.EchoRequest;
import com.wang.common.demo.domain.EchoResponse;
import com.wang.starter.rpc.config.annotation.RpcComponent;

/**
 * <p>Package:com.wang.starter.service</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/12 3:00
 */
@RpcComponent(name = "testServiceImpl", requestType = Integer.class)
public class TestServiceImpl implements ITestService {

    private static final String LIVE_FORMAT = "I am %s living in %s";

    @Override
    public String fly(String name) {

        return name + ": I believe I can fly!";

    }

    @Override
    public EchoResponse echo(EchoRequest echoRequest) {
        EchoResponse echoResponse = new EchoResponse();
        echoResponse.setResult(String.format(LIVE_FORMAT, echoRequest.getName(), echoRequest.getAddress()));
        return echoResponse;
    }

    @Override
    public Integer increment(Integer count) {

        return count + 1;
    }


}
