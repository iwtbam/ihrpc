package cn.iwtbam.ih.interfaces;

import cn.iwtbam.ih.impl.TargetServiceImpl;
import cn.iwtbam.ih.rpc.annotations.RpcConsumer;
import cn.iwtbam.ih.rpc.annotations.RpcProvider;

@RpcProvider(value = TargetServiceImpl.class)
@RpcConsumer
public interface TargetService {
    public int add(int a, int b);
}
