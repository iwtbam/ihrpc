package cn.iwtbam.ih.rpc.codec;

import cn.iwtbam.ih.rpc.utils.SerializationUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> genericClass;

    public RpcDecoder(Class<?> cls){
        this.genericClass = cls;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes() < 4){
            return;
        }

        log.debug("decode start");
        int dataLength = in.readInt();
        if(in.readableBytes() < dataLength){
            in.resetReaderIndex();
            return;
        }

        byte[] msg = new byte[dataLength];
        in.readBytes(msg);
        Object obj = SerializationUtils.deserialize(msg, genericClass);
        System.out.println(obj);
        log.debug("decode end");
        out.add(obj);
    }
}
