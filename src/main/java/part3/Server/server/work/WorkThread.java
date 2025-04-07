package part3.Server.server.work;


import lombok.AllArgsConstructor;
import part3.Server.provider.ServiceProvider;
import part3.common.Message.RpcRequest;
import part3.common.Message.RpcResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

@AllArgsConstructor
public class WorkThread implements Runnable{


    private Socket socket;
    private ServiceProvider serviceProvider;




    @Override
    public void run() {

        try {
            //将响应数据(即服务端返回的response)通过网络连接发送给客户端
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            //从客户端的网络连接中接收数据，读取序列化对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            //读取客户端传来的request
            RpcRequest rpcRequest = (RpcRequest) ois.readObject();

            //反射调用服务并获取返回值
            RpcResponse rpcResponse = getResponse(rpcRequest);

            oos.writeObject(rpcResponse);
            oos.flush();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private RpcResponse getResponse(RpcRequest rpcRequest) {

        //获取接口名字并获取其对应的服务类
        String interfaceName = rpcRequest.getInterfaceName();
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
