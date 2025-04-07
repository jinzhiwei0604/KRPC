package part3.Client.circuitBreaker;

import java.util.HashMap;
import java.util.Map;

public class CircuitBreakerProvider {
    //用map存储每个服务的熔断器实例
    private Map<String, CircuitBreaker> circuitBreakerMap = new HashMap<>();

    public synchronized CircuitBreaker getCircuitBreaker(String serviceName){
        CircuitBreaker circuitBreaker;

        //检查是否存在该服务的熔断器
        if(circuitBreakerMap.containsKey(serviceName)){
            circuitBreaker = circuitBreakerMap.get(serviceName);
        }else{
            //不存在则创建一个新的熔断器
            System.out.println("serviceName="+serviceName+"创建一个新的熔断器");
            circuitBreaker=new CircuitBreaker(1,0.5,10000);
            circuitBreakerMap.put(serviceName,circuitBreaker);
        }
        return circuitBreaker;
    }

}
