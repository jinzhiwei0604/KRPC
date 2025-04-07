package part3.Client.serviceCenter.balance;

import java.util.*;

public class ConsistencyHashBalance implements LoadBalance{

    private static final int VTRTUAL_NUM = 5;//虚拟节点个数

    private SortedMap<Integer, String> shards = new TreeMap<>();//保存虚拟节点的hash值和对应的虚拟节点

    private List<String> realNodes = new LinkedList<String>();//真实节点列表

    private String[] servers = null;//模拟初始化服务器

    //模拟负载均衡，通过随机生成一个字符串来模拟请求
    @Override
    public String balance(List<String> addressList) {
        String random = UUID.randomUUID().toString();
        return getServer(random, addressList); //返回服务器地址
    }

    private String getServer(String random, List<String> addressList) {

        return null;
    }

    @Override
    public void addNode(String node) {

    }

    @Override
    public void delNode(String node) {

    }
}
