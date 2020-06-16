package com.wang.starter.rpc.config.server;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>Package:com.wang.starter.rpc</p>
 * <p>Description: </p>
 * <p>Company: com.dfire</p>
 *
 * @author baiyundou
 * @date 2020/6/12 3:20
 */
public class RpcThreadFactory implements ThreadFactory {

    private AtomicInteger seq = new AtomicInteger();

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setName("starter-" + seq.getAndIncrement());
        return t;
    }

}
