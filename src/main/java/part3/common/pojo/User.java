package part3.common.pojo;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User {
    String userName;
    int id;
    boolean sex;


}
