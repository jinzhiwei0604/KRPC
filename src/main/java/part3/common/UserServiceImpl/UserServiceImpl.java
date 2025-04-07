package part3.common.UserServiceImpl;

import part3.common.UserService.UserService;
import part3.common.pojo.User;

import java.util.Random;
import java.util.UUID;

public class UserServiceImpl implements UserService {
    public User getUserByUserId(Integer id) {
        System.out.println("客户端查询了id为"+id+"的用户");
        Random random = new Random();

        User user = User.builder().userName(UUID.randomUUID().toString()).id(id)
                .sex(random.nextBoolean()).build();
        return user;
    }

    public Integer insertUsedId(User user) {
        System.out.println("插入数据成功"+user.getUserName());
        return user.getId();
    }
}
