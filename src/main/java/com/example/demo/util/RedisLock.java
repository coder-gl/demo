package com.example.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * redis锁工具类
 * @author coder-gl
 */
@Component
public class RedisLock {
    @Autowired
    private RedisTemplate<String,Object> template;

    /**
     * 尝试抢锁
     * @param lockKey
     * @param requestId
     * @param time
     * @return
     */
    public Boolean tryGetLock(String lockKey,String requestId,Integer time){
        return template.opsForValue().setIfAbsent(lockKey,requestId,time, TimeUnit.SECONDS);
    }

    /**
     * 刷新锁的时间
     * @param lockKey
     * @param requestId
     * @param time
     * @return
     */
    public Boolean resetLock(String lockKey,String requestId,Integer time){
        String  result = (String) template.opsForValue().get(lockKey);
        if(requestId.equals(result)){
            return template.expire(lockKey,time, TimeUnit.SECONDS);
        }
        return false;
    }

    /**
     * 删除锁
     * @param lockKey
     * @param requestId
     * @return
     */
    public Boolean  removeLock(String lockKey,String requestId){
        if(requestId.equals(template.opsForValue().get(lockKey))){
            return template.delete(lockKey);
        }
        return false;
    }


}
