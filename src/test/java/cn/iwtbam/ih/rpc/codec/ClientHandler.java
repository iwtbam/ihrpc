//package cn.iwtbam.ih.rpc.codec;
//
//import cn.iwtbam.ih.rpc.base.RpcRequest;
//import cn.iwtbam.ih.rpc.base.RpcResponse;
//import cn.iwtbam.ih.rpc.proxy.HelloServer;
//import cn.iwtbam.ih.rpc.serialization.SerializationUtils;
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.Unpooled;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelInboundHandlerAdapter;
//
//import java.lang.reflect.Method;
//import java.util.Objects;
//
//public class ClientHandler extends ChannelInboundHandlerAdapter {
//
//    private  ByteBuf prompt;
//
//    ClientHandler(){
//
//    }
//
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        Class<HelloServer> cls = HelloServer.class;
//
//        RpcRequest rc = new RpcRequest();
//        rc.className = cls.getName();
//        rc.methodName = "hello";
//        Method method = cls.getMethod(rc.methodName);
//        rc.paramterTypes = method.getParameterTypes();
//        rc.parameters = null;
//        System.out.println("send");
//        ctx.write(rc);
//    }
//}
