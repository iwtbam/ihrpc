package cn.iwtbam.ih;

import cn.iwtbam.ih.interfaces.HelloService;
import cn.iwtbam.ih.interfaces.TargetService;
import cn.iwtbam.ih.rpc.annotations.RpcProvider;
import cn.iwtbam.ih.rpc.annotations.RpcScan;
import cn.iwtbam.ih.rpc.proxy.RpcProxy;

@RpcScan(targets = {RpcProvider.class})
public class TestClient {

    public static void main(String[] args){
        HelloService service = RpcProxy.getProxyInstance(HelloService.class);
        System.out.println(service.hello());
        TargetService target = RpcProxy.getProxyInstance(TargetService.class);
        System.out.println(target.add(3, 5));
    }
}
