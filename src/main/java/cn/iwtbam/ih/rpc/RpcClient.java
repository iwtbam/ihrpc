package cn.iwtbam.ih.rpc;


import cn.iwtbam.ih.rpc.base.RpcRequest;
import cn.iwtbam.ih.rpc.base.RpcResponse;
import cn.iwtbam.ih.rpc.codec.RpcDecoder;
import cn.iwtbam.ih.rpc.codec.RpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.Setter;

public class RpcClient extends SimpleChannelInboundHandler<RpcResponse> {


    private Object syncObj = new Object();
    private RpcResponse response;

    @Getter
    @Setter
    private String serverAddress;

    @Getter
    @Setter
    private int port;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse response) throws Exception {
        synchronized(syncObj){
            this.response = response;
            syncObj.notifyAll();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    public RpcClient(String serverAddress, int port){
        this.serverAddress = serverAddress;
        this.port = port;
    }

    public RpcClient(String connectString){
        String[] strs = connectString.split(":");
        if(strs.length != 2)
            return;
        this.serverAddress = strs[0];
        this.port = Integer.parseInt(strs[1]);
    }


    public RpcResponse send(RpcRequest request) throws Exception {
        EventLoopGroup worker = new NioEventLoopGroup();
        try{
            Bootstrap client = new Bootstrap();
            client.group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new RpcDecoder(RpcResponse.class))
                                    .addLast(RpcClient.this)
                                    .addLast(new RpcEncoder(RpcRequest.class));
                        }
                    });

            ChannelFuture f  = client.connect(this.serverAddress, this.port).sync();
            f.channel().writeAndFlush(request).sync();

            synchronized (syncObj){
                syncObj.wait();
            }

            if(response != null){
                f.channel().close();
            }
            return response;

        }finally {
            worker.shutdownGracefully();
        }

    }
}
