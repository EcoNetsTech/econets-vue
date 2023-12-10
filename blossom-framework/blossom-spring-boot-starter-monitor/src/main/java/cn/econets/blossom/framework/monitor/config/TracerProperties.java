package cn.econets.blossom.framework.monitor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * BizTracer配置类
 *
 */
@ConfigurationProperties("blossom.tracer")
@Data
public class TracerProperties {
}
