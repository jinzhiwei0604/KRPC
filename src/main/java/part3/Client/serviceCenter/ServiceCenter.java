package part3.Client.serviceCenter;


import java.net.InetSocketAddress;

//服务中心接口
public interface ServiceCenter {

    InetSocketAddress serviceDiscovery(String serviceName);
}
