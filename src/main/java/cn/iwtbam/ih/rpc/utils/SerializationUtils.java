package cn.iwtbam.ih.rpc.utils;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class SerializationUtils {

    private static ConcurrentHashMap<Class<?>, Schema<?>> schemas = new ConcurrentHashMap<>();

    private static LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
    private static <T> Schema<T> getSchema(Class<T> cls){

        if(schemas.containsKey(cls))
            return (Schema<T>) schemas.get(cls);

        Schema<T> schema = RuntimeSchema.getSchema(cls);

        if(Objects.nonNull(schema))
            schemas.put(cls, schema);

        return schema;

    }

    public static <T> byte[] serialize(T obj){
        Class<T> cls = (Class<T>) obj.getClass();
        Schema<T> schema = getSchema(cls);

        byte[] ret;
        try{
            ret = ProtostuffIOUtil.toByteArray(obj,schema,buffer);
        }finally {
            buffer.clear();
        }
        return ret;

    }

    public static <T> T deserialize(byte[] data, Class<T> cls){
        Schema<T> schema = getSchema(cls);
        T obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(data, obj, schema);
        return obj;
    }
}
