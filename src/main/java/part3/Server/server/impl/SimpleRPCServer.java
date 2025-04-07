package part3.Server.server.impl;

import lombok.AllArgsConstructor;
import part3.Server.provider.ServiceProvider;
import part3.Server.server.RpcServer;
import part3.Server.server.work.WorkThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@AllArgsConstructor
public class SimpleRPCServer implements RpcServer {

    private ServiceProvider serviceProvider;
    @Override
    public void start(int port) {
        try {
           ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("服务器启动了");;

            while (true) {
                Socket socket = serverSocket.accept();//如果没有连接会堵塞在这里
                new Thread(new WorkThread(socket, serviceProvider)).start();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        //停止服务端
    }


}
