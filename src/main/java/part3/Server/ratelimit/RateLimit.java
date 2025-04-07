package part3.Server.ratelimit;

public interface RateLimit {

    boolean getToken();//获取访问许可
}
