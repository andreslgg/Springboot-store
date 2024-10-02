package com.store.oncommerce_web.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.oncommerce_web.model.Product;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class RedisConfig {

    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Bean
    public RedisTemplate<String, Product> redisTemplate(RedisConnectionFactory connectionFactory) {
        try {
            RedisTemplate<String, Product> template = new RedisTemplate<>();
            template.setConnectionFactory(connectionFactory);

            // Configuración de serializadores
            Jackson2JsonRedisSerializer<Product> serializer = new Jackson2JsonRedisSerializer<>(Product.class);
            template.setValueSerializer(serializer);
            template.setKeySerializer(new StringRedisSerializer());

            template.afterPropertiesSet();
            return template;
        } catch (Exception e) {
            logger.error("No se pudo conectar a Redis. La aplicación continuará sin cache.", e);
            return null; // O retorna un bean de RedisTemplate vacío
        }
    }
}

