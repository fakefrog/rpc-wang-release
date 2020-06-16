package com.wang.common.demo;

import com.wang.common.demo.domain.EchoRequest;
import com.wang.common.demo.domain.EchoResponse;

/**
 * <p>Package:com.wang.starter.service</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/12 2:44
 */
public interface ITestService {

    String fly(String name);

    EchoResponse echo(EchoRequest echoRequest);

}
