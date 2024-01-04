package cn.econets.blossom.framework.apilog.config;

import cn.econets.blossom.framework.common.enums.WebFilterOrderEnum;
import cn.econets.blossom.framework.apilog.core.filter.ApiAccessLogFilter;
import cn.econets.blossom.framework.apilog.core.service.ApiAccessLogFrameworkService;
import cn.econets.blossom.framework.apilog.core.service.ApiAccessLogFrameworkServiceImpl;
import cn.econets.blossom.framework.apilog.core.service.ApiErrorLogFrameworkService;
import cn.econets.blossom.framework.apilog.core.service.ApiErrorLogFrameworkServiceImpl;
import cn.econets.blossom.framework.web.config.WebProperties;
import cn.econets.blossom.module.infrastructure.api.logger.ApiAccessLogApi;
import cn.econets.blossom.module.infrastructure.api.logger.ApiErrorLogApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;

/**
 *
 */
@AutoConfiguration(after = WebProperties.class)
public class ApiLogAutoConfiguration {

    @Bean
    public ApiAccessLogFrameworkService apiAccessLogFrameworkService(ApiAccessLogApi apiAccessLogApi) {
        return new ApiAccessLogFrameworkServiceImpl(apiAccessLogApi);
    }

    @Bean
    public ApiErrorLogFrameworkService apiErrorLogFrameworkService(ApiErrorLogApi apiErrorLogApi) {
        return new ApiErrorLogFrameworkServiceImpl(apiErrorLogApi);
    }

    /**
     * 创建 ApiAccessLogFilter Bean，记录 API 请求日志
     */
    @Bean
    // 允许使用 application.access-log.enable=false 禁用访问日志
    @ConditionalOnProperty(prefix = "application.access-log", value = "enable", matchIfMissing = true)
    public FilterRegistrationBean<ApiAccessLogFilter> apiAccessLogFilter(WebProperties webProperties,
                                                                         @Value("${spring.application.name}") String applicationName,
                                                                         ApiAccessLogFrameworkService apiAccessLogFrameworkService) {
        ApiAccessLogFilter filter = new ApiAccessLogFilter(webProperties, applicationName, apiAccessLogFrameworkService);
        return createFilterBean(filter, WebFilterOrderEnum.API_ACCESS_LOG_FILTER);
    }

    private static <T extends Filter> FilterRegistrationBean<T> createFilterBean(T filter, Integer order) {
        FilterRegistrationBean<T> bean = new FilterRegistrationBean<>(filter);
        bean.setOrder(order);
        return bean;
    }
}
