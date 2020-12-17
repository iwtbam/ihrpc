package cn.iwtbam.ih.rpc.proxy;

import cn.iwtbam.ih.rpc.RpcClient;
import cn.iwtbam.ih.rpc.base.RpcRequest;
import cn.iwtbam.ih.rpc.base.RpcResponse;
import cn.iwtbam.ih.rpc.manager.ServiceDiscovery;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

@Slf4j
public class RpcProxy implements InvocationHandler {


    private String serverAddress;
    private int port;

    private Class<?> serviceInterface;

    public RpcProxy(Class<?> serviceInterface){
        this.serviceInterface = serviceInterface;
    }

    public  static <T> T getProxyInstance(Class<T> serviceInterface){

        return (T)Proxy.newProxyInstance(
                serviceInterface.getClassLoader(),
                new Class<?>[] {serviceInterface},
                new RpcProxy(serviceInterface));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.className = serviceInterface.getName();
        request.methodName = method.getName();
        request.parameters = args;
        request.paramterTypes = method.getParameterTypes();
        String nodeName = request.className;
        String connectString = ServiceDiscovery.getInstance().getServiceAddress(nodeName);
        System.out.println(connectString);
        log.debug("connect", connectString);
        if(connectString == null)
            return null;
        RpcClient client = new RpcClient(connectString);
        RpcResponse response = client.send(request);

        log.debug("response", response);
        if(response.error != null)
            throw response.error;;
        return response.result;

    }
}
