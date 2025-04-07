package part3.Server.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import part3.Server.provider.ServiceProvider;
import part3.Server.ratelimit.RateLimit;
import part3.common.Message.RpcRequest;
import part3.common.Message.RpcResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


@AllArgsConstructor
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest>{

    private ServiceProvider serviceProvider;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        RpcResponse response = getResponse(request);
        ctx.writeAndFlush(response);//将消息发送到网络层，不会缓存在本地 flush
        ctx.close();
    }

    private RpcResponse getResponse(RpcRequest rpcRequest) {
        //获取接口名字并获取其对应的服务类
        String interfaceName = rpcRequest.getInterfaceName();
        RateLimit rateLimit = serviceProvider.getRateLimitProvider().getRateLimit(interfaceName);
        if(!rateLimit.getToken()){
            System.out.println("令牌已用完，服务限流");
            return RpcResponse.fail();
        }
        //得到服务端相应服务的实现类
        Object service = serviceProvider.getService(interfaceName);

        Method method = null;
        try {
            method = service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParamsType());

            Object invoke = method.invoke(service, rpcRequest.getParams());
            //封装响应对象并返回
            return RpcResponse.sussess(invoke);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            System.out.println("方法执行错误");
            return RpcResponse.fail();
        }
    }
}
