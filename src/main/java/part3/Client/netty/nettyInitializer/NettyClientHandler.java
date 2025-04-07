package part3.Client.netty.nettyInitializer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.AllArgsConstructor;
import part3.common.Message.RpcResponse;

@AllArgsConstructor
//Netty用于处理服务器响应的处理器
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse rpcResponse) throws Exception {

        AttributeKey<RpcResponse> key = AttributeKey.valueOf("RpcResponse");//给channel设置别名，将 RpcResponse绑定到channel属性中
        ctx.attr(key).set(rpcResponse);

        ctx.channel().close();//关闭当前channel
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) throws  Exception {

        cause.printStackTrace();
        ctx.close();
    }
}
