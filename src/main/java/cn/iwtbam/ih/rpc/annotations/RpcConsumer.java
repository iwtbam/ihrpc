package cn.iwtbam.ih.rpc.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@RpcComponent
@Inherited
public @interface RpcConsumer {
}
