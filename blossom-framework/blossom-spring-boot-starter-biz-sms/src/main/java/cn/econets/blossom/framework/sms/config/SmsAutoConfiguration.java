package cn.econets.blossom.framework.sms.config;

import cn.econets.blossom.framework.sms.core.client.SmsClientFactory;
import cn.econets.blossom.framework.sms.core.client.impl.SmsClientFactoryImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 短信配置类
 *
 */
@AutoConfiguration
public class SmsAutoConfiguration {

    @Bean
    public SmsClientFactory smsClientFactory() {
        return new SmsClientFactoryImpl();
    }

}
