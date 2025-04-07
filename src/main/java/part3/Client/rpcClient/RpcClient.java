package part3.Client.rpcClient;

import part3.common.Message.RpcRequest;
import part3.common.Message.RpcResponse;

public interface RpcClient {
    RpcResponse sendRequest(RpcRequest rpcRequest);
}
