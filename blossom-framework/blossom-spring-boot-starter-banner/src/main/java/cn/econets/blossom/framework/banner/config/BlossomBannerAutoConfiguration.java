package cn.econets.blossom.framework.banner.config;

import cn.econets.blossom.framework.banner.BannerApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Banner 的自动配置类
 *
 * @author 芋道源码
 */
@AutoConfiguration
public class BlossomBannerAutoConfiguration {

    @Bean
    public BannerApplicationRunner bannerApplicationRunner() {
        return new BannerApplicationRunner();
    }

}
