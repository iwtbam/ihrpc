package cn.iwtbam.ih.interfaces;

import cn.iwtbam.ih.impl.HelloServiceImpl;
import cn.iwtbam.ih.rpc.annotations.RpcConsumer;
import cn.iwtbam.ih.rpc.annotations.RpcProvider;

@RpcProvider(value = HelloServiceImpl.class)
@RpcConsumer
public interface HelloService {
    public String hello();
}
