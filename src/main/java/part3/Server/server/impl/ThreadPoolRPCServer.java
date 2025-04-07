package part3.Server.server.impl;

import part3.Server.provider.ServiceProvider;
import part3.Server.server.RpcServer;
import part3.Server.server.work.WorkThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolRPCServer implements RpcServer {

    private final ThreadPoolExecutor threadPool; //定义一个线程池对象，用于管理和执行线程任务
    private ServiceProvider serviceProvider;

    public ThreadPoolRPCServer(ServiceProvider serviceProvider){
        threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                1000,60, TimeUnit.SECONDS,new ArrayBlockingQueue<>(100));
        this.serviceProvider = serviceProvider;
    }
    @Override
    public void start(int port) {
        System.out.println("服务端启动了");
        try {
            ServerSocket serverSocket = new ServerSocket();

            while (true) {
                Socket socket = serverSocket.accept();//如果没有连接会堵塞在这里
                new Thread(new WorkThread(socket, serviceProvider)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {

    }
}
