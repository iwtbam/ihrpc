package cn.iwtbam.ih.impl;

import cn.iwtbam.ih.interfaces.HelloService;
import cn.iwtbam.ih.rpc.annotations.RpcProvider;


public class HelloServiceImpl implements HelloService {

    public HelloServiceImpl(){

    }

    @Override
    public String hello() {
        return "hello";
    }
}
