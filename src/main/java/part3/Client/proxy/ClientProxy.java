package part3.Client.proxy;

import lombok.AllArgsConstructor;
import part3.Client.circuitBreaker.CircuitBreaker;
import part3.Client.circuitBreaker.CircuitBreakerProvider;
import part3.Client.netty.nettyInitializer.NettyRpcClient;
import part3.Client.retry.GuavaRetry;
import part3.Client.rpcClient.RpcClient;
import part3.Client.serviceCenter.ServiceCenter;
import part3.Client.serviceCenter.ZKServiceCenter;
import part3.common.Message.RpcRequest;
import part3.common.Message.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@AllArgsConstructor
public class ClientProxy implements InvocationHandler {
    //传入参数service接口的class对象，反射封装成一个request


    private RpcClient rpcClient;
    private ServiceCenter serviceCenter;
    private CircuitBreakerProvider circuitBreakerProvider;
    public ClientProxy() throws InterruptedException {
        serviceCenter = new ZKServiceCenter();
        rpcClient = new NettyRpcClient(serviceCenter);
        circuitBreakerProvider = new CircuitBreakerProvider();

    }
    //jdk动态代理，每一次代理对象调用方法，都会经过此方法增强（反射获取request对象，socket发送到服务端）
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //构建request
        RpcRequest request=RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args).paramsType(method.getParameterTypes()).build();

        //熔断器
        CircuitBreaker circuitBreaker = circuitBreakerProvider.getCircuitBreaker(method.getName());
        if(!circuitBreaker.allowRequest()){
            System.out.println("熔断开启，请求失败");
            return null;
        }
        //数据传输
        RpcResponse response = rpcClient.sendRequest(request);
        //保持幂等性，只对白名单上注册的服务进行重试
        if (serviceCenter.checkRetry(request.getInterfaceName())) {
            response = new GuavaRetry().sendServiceWithRetry(request, rpcClient);//重试
        } else {
            response = rpcClient.sendRequest(request);//调用一次
        }
        return response.getData();
    }

    public <T>T getProxy(Class<T> clazz){
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T)o;
    }



}