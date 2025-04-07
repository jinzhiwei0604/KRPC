package part3.common.UserService;

import part3.common.pojo.User;

public interface UserService {
    User getUserByUserId(Integer id);
    Integer insertUsedId(User user);


}
