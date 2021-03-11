package com.naharavirtualwallet.virtualwallet;

import com.naharavirtualwallet.virtualwallet.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories
@EnableCaching
public class virtualWalletApplication {
	@Bean
	LettuceConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory();
	}

	@Bean
	public RedisTemplate<?, ?> redisTemplate(){
     RedisTemplate<byte[], byte[]> redisTemplate=new RedisTemplate<>();
     redisTemplate.setConnectionFactory(redisConnectionFactory());
     return redisTemplate;
	}
	public static void main(String[] args) {
		SpringApplication.run(virtualWalletApplication.class, args);
	}

}
