package part3.common.serializer.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import part3.common.Message.MessageType;
import part3.common.serializer.Serializer;

import java.util.List;

public class MyDecoder extends ByteToMessageDecoder {
    //负责传入的字节流解码为业务独享，并添加到out中
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //1.读取消息类型
        short messageType = in.readShort();
        // 现在还只支持request与response请求
        if(messageType != MessageType.REQUEST.getCode() &&
                messageType != MessageType.RESPONSE.getCode()){
            System.out.println("暂不支持此种数据");
            return;
        }

        //2.读取序列化的方式&类型
        short serializerType = in.readShort();
        Serializer serializer = Serializer.getSerializerByCode(serializerType);
        if(serializer == null)
            throw new RuntimeException("不存在对应的序列化器");

        //3.读取序列化数组长度
        int length = in.readInt();
        //4.读取序列化数组
        byte[] bytes=new byte[length];
        in.readBytes(bytes);
        Object deserialize = serializer.deserializer(bytes, messageType);
        out.add(deserialize);
    }
}
