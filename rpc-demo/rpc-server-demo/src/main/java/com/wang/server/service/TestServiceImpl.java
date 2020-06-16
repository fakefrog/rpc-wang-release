package com.wang.server.service;

import com.wang.common.demo.ITestService;
import com.wang.common.demo.domain.ExpRequest;
import com.wang.common.demo.domain.ExpResponse;
import com.wang.starter.rpc.config.annotation.RpcComponent;

import java.util.List;

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

    @Override
    public String fly(String name) {

        return name + ": I believe I can fly!";

    }

    @Override
    public ExpResponse exp(ExpRequest message) {
        int base = message.getBase();
        int exp = message.getExp();
        long start = System.nanoTime();
        long res = 1;
        for (int i = 0; i < exp; i++) {
            res *= base;
        }
        long cost = System.nanoTime() - start;
        return new ExpResponse(res, cost);
    }

    @Override
    public ExpResponse exp(List<ExpRequest> expRequest) {
        return null;
    }

}
