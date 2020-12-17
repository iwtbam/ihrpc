package cn.iwtbam.ih.rpc.annotations;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@RpcComponent
@Inherited
public @interface RpcProvider {
    Class<?> value();
}
