package part3.Client.retry;

import com.github.rholder.retry.*;
import part3.Client.rpcClient.RpcClient;
import part3.common.Message.RpcRequest;
import part3.common.Message.RpcResponse;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GuavaRetry {

    private RpcClient rpcClient;//用来发送RPC请求


    public RpcResponse sendServiceWithRetry(RpcRequest request,RpcClient rpcClient) {
        this.rpcClient = rpcClient;
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder() //创建一个retryer构建起配置重试逻辑
                .retryIfException()  //无论出现什么异常，都进行重试
                .withWaitStrategy(WaitStrategies.fixedWait(2, TimeUnit.SECONDS))      //重试等待策略：等待 2s 后再进行重试
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))   //重试停止策略：重试达到 3 次
                //配置重试监听器
                .withRetryListener(new RetryListener() { //RetryListener是一个接口，每次重试时onRetry都会被处罚
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        //重试监听器，允许每次重试时执行自定义操作
                        //打印重试信息
                        System.out.println("RetryListener: 第" + attempt.getAttemptNumber() + "次调用");

                    }
                })
                .build();
        try {
            return retryer.call(() -> rpcClient.sendRequest(request));
        } catch (ExecutionException | RetryException e) {
            e.printStackTrace();
        }
        return RpcResponse.fail();
    }
}
