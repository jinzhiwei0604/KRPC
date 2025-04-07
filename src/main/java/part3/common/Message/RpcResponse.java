package part3.common.Message;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RpcResponse {
    //状态
    private int code;
    private String message;
    //数据
    private Object data;
    private Class<?> DataType;
    public static RpcResponse sussess(Object data) {
        return RpcResponse.builder().code(500).message("服务器发生错误").build();
    }

    public static RpcResponse fail() {
        return RpcResponse.builder().code(500).message("服务器发生错误").build();
    }
}
