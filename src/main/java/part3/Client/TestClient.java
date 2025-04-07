package part3.Client;

import part3.Client.proxy.ClientProxy;
import part3.common.UserService.UserService;
import part3.common.pojo.User;

public class TestClient {
    public static void main(String[] args) throws InterruptedException {

        ClientProxy clientProxy = new ClientProxy();

        UserService proxy = clientProxy.getProxy(UserService.class);
        System.out.println("请求代理服务");
        //模拟  每30次调用暂停十秒模拟实际的负载
        for(int i =0;i<120;i++){
            Integer i1 = i;
            if(i%30==0){
                Thread.sleep(10000);
            }
            new Thread(()-> {
                try {
                    User user = proxy.getUserByUserId(1);

                    System.out.println("从服务器得到的user" + user.toString());

                    User u = User.builder().id(i1).userName("jzw").sex(true).build();
                    Integer id = proxy.insertUsedId(u);
                    System.out.println("向服务器插入user的id" + id);
                } catch (Exception e) {
                    System.out.println("user为空");
                    e.printStackTrace();
                }
            }).start();
        }
//        User user = proxy.getUserByUserId(1);
//
//        System.out.println("从服务器得到的user"+user.toString());
//
//        User u = User.builder().id(100).userName("jzw").sex(true).build();
//        Integer id = proxy.insertUsedId(u);
//        System.out.println("向服务器插入user的id"+id);
    }
}
