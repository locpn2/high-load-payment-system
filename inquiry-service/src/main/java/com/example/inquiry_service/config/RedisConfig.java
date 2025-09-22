package com.example.inquiry_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis Configuration - Cấu hình RedisTemplate cho caching
 *
 * RedisTemplate được sử dụng để:
 * - Serialize/deserialize key và value khi lưu trữ trong Redis
 * - StringRedisSerializer cho key để đảm bảo tính nhất quán
 * - GenericJackson2JsonRedisSerializer cho value để hỗ trợ object phức tạp
 */
@Configuration
public class RedisConfig {

    /**
     * Cấu hình RedisTemplate với serializer phù hợp
     * @param connectionFactory - Factory để tạo kết nối Redis
     * @return RedisTemplate đã được cấu hình
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Sử dụng StringRedisSerializer cho key
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Sử dụng GenericJackson2JsonRedisSerializer cho value
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }
}
