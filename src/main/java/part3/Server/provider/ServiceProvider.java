package part3.Server.provider;

import part3.Server.serviceRegister.ServiceRegister;
import part3.Server.serviceRegister.impl.ZKServiceRegister;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class ServiceProvider {

    private String host;
    private int port;
    private ServiceRegister serviceRegister;
    private Map<String, Object> interfaceProvider;//存放服务的实例，接口的全限定名String，对应的实现类Object
    private RateLimitProvider rateLimitProvider;

    public ServiceProvider(String host,int port) {
        this.host = host;
        this.port = port;
        this.interfaceProvider = new HashMap<>();
        this.serviceRegister = new ZKServiceRegister();
        this.rateLimitProvider = new RateLimitProvider();
        }


    //本地注册服务
    public void provideServiceInterface(Object service,boolean canRetry) { //接受服务实例
        String serviceName = service.getClass().getName(); //接受服务对象的类名
        Class<?>[] interfaceName = serviceName.getClass().getInterfaces();//获得服务对象实现的所有接口

        for (Class<?> clazz : interfaceName) {
            interfaceProvider.put(clazz.getName(), service);
            serviceRegister.register(clazz.getName(),new InetSocketAddress(host,port),canRetry);
        }
    }

    //获取服务实例
    public Object getService(String interfaceName) {
        return interfaceProvider.get((interfaceName));
    }

    public RateLimitProvider getRateLimitProvider() {
        return  rateLimitProvider;
    }
}
