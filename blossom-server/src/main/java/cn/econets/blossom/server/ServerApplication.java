package cn.econets.blossom.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目启动类
 */
@SuppressWarnings("SpringComponentScan") // 忽略 IDEA 无法识别 ${application.info.base-package}
@SpringBootApplication(scanBasePackages = {"${application.info.base-package}.server", "${application.info.base-package}.module"})
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class);
    }
}
