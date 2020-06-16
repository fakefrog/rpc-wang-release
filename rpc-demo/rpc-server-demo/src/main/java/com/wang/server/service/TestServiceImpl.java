package com.wang.server.service;

import com.alibaba.fastjson.JSON;
import com.wang.common.demo.ITestService;
import com.wang.common.demo.domain.ExpRequest;
import com.wang.common.demo.domain.ExpResponse;
import com.wang.starter.rpc.config.annotation.RpcComponent;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.synth.SynthSpinnerUI;

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


    public static void main(String[] args) {
        ArrayList<ExpRequest> ins = new ArrayList<>();
        ins.add(new ExpRequest(1,2));
        ins.add(new ExpRequest(2,3));
        ins.add(new ExpRequest(3,4));
        String s = JSON.toJSONString(ins);
        Method[] declaredMethods = ITestService.class.getDeclaredMethods();
        int i = 0;
        for (Method declaredMethod : declaredMethods) {
            i++;
            Parameter[] parameters = declaredMethod.getParameters();
            for (Parameter parameter : parameters) {
                System.out.println(parameter.getName());
                if(i == 2){
                    Type parameterizedType = parameter.getParameterizedType();
                    Class<?> type = parameter.getType();
                    Object object = JSON.parseObject(s, type);
                    System.out.println(object);
                    System.out.println(1);
                }
            }
        }
    }


}
