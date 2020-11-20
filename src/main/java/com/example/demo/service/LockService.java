package com.example.demo.service;

import com.example.demo.util.LockThreadRunnable;
import com.example.demo.util.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author coder-gl
 */
@Service
public class LockService {

    @Autowired
    private RedisLock redisLock;

    public void lockResource()  {
        String lockKey = "lock:test";
        String requestId = randomStr();
        Integer time = 10;

        try {
            boolean lockResult = redisLock.tryGetLock(lockKey, requestId, time);
            if(lockResult){
                LockThreadRunnable runnable = new LockThreadRunnable(lockKey,requestId,time,redisLock);
                Thread lockThread = new Thread(runnable);
                lockThread.setDaemon(true);
                lockThread.start();
                //业务执行
                System.out.println(Thread.currentThread().getName());
                Thread.sleep(60*1000);
                //服务结束，通知“续费”线程结束
                runnable.stop();
                lockThread.interrupt();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            redisLock.removeLock(lockKey,requestId);
        }
    }

    private synchronized String randomStr(){
        return UUID.randomUUID().toString() + String.valueOf((int) Math.random()*100);
    }
}
