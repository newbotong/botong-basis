package com.yunjing.zuul.permission.config;

import com.common.redis.config.BaseRedisConfig;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunjing.zuul.permission.dao.redis.template.ReadonlyStringRedisTemplate;
import com.yunjing.zuul.permission.properties.ReadonlyRedisProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPoolConfig;

import static com.yunjing.zuul.permission.properties.ReadonlyRedisProperties.PREFIX;


/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/4/13
 * @description
 **/
@Configuration
@EnableConfigurationProperties(ReadonlyRedisProperties.class)
@ConditionalOnProperty(prefix = PREFIX, name = "enabled", havingValue = "true")
public class RedisConfig {

    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        return new RedisCacheManager(redisTemplate);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate template = new StringRedisTemplate(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean("readonlyRedisTemplate")
    @Qualifier("readonlyRedisTemplate")
    public ReadonlyStringRedisTemplate readonlyRedisTemplate(ReadonlyRedisProperties readonlyRedisProperties) {
        ReadonlyStringRedisTemplate readonlyStringRedisTemplate = new ReadonlyStringRedisTemplate();
        readonlyStringRedisTemplate.setConnectionFactory(
                readonlyRedisConnectionFactory(readonlyRedisProperties.getHost(), readonlyRedisProperties.getPort(),
                        readonlyRedisProperties.getPassword(), readonlyRedisProperties.getPool().getMaxIdle(),
                        readonlyRedisProperties.getPool().getMinIdle(),
                        readonlyRedisProperties.getDatabase(),
                        readonlyRedisProperties.getPool().getMaxWait(),
                        true));
        return readonlyStringRedisTemplate;
    }

    private RedisConnectionFactory readonlyRedisConnectionFactory(String hostName, int port, String password, int maxIdle,
                                                                  int minIdle, int index, long maxWaitMillis, boolean testOnBorrow) {
        JedisConnectionFactory jedis = new JedisConnectionFactory();
        jedis.setHostName(hostName);
        jedis.setPort(port);
        if (StringUtils.isNotEmpty(password)) {
            jedis.setPassword(password);
        }
        if (index != 0) {
            jedis.setDatabase(index);
        }
        jedis.setPoolConfig(readonlyPoolConfig(maxIdle, minIdle, maxWaitMillis, testOnBorrow));
        // 初始化连接pool
        jedis.afterPropertiesSet();
        return jedis;
    }

    private JedisPoolConfig readonlyPoolConfig(int maxIdle, int minIdle, long maxWaitMillis, boolean testOnBorrow) {
        JedisPoolConfig poolCofig = new JedisPoolConfig();
        poolCofig.setMaxIdle(maxIdle);
        poolCofig.setMinIdle(minIdle);
        poolCofig.setMaxWaitMillis(maxWaitMillis);
        poolCofig.setTestOnBorrow(testOnBorrow);
        return poolCofig;
    }
}
