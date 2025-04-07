package part3.Client.serviceCenter.balance;

import java.util.List;

public class RoundLoadBalance implements LoadBalance{

    private int choose = -1;
    @Override
    public String balance(List<String> addressList) {
        choose++;
        choose = choose % addressList.size();
        System.out.println("负载均衡选择了"+addressList.get(choose)+"服务器");
        return addressList.get(choose);
    }

    @Override
    public void addNode(String node) {

    }

    @Override
    public void delNode(String node) {

    }
}
