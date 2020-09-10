package com.example.demo.cfg;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 */
@Configuration
public class RedisSentinelConfig {
    @Autowired
    private Environment environment;
    /**
     * 配置lettuce连接池
     *
     * @return
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.redis.cluster.lettuce.pool")
    public GenericObjectPoolConfig redisPool() {
        return new GenericObjectPoolConfig();
    }

    /**
     * 配置第一个数据源的
     *
     * @return
     */
    @Bean("redisSentinelConfiguration")
    @Primary
    public RedisSentinelConfiguration redisSentinelConfig() {

        Map<String, Object> source = new HashMap<>(8);
        source.put("spring.redis.sentinel.master", environment.getProperty("spring.redis.sentinel.master"));
        source.put("spring.redis.sentinel.nodes", environment.getProperty("spring.redis.sentinel.nodes"));
        RedisSentinelConfiguration redisSentinelConfiguration;
        redisSentinelConfiguration = new RedisSentinelConfiguration(new MapPropertySource("RedisSentinelConfiguration", source));
        int database = Integer.parseInt(environment.getProperty("spring.redis.database"));
        redisSentinelConfiguration.setDatabase(database);
        redisSentinelConfiguration.setPassword(environment.getProperty("spring.redis.password"));

        return redisSentinelConfiguration;

    }

    /**
     * 配置第一个数据源的连接工厂
     * 这里注意：需要添加@Primary 指定bean的名称，目的是为了创建两个不同名称的LettuceConnectionFactory
     *
     * @param redisPool
     * @param redisSentinelConfig
     * @return
     */
    @Bean("lettuceConnectionFactory")
    @Primary
    public LettuceConnectionFactory lettuceConnectionFactory(GenericObjectPoolConfig redisPool, @Qualifier("redisSentinelConfiguration") RedisSentinelConfiguration redisSentinelConfig) {
        LettucePoolingClientConfiguration lettucePoolingClientConfiguration  = LettucePoolingClientConfiguration
                .builder()
                .poolConfig(redisPool)
                .build();
        return new LettuceConnectionFactory(redisSentinelConfig, lettucePoolingClientConfiguration);
    }

    /**
     * 配置第一个数据源的RedisTemplate
     * 注意：这里指定使用名称=factory 的 RedisConnectionFactory
     * 并且标识第一个数据源是默认数据源 @Primary
     *
     * @param redisConnectionFactory
     * @return
     */
    @Bean("redisTemplate")
    @Primary
    public RedisTemplate redisTemplate(@Qualifier("lettuceConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        return getRedisTemplate(redisConnectionFactory);

    }

    /**
     * 配置第二个数据源
     *
     * @return
     */
    /*@Bean("secondaryRedisClusterConfig")
    public RedisClusterConfiguration secondaryRedisConfig() {

        Map<String, Object> source = new HashMap<>(8);
        source.put("spring.redis.cluster.nodes", environment.getProperty("spring.secondaryRedis.cluster.nodes"));
        RedisClusterConfiguration redisClusterConfiguration;
        redisClusterConfiguration = new RedisClusterConfiguration(new MapPropertySource("RedisClusterConfiguration", source));
        redisClusterConfiguration.setPassword(environment.getProperty("spring.redis.password"));

        return redisClusterConfiguration;
    }

    @Bean("secondaryLettuceConnectionFactory")
    public LettuceConnectionFactory secondaryLettuceConnectionFactory(GenericObjectPoolConfig redisPool, @Qualifier("secondaryRedisClusterConfig") RedisClusterConfiguration secondaryRedisClusterConfig) {
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder().poolConfig(redisPool).build();
        return new LettuceConnectionFactory(secondaryRedisClusterConfig, clientConfiguration);
    }
    */
    /**
     * 配置第一个数据源的RedisTemplate
     * 注意：这里指定使用名称=factory2 的 RedisConnectionFactory
     *
     * @param //redisConnectionFactory
     * @return
     */
    /*@Bean("secondaryRedisTemplate")
    public RedisTemplate secondaryRedisTemplate(@Qualifier("secondaryLettuceConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        return getRedisTemplate(redisConnectionFactory);
    }*/

    private RedisTemplate getRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance ,ObjectMapper.DefaultTyping.NON_FINAL);
        //om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        return template;
    }
}
