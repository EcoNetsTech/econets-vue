package cn.econets.blossom.module.trade.framework.order.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// TODO 可以直接给 TradeOrderProperties 一个 @Component生效哈
/**
 */
@Configuration
@EnableConfigurationProperties(TradeOrderProperties.class)
public class TradeOrderConfig {
}
