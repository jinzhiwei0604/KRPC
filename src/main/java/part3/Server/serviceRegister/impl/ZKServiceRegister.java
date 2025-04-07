package part3.Server.serviceRegister.impl;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import part3.Server.serviceRegister.ServiceRegister;

import java.net.InetSocketAddress;

public class ZKServiceRegister implements ServiceRegister {


    private CuratorFramework clent;//curator提供的ZK客户端

    private static final String ROOT_PATH = "MyRPC"; //根路径

    public  ZKServiceRegister() {

        RetryPolicy policy = new ExponentialBackoffRetry(1000,3);//指数时间重试策略 初始1秒 后续翻倍
        //初始化
        this.clent = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                .sessionTimeoutMs(30000).retryPolicy(policy).namespace(ROOT_PATH).build();//30秒没有操作就断开连接
        this.clent.start();
        System.out.println("Zookeeper连接成功");
    }
    @Override
    public void register(String serviceName, InetSocketAddress serviceAddress) {

        try {
            //根据服务名字检查是否存在 不存在则创建永久节点，服务下线时不删服务名，只删除地址
            if (clent.checkExists().forPath("/" + serviceName) == null) {
                clent.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/" + serviceName);
            }
            String path = "/" + serviceName + "/" + getServiceAddress(serviceAddress);
            //临时节点,服务断开时自动删除该节点
            clent.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
        } catch (Exception e) {
            System.out.println("此服务已存在");
        }


    }
    //地址->字符串
    private String getServiceAddress(InetSocketAddress serviceAddress) {
        return serviceAddress.getHostName() + ":" + serviceAddress.getPort();
    }
    //字符串->地址
    private InetSocketAddress parseAddress(String address) {
        String[] result = address.split("：");
        return new InetSocketAddress(result[0], Integer.parseInt(result[1]));
    }
}
