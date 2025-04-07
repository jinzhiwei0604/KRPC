package part3.Client.circuitBreaker;

import java.util.concurrent.atomic.AtomicInteger;

public class CircuitBreaker {

    //当前状态
    private CircuitBreakerState state = CircuitBreakerState.CLOSED;//熔断器初始状态，关闭
    private AtomicInteger failureCount = new AtomicInteger(0);//失败请求计数
    private AtomicInteger successCount = new AtomicInteger(0);//成功请求计数
    private AtomicInteger requestCount = new AtomicInteger(0);//请求总数
    //失败次数阈值
    private final int failureThreshold;//失败阈值
    //半开启-》关闭状态的成功次数比例
    private final double halfOpenSuccessRate; //半开状态下的成功率阈值
    //恢复时间
    private final long retryTimePeriod; //重置时间周期
    //上一次失败时间
    private long lastFailureTime = 0; //最后一次失败时间

    //构造函数初始化熔断器参数
    public CircuitBreaker(int failureThreshold, double halfOpenSuccessRate, long retryTimePeriod) {
        this.failureThreshold = failureThreshold;
        this.halfOpenSuccessRate = halfOpenSuccessRate;
        this.retryTimePeriod = retryTimePeriod;
    }

    //根据当前熔断器状态判断是否允许请求
    public synchronized boolean allowRequest(){
        long currentTime = System.currentTimeMillis();
        switch (state){
            case OPEN:
                if(currentTime-lastFailureTime>retryTimePeriod){
                    state = CircuitBreakerState.HALF_OPEN;
                    resetCounts(); //重置计数
                    return true;
                }
                System.out.println("熔断生效!!!!!!!");
                return false;//继续熔断
            case HALF_OPEN: //在半开状态下记录请求
                requestCount.incrementAndGet();
                return true;
            case CLOSED:
            default:
                return true;
        }
    }
    //记录一次成功的请求
    public synchronized void recordSuccess() {
        if (state == CircuitBreakerState.HALF_OPEN) {
            successCount.incrementAndGet();
            if (successCount.get() >= halfOpenSuccessRate * requestCount.get()) {
                state = CircuitBreakerState.CLOSED;
                resetCounts();
            }
        } else {
            resetCounts();
        }
    }

    //记录失败
    public synchronized void recordFailure() {
        failureCount.incrementAndGet();
        System.out.println("记录失败!!!!!!!失败次数:"+failureCount);
        lastFailureTime = System.currentTimeMillis();
        if (state == CircuitBreakerState.HALF_OPEN) {
            state = CircuitBreakerState.OPEN;
            lastFailureTime = System.currentTimeMillis();
        } else if (failureCount.get() >= failureThreshold) {
            state = CircuitBreakerState.OPEN;
        }
    }

    private void resetCounts() {
        failureCount.set(0);
        successCount.set(0);
        requestCount.set(0);
    }

     enum CircuitBreakerState{
        OPEN,HALF_OPEN,CLOSED;
     }
}
