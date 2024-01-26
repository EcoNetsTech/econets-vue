package cn.econets.blossom.framework.errorcode.config;

import cn.econets.blossom.framework.errorcode.core.generator.ErrorCodeAutoGenerator;
import cn.econets.blossom.framework.errorcode.core.generator.ErrorCodeAutoGeneratorImpl;
import cn.econets.blossom.framework.errorcode.core.loader.ErrorCodeLoader;
import cn.econets.blossom.framework.errorcode.core.loader.ErrorCodeLoaderImpl;
import cn.econets.blossom.module.system.api.errorcode.ErrorCodeApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 错误码配置类
 *
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "application.error-code", value = "enable", matchIfMissing = true) // 允许使用 aplication.error-code.enable=false 禁用访问日志
@EnableConfigurationProperties(ErrorCodeProperties.class)
@EnableScheduling // 开启调度任务的功能，因为 ErrorCodeRemoteLoader 通过定时刷新错误码
public class ErrorCodeAutoConfiguration {

    @Bean
    public ErrorCodeAutoGenerator errorCodeAutoGenerator(@Value("${spring.application.name}") String applicationName,
                                                         ErrorCodeProperties errorCodeProperties,
                                                         ErrorCodeApi errorCodeApi) {
        return new ErrorCodeAutoGeneratorImpl(applicationName, errorCodeProperties.getConstantsClassList(), errorCodeApi);
    }

    @Bean
    public ErrorCodeLoader errorCodeLoader(@Value("${spring.application.name}") String applicationName,
                                           ErrorCodeApi errorCodeApi) {
        return new ErrorCodeLoaderImpl(applicationName, errorCodeApi);
    }

}
