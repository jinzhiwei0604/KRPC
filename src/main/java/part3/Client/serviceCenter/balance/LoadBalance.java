package part3.Client.serviceCenter.balance;

import java.util.List;

public interface LoadBalance {

    String balance(List<String> addressList);//实现负载均衡算法，返回分配的地址

    void addNode(String node);//添加节点
    void delNode(String node);//删除节点
}
