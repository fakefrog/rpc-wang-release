package com.wang.common.demo;

import com.wang.common.demo.domain.ExpRequest;
import com.wang.common.demo.domain.ExpResponse;

import java.util.List;

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

    ExpResponse exp(ExpRequest expRequest);

    ExpResponse exp(List<ExpRequest> expRequest);


}
