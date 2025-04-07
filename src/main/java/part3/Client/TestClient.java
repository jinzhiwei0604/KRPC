package part3.Client;

import part3.Client.proxy.ClientProxy;
import part3.common.UserService.UserService;
import part3.common.pojo.User;

public class TestClient {
    public static void main(String[] args) throws InterruptedException {

        ClientProxy clientProxy = new ClientProxy();

        UserService proxy = clientProxy.getProxy(UserService.class);
        System.out.println("请求代理服务");

        User user = proxy.getUserByUserId(1);

        System.out.println("从服务器得到的user"+user.toString());

        User u = User.builder().id(100).userName("jzw").sex(true).build();
        Integer id = proxy.insertUsedId(u);
        System.out.println("向服务器插入user的id"+id);
    }
}
