package com.example.demo.cfg;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 *	redis 的单例实现
 *  @author coder-gl
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig {    
		@Value("${spring.redis.host}")
	    private String host;
		@Value ("${spring.redis.port}")
	    private String port;
	    @Value("${spring.redis.password}")
	    private String password;
	    @Value("${spring.redis.database}")
	    private Integer database;
	    @Value("${spring.redis.lettuce.pool.max-idle}")
	    private Integer maxIdle;
	    @Value("${spring.redis.lettuce.pool.min-idle}")
	    private Integer minIdle;
	    @Value("${spring.redis.lettuce.pool.max-active}")
	    private Integer maxTotal;
	    @Value("${spring.redis.lettuce.pool.max-wait}")
	    private Long maxWaitMillis;



		@Bean
	    public LettuceConnectionFactory lettuceConnectionFactory() {
	        // 连接池配置
	        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
	        poolConfig.setMaxIdle(maxIdle == null ? 8 : maxIdle);
	        poolConfig.setMinIdle(minIdle == null ? 1 : minIdle);
	        poolConfig.setMaxTotal(maxTotal == null ? 8 : maxTotal);
	        poolConfig.setMaxWaitMillis(maxWaitMillis == null ? 5000L : maxWaitMillis);
	        LettucePoolingClientConfiguration lettucePoolingClientConfiguration = LettucePoolingClientConfiguration.builder()
	                .poolConfig(poolConfig)
	                .build();
	        // 单机redis
	        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
	        redisConfig.setHostName(host==null||"".equals(host)?"localhost":host);
	        redisConfig.setPort(Integer.valueOf(port==null||"".equals(port)?"6379":port));

	        if (password != null && !"".equals(password)) {
	            redisConfig.setPassword(password);
	        }
	        
	        redisConfig.setDatabase(database==null?0:database);
	       
	        return new LettuceConnectionFactory(redisConfig, lettucePoolingClientConfiguration);
	    }
	    @Bean
	    public RedisTemplate<String, Object> redisCacheTemplate(LettuceConnectionFactory redisConnectionFactory) {
	       return getRedisTemplate(redisConnectionFactory);
	    }

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