package part3.Server.ratelimit;

public class TokenBucketRateLimitImpl implements RateLimit{

    private static int RATE; //令牌产生速率  每个RATE毫秒生成一个令牌
    private static int CAPACITY; //桶容量
    private volatile int curCapicity;//当前桶容量
    private volatile long timeStamp = System.currentTimeMillis();//上次请求的时间戳

    public TokenBucketRateLimitImpl(int rate,int capacity) {
        RATE = rate;
        CAPACITY = capacity;
        curCapicity = capacity;
    }
    @Override
    public boolean getToken() {
        if (curCapicity > 0) {
            curCapicity--;
            return true;
        }

        long current = System.currentTimeMillis();
        //判断上次获取令牌是否已过RATE时间
        if (current - timeStamp >= RATE) {
            //计算应该产生多少令牌
            if((current-timeStamp)/RATE>=2){
                curCapicity += (int) ((current-timeStamp)/RATE);
            }
            if(curCapicity>CAPACITY){
                curCapicity = CAPACITY;
            }
            timeStamp = current;
            return  true;
        }
        return false;
    }
}
