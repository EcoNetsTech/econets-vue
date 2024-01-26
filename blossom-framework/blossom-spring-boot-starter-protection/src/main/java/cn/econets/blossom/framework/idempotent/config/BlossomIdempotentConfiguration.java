package cn.econets.blossom.framework.idempotent.config;

import cn.econets.blossom.framework.idempotent.core.aop.IdempotentAspect;
import cn.econets.blossom.framework.idempotent.core.keyresolver.IdempotentKeyResolver;
import cn.econets.blossom.framework.idempotent.core.keyresolver.impl.DefaultIdempotentKeyResolver;
import cn.econets.blossom.framework.idempotent.core.keyresolver.impl.ExpressionIdempotentKeyResolver;
import cn.econets.blossom.framework.idempotent.core.redis.IdempotentRedisDAO;
import cn.econets.blossom.framework.redis.config.BlossomRedisAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@AutoConfiguration(after = BlossomRedisAutoConfiguration.class)
public class BlossomIdempotentConfiguration {

    @Bean
    public IdempotentAspect idempotentAspect(List<IdempotentKeyResolver> keyResolvers, IdempotentRedisDAO idempotentRedisDAO) {
        return new IdempotentAspect(keyResolvers, idempotentRedisDAO);
    }

    @Bean
    public IdempotentRedisDAO idempotentRedisDAO(StringRedisTemplate stringRedisTemplate) {
        return new IdempotentRedisDAO(stringRedisTemplate);
    }

    // ========== 各种 IdempotentKeyResolver Bean ==========

    @Bean
    public DefaultIdempotentKeyResolver defaultIdempotentKeyResolver() {
        return new DefaultIdempotentKeyResolver();
    }

    @Bean
    public ExpressionIdempotentKeyResolver expressionIdempotentKeyResolver() {
        return new ExpressionIdempotentKeyResolver();
    }
}
