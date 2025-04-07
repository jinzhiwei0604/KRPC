package part3.Server.server;

public interface RpcServer {

    //开始监听
    void start(int port);

    void stop();
}
