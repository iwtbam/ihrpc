package cn.iwtbam.ih.rpc.codec;

import cn.iwtbam.ih.rpc.utils.SerializationUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> genericClass;

    public RpcEncoder(Class<?> cls){
        this.genericClass = cls;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        log.trace("encode enter");
        if(this.genericClass.isInstance(in)){
            log.trace("encode start");
            byte[] msg = SerializationUtils.serialize(in);
            if(Objects.isNull(msg))
                return;
            out.writeInt(msg.length);
            out.writeBytes(msg);
            log.trace("encode end");
        }
    }
}
