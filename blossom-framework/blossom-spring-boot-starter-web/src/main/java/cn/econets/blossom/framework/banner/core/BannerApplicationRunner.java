package cn.econets.blossom.framework.banner.core;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.util.ClassUtils;

import java.util.concurrent.TimeUnit;

/**
 * 项目启动成功后，提供文档相关的地址
 *
 */
@Slf4j
public class BannerApplicationRunner implements ApplicationRunner {

    @Value("${server.port:58080}")
    private Integer port;

    @Override
    public void run(ApplicationArguments args) {
        ThreadUtil.execute(() -> {
            ThreadUtil.sleep(1, TimeUnit.SECONDS); // 延迟 1 秒，保证输出到结尾
            log.info("\n----------------------------------------------------------\n\t" +
                            "项目启动成功！\n\t" +
                            "接口文档: \t{} \n\t" +
                            "----------------------------------------------------------",
                    "http://localhost:"+port+"/doc.html");

            // 会员中心
            if (isNotPresent("cn.econets.blossom.module.member.framework.web.config.MemberWebConfiguration")) {
                System.out.println("[会员中心模块 blossom-module-member - 已禁用]");
            }
            // 工作流
            if (isNotPresent("cn.econets.blossom.framework.flowable.config.FlowableConfiguration")) {
                System.out.println("[工作流模块 blossom-module-bpm - 已禁用]");
            }
            // 微信公众号
            if (isNotPresent("cn.econets.blossom.module.mp.framework.mp.config.MpConfiguration")) {
                System.out.println("[微信公众号 blossom-module-mp - 已禁用]");
            }
            // 商城系统
            if (isNotPresent("cn.econets.blossom.module.trade.framework.web.config.TradeWebConfiguration")) {
                System.out.println("[商城系统 blossom-module-mall - 已禁用]");
            }
            // 支付平台
            if (isNotPresent("cn.econets.blossom.module.pay.framework.pay.config.PayConfiguration")) {
                System.out.println("[支付系统 blossom-module-pay - 已禁用]");
            }
        });
    }

    private static boolean isNotPresent(String className) {
        return !ClassUtils.isPresent(className, ClassUtils.getDefaultClassLoader());
    }

}
