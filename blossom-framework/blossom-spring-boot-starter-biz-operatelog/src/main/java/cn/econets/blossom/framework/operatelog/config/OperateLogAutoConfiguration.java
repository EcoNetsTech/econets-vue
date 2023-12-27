package cn.econets.blossom.framework.operatelog.config;

import cn.econets.blossom.framework.operatelog.core.aop.OperateLogAspect;
import cn.econets.blossom.framework.operatelog.core.service.OperateLogFrameworkService;
import cn.econets.blossom.framework.operatelog.core.service.OperateLogFrameworkServiceImpl;
import cn.econets.blossom.module.system.api.logger.OperateLogApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class OperateLogAutoConfiguration {

    @Bean
    public OperateLogAspect operateLogAspect() {
        return new OperateLogAspect();
    }

    @Bean
    public OperateLogFrameworkService operateLogFrameworkService(OperateLogApi operateLogApi) {
        return new OperateLogFrameworkServiceImpl(operateLogApi);
    }

}
