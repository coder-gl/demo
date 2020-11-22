package com.example.demo.util;


/**
 *
 * @author coder-gl
 */
public class LockThreadRunnable implements Runnable {
    private final String key;
    private final String value;
    private int lockTime;
    private RedisLock redisLock;
    boolean isOk = true;

    public LockThreadRunnable(String key, String value, int lockTime, RedisLock redisLock) {
        this.key = key;
        this.value = value;
        this.lockTime = lockTime;
        this.redisLock = redisLock;
    }

    @Override
    public void run() {
        while (isOk) {
            try {
                //让线程休息会，别老占用资源
                Thread.sleep(lockTime * 1000 / 3);
                /*
                //KEYS 参数
                String[] keys = new String[]{key};
                //ARGV 参数
                Object[] values = new Object[]{value, String.valueOf(lockTime)};
                //这里采用 lua 脚本 “续费锁” ，注意要续费是自己持有的锁， value 值唯一确认现在这把锁是自己持有的
                Object eval = template.eval("if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('expire', KEYS[1],ARGV[2]) else return 0 end",
                        ScriptOutputType.INTEGER,
                        keys, values);
                */
                boolean resetLock = redisLock.resetLock(key, value, lockTime);
                if (resetLock) {
                    System.out.println("续费成功，"+key+"将锁超时时间重置为 " + lockTime + "s" + value );
                } else {
                    stop();
                    System.out.println("续费失败");
                }

            } catch (Exception e) {
                System.out.println("续费线程异常");
                e.printStackTrace();
            }
        }
        System.out.println("续费结束");
    }

    public void stop() {
        this.isOk = false;
    }
}