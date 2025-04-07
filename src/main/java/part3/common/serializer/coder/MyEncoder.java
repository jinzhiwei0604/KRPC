package part3.common.serializer.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import part3.common.Message.MessageType;
import part3.common.Message.RpcRequest;
import part3.common.Message.RpcResponse;
import part3.common.serializer.Serializer;

public class MyEncoder extends MessageToByteEncoder {
    //MessageToByteEncoder是netty中涉及用来实现编码器的抽象类
    private Serializer serializer;
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        //ctx代表管道上下文，包含通道和处理器，msg为要编码的消息对象，out为netty提供给的字节缓冲区，编码后的字节数据写入其中
        System.out.println(msg.getClass());//获取消息类型
        if (msg instanceof RpcRequest) {
            out.writeShort(MessageType.REQUEST.getCode());
        } else if (msg instanceof RpcResponse) {
            out.writeShort(MessageType.RESPONSE.getCode());
        }

        out.writeShort(serializer.getType());//写入当前序列化器的类型
        byte[] serializeBytes = serializer.serialize(msg); //转化为字符数组
        out.writeInt(serializeBytes.length);
        out.writeBytes(serializeBytes); //将字节数据写入缓冲区中
    }
}
