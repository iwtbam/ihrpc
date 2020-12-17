//package cn.iwtbam.ih.rpc.codec;
//
//import cn.iwtbam.ih.rpc.base.RpcRequest;
//import cn.iwtbam.ih.rpc.base.RpcResponse;
//import cn.iwtbam.ih.rpc.proxy.HelloServer;
//import io.netty.bootstrap.Bootstrap;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelOption;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioSocketChannel;
//
//import java.lang.reflect.Method;
//
//public class Client {
//
//    public static void main(String[] args) throws Exception{
//        EventLoopGroup worker = new NioEventLoopGroup();
//        try {
//            Bootstrap b = new Bootstrap();
//            b.group(worker)
//                    .channel(NioSocketChannel.class)
//                    .option(ChannelOption.TCP_NODELAY, true)
//                    .handler(new ChannelInitializer<SocketChannel>(){
//                        @Override
//                        protected void initChannel(SocketChannel socketChannel) throws Exception {
//                            socketChannel.pipeline()
//                                .addLast(new ClientHandler())
//                                .addLast(new RpcEncoder(RpcRequest.class));
//                        }
//                    });
//            ChannelFuture f  = b.connect("0.0.0.0", 7777).sync();
//            f.channel().closeFuture().sync();
////            f.channel().writeAndFlush(getRequest()).sync();
//        }finally {
//            worker.shutdownGracefully();
//        }
//    }
//
//    static RpcRequest getRequest() throws Exception{
//        Class<HelloServer> cls = HelloServer.class;
//        RpcRequest rc = new RpcRequest();
//        rc.className = cls.getName();
//        rc.methodName = "hello";
//        Method method = cls.getMethod(rc.methodName);
//        rc.paramterTypes = method.getParameterTypes();
//        rc.parameters = null;
//
//        return rc;
//    }
//}
