package com.example.demo.service;

import com.example.demo.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author Administrator
 */
@Service
public class UserService {

    @Autowired
    @Qualifier(value = "redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;
    /*@Autowired
    @Qualifier(value = "redisTemplate")
    private StringRedisTemplate stringRedisTemplate;*/

    private static String keyStr = "userModel";

    private static String keyNameStr = "userName";

    public String  getUserName(){
        UserModel model = new UserModel();
        String result  = (String) redisTemplate.opsForValue().get(keyNameStr);
        if ( StringUtils.isEmpty(result)) {
            model.setName("数据库：酷酷酷");
            model.setAge(25);
            model.setSex("男");
            redisTemplate.opsForValue().set(keyNameStr,"redis：酷酷酷");
            return model.getName();
        }
        return result;
    }

    public UserModel  getUserModel(){
        UserModel model = (UserModel) redisTemplate.opsForValue().get(keyStr);
        if ( model == null ) {
            model = new UserModel();
            model.setName("redis：酷酷酷");
            model.setAge(25);
            model.setSex("男");
            redisTemplate.opsForValue().set(keyStr,model);

            model = new UserModel();
            model.setName("数据库：酷酷酷");
            model.setAge(25);
            model.setSex("男");
            return model;
        }
        return model;
    }

    public void updateUserModel(){

    }
}
