package part3.Client.serviceCenter;


import java.net.InetSocketAddress;

//服务中心接口
public interface ServiceCenter {

    InetSocketAddress serviceDiscovery(String serviceName);//根据服务查询地址

    Boolean checkRetry(String serviceName);//判断是否可重试
}
