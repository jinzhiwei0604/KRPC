package part3.Client.netty.nettyInitializer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import part3.Client.rpcClient.RpcClient;
import part3.Client.serviceCenter.ServiceCenter;
import part3.Client.serviceCenter.ZKServiceCenter;
import part3.common.Message.RpcRequest;
import part3.common.Message.RpcResponse;

import java.net.InetSocketAddress;

public class NettyRpcClient implements RpcClient {

    private static final Bootstrap bootstrap;//用于启动客户端的对象，设置与服务器的连接配置
    private static final EventLoopGroup evenLoopGroup; //Netty的线程池，用与IO操作，基于NIO实现 非阻塞IO

    private ServiceCenter serviceCenter;
    public NettyRpcClient() throws InterruptedException {
        this.serviceCenter = new ZKServiceCenter();
    }
    //客户端初始化
    static {
        evenLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(evenLoopGroup).channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer()); //配置netty对消息的处理机制
    }

    @Override
    public RpcResponse sendRequest(RpcRequest rpcRequest) {
        //从注册中心获得host port
        InetSocketAddress address = serviceCenter.serviceDiscovery(rpcRequest.getInterfaceName());
        String host = address.getHostName();
        int port = address.getPort();

        try {
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();

            Channel channel = channelFuture.channel();//channel表示一个连接单位，类似socker

            channel.writeAndFlush(rpcRequest);//发送数据

            channel.closeFuture().sync();//sync 阻塞获取结果，获取结果后连接才会关闭

            //AttributeKey是线程隔离的，没有线程隔离问题，通过设计别名获取channel的内容（在handler中设置）
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("RpcResponse");
            RpcResponse rpcResponse = channel.attr(key).get();

            System.out.println(rpcResponse);
            return rpcResponse;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


}
