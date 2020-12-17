package cn.iwtbam.ih.rpc.base;

import lombok.Data;

@Data
public class RpcResponse {
    public String responseId;
    public Throwable error;
    public Object result;
}
