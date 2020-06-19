package com.wang.demo.client.controller;


import com.wang.common.demo.ITestService;
import com.wang.common.demo.domain.EchoRequest;
import com.wang.common.demo.domain.EchoResponse;
import com.wang.starter.rpc.config.annotation.RpcReference;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Package:com.wang.demo.client</p>
 * <p>Description: </p>
 * <p>Company: com.2dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/12 10:19
 */
@RestController
public class TestController {

    @RpcReference
    private ITestService testService;

    @RequestMapping("/fly")
    public String fly() {
        String s = testService.toString();
        return testService.fly("james");
    }

    @RequestMapping("/echo/{name}/{address}")
    public EchoResponse echo(@PathVariable String name, @PathVariable String address) {
        EchoRequest echoRequest = new EchoRequest();
        echoRequest.setName(name);
        echoRequest.setAddress(address);
        return testService.echo(echoRequest);
    }

    @RequestMapping("/increment/{count}")
    public Integer echo(@PathVariable Integer count) {
        return testService.increment(count);
    }

}
