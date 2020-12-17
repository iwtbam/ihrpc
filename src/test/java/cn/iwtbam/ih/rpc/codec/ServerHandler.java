package cn.iwtbam.ih.rpc.codec;

import cn.iwtbam.ih.rpc.base.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {
        System.out.println("rpc request");
        System.out.println(rpcRequest.className);
        System.out.println(rpcRequest.methodName);
    }
}
