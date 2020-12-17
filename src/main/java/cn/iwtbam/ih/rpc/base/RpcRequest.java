package cn.iwtbam.ih.rpc.base;

import lombok.Data;

@Data
public class RpcRequest {
    public String requestId;
    public String className;
    public String methodName;
    public Class<?>[] paramterTypes;
    public Object[] parameters;
}
