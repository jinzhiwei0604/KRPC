package part3.Server;


import part3.Server.provider.ServiceProvider;
import part3.Server.server.RpcServer;
import part3.Server.server.impl.NettyRpcServer;
import part3.common.UserService.UserService;
import part3.common.UserServiceImpl.UserServiceImpl;

/**
 * @author wxx
 * @version 1.0
 * @create 2024/2/11 19:39
 */
public class TestServer {
    public static void main(String[] args) {
        UserService userService=new UserServiceImpl();

        ServiceProvider serviceProvider=new ServiceProvider("127.0.0.1",9999);
        serviceProvider.provideServiceInterface(userService,true); //注册服务并创建

        RpcServer rpcServer=new NettyRpcServer(serviceProvider);
        rpcServer.start(9999);
    }
}