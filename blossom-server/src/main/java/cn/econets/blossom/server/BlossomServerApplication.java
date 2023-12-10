package cn.econets.blossom.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目启动类
 */
@SpringBootApplication(scanBasePackages = {"${blossom.info.base-package}.server", "${blossom.info.base-package}.module"})
public class BlossomServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlossomServerApplication.class);
    }
}
