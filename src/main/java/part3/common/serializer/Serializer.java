package part3.common.serializer;


//通过一个静态工程方法根据类型代码返回具体的序列化实例
public interface Serializer {

    byte[] serialize(Object obj); //将对象序列化为字节数组,在网络通信中，数据只能以字节流形式进行传输

    Object deserializer(byte[] bytes,int messageType);    //将数组反序列化为对象

    int getType();// 返回使用的序列器，是哪个
    // 0：java自带序列化方式, 1: json序列化方式
    // 根据序号取出序列化器，暂时有两种实现方式，需要其它方式，实现这个接口即可
    static Serializer getSerializerByCode(int code){
        switch (code){
            case 0:
                return new ObjectSerializer();
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }
}
