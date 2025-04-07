package part3.common.Message;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MessageType {

    REQUEST(0),//代表消息请求
    RESPONSE(1);//代表消息响应
    private int code;
    
    //提供对code的访问
    public int getCode() {
        return code;
    }


}
