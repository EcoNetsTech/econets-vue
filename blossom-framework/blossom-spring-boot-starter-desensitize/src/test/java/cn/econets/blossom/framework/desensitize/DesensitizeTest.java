package cn.econets.blossom.framework.desensitize;

import cn.econets.blossom.framework.common.util.json.JsonUtils;
import cn.econets.blossom.framework.desensitize.annotation.Address;
import cn.econets.blossom.framework.desensitize.core.regex.annotation.EmailDesensitize;
import cn.econets.blossom.framework.desensitize.core.regex.annotation.RegexDesensitize;
import cn.econets.blossom.framework.desensitize.core.slider.annotation.*;
import cn.econets.blossom.framework.test.core.ut.BaseMockitoUnitTest;
import lombok.Data;
import org.junit.jupiter.api.Test;

/**
 *
 */
public class DesensitizeTest extends BaseMockitoUnitTest {

    @Test
    public void test(){
        DesensitizeDemo desensitizeDemo = new DesensitizeDemo();
        desensitizeDemo.setNickname("测试脱敏");
        desensitizeDemo.setBankCard("9988002866797031");
        desensitizeDemo.setCarLicense("粤A66666");
        desensitizeDemo.setFixedPhone("01086551122");
        desensitizeDemo.setIdCard("530321199204074611");
        desensitizeDemo.setPassword("123456");
        desensitizeDemo.setPhoneNumber("13248765917");
        desensitizeDemo.setSlider1("ABCDEFG");
        desensitizeDemo.setSlider2("ABCDEFG");
        desensitizeDemo.setSlider3("ABCDEFG");
        desensitizeDemo.setEmail("123456@gmail.com");
        desensitizeDemo.setRegex("你好，我是测试脱敏");
        desensitizeDemo.setAddress("北京市海淀区上地十街10号");
        desensitizeDemo.setOrigin("测试脱敏");

        System.out.println(JsonUtils.toJsonString(desensitizeDemo));
    }

    @Data
    public static class DesensitizeDemo {

        @ChineseNameDesensitize
        private String nickname;
        @BankCardDesensitize
        private String bankCard;
        @CarLicenseDesensitize
        private String carLicense;
        @FixedPhoneDesensitize
        private String fixedPhone;
        @IdCardDesensitize
        private String idCard;
        @PasswordDesensitize
        private String password;
        @MobileDesensitize
        private String phoneNumber;
        @SliderDesensitize(prefixKeep = 6, suffixKeep = 1, replacer = "#")
        private String slider1;
        @SliderDesensitize(prefixKeep = 3, suffixKeep = 3)
        private String slider2;
        @SliderDesensitize(prefixKeep = 10)
        private String slider3;
        @EmailDesensitize
        private String email;
        @RegexDesensitize(regex = "测试脱敏", replacer = "*")
        private String regex;
        @Address
        private String address;
        private String origin;

    }
}
