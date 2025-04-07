package part3.Client.serviceCenter;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import part3.Client.cache.ServiceCache;
import part3.Client.serviceCenter.zkWatcher.WatchZK;

import java.net.InetSocketAddress;
import java.util.List;

public class ZKServiceCenter implements ServiceCenter{

    private CuratorFramework clent;//curator提供的ZK客户端

    private static final String ROOT_PATH = "MyRPC"; //根路径

    private ServiceCache cache;
    public ZKServiceCenter() throws InterruptedException{ //初始化并于ZK服务端进行连接

        RetryPolicy policy = new ExponentialBackoffRetry(1000,3);//指数时间重试策略 初始1秒 后续翻倍
        //初始化
        this.clent = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                .sessionTimeoutMs(40000).retryPolicy(policy).namespace(ROOT_PATH).build();
        this.clent.start();
        System.out.println("Zookeeper连接成功");
        //初始化缓存
        this.cache = new ServiceCache();
        //加入zookeeper事件监听器
        WatchZK watcher = new WatchZK(clent, cache);
        watcher.watchToUpdate(ROOT_PATH);
    }


    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {
        try {
            //List<String> strings = clent.getChildren().forPath("/" + serviceName);//获取路径的所有子节点，子节点保存ip：port
            List<String> serviceList = cache.getServiceFromCache(serviceName);
            if (serviceList == null) {
                serviceList = clent.getChildren().forPath("/" + serviceName);
            }
            String string = serviceList.get(0);//默认使用第一个

            return parseAddress(string);//获取ip地址和接口
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getServiceAddress(InetSocketAddress serviceAddress) {
        return serviceAddress.getHostName() + ":" + serviceAddress.getPort();
    }

    private InetSocketAddress parseAddress(String address) {
        String[] result = address.split("：");
        return new InetSocketAddress(result[0], Integer.parseInt(result[1]));
    }
}
