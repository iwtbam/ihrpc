package cn.iwtbam.ih.rpc;

import cn.iwtbam.ih.TestClient;
import cn.iwtbam.ih.rpc.annotations.RpcProvider;
import cn.iwtbam.ih.rpc.annotations.RpcScan;
import cn.iwtbam.ih.rpc.base.RpcRequest;
import cn.iwtbam.ih.rpc.base.RpcResponse;
import cn.iwtbam.ih.rpc.codec.RpcDecoder;
import cn.iwtbam.ih.rpc.codec.RpcEncoder;
import cn.iwtbam.ih.rpc.manager.ServiceRegister;
import cn.iwtbam.ih.rpc.utils.IocManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

@ChannelHandler.Sharable
@Slf4j
public class RpcServer extends SimpleChannelInboundHandler<RpcRequest> {

    private ServiceRegister register;
    private String serviceAddress = "0.0.0.0";
    private Map<String, Class<?>> interfaces = null;
    private Map<String, Object> providers = null;
    private int port;

    public RpcServer(int port, Class<?> config){
        this.port = port;

        RpcScan rs = config.getAnnotation(RpcScan.class);
        if(rs == null)
            return;

        try {
            this.register = new ServiceRegister();
            this.serviceAddress = InetAddress.getLocalHost().getHostAddress();
            String packageName = config.getPackageName();
            String path = config.getResource("").getPath();
            interfaces = IocManager.loadClasses(path, packageName, RpcProvider.class);
            providers = getProviders(interfaces);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() throws  Exception{

        for(String seviceName : providers.keySet()){
            try {
                register.registe(seviceName, serviceAddress, port);
                log.debug("registe", "serviceName", serviceAddress + ":" + String.valueOf(port));
            }catch (Exception e){
                e.printStackTrace();
                log.debug("registe", e);
            }
        }
        listen();
    }

    private void listen() throws  Exception{
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try{

            ServerBootstrap server = new ServerBootstrap();
            server.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new RpcDecoder(RpcRequest.class));
                            socketChannel.pipeline().addLast(new RpcEncoder(RpcResponse.class));
                            socketChannel.pipeline().addLast(RpcServer.this);


                        }
                    });

            ChannelFuture f = server.bind(port).sync();
            f.channel().closeFuture().sync();

        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

        return;
    }



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        String className = request.getClassName();
        String methodName = request.getMethodName();

        RpcResponse response = new RpcResponse();
        response.setResponseId(request.getRequestId());


        if(providers.containsKey(className)){
            Object provider = providers.get(className);
            Class<?> providerClass = interfaces.get(className);
            Method method = providerClass.getMethod(methodName, request.getParamterTypes());
            try {
                response.result = method.invoke(provider, request.parameters);
            }catch (Exception e){
                response.setError(e);
            }
        }else{
            response.setError(new RuntimeException("service : " + className + " is not exists!"));
        }
        System.out.println(response);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }


    private static Map<String, Object> getProviders(Map<String, Class<?>> interfaces) throws Exception{
        Map<String, Object> instances = new HashMap<>();
        for(Map.Entry<String, Class<?>> entry : interfaces.entrySet()){
            RpcProvider rc = entry.getValue().getAnnotation(RpcProvider.class);
            if(rc != null){
                instances.put(entry.getKey(), rc.value().newInstance());
            }
        }
        return instances;
    }

    public static void main(String[] args) throws Exception{
        new RpcServer(7777, TestClient.class).run();

    }
}

