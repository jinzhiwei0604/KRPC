package part3.Server.provider;

import part3.Server.ratelimit.RateLimit;
import part3.Server.ratelimit.TokenBucketRateLimitImpl;

import java.util.HashMap;
import java.util.Map;

public class RateLimitProvider {

    //存储接口名称与对应的速率限制器实例之间的关系
    private Map<String, RateLimit> rateLimitMap = new HashMap<>();

    //根据接口名称获取对应的速率限制器
    public RateLimit getRateLimit(String interfaceName){

        //判断Map中是否已有该接口的速率限制器实例
        if(!rateLimitMap.containsKey(interfaceName)){
            RateLimit rateLimit = new TokenBucketRateLimitImpl(100,10);
            rateLimitMap.put(interfaceName, rateLimit);

            return  rateLimit;
        }
        //若存在，则直接从map中获取返回
        return rateLimitMap.get(interfaceName);
    }
}
