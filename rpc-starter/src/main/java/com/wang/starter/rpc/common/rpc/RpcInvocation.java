package com.wang.starter.rpc.common.rpc;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * <p>Package:com.wang.starter.rpc.config.common.rpc</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/13 2:55
 */
@Data
public class RpcInvocation {

    private Map<String, Object> attachments = new HashMap<>();

    private String interfaceName;

    private String method;

    private Object[] args;

    private String[] argsType;

    private String requestId;

}
