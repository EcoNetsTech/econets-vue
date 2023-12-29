package cn.econets.blossom.test;

import cn.econets.blossom.server.ServerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

/**
 *
 */
@SpringBootTest(classes = ServerApplication.class)
public class ApplicationTests {
    @Resource
    private PasswordEncoder passwordEncoder;

    @Test
    private void test1(){
        System.out.println(passwordEncoder.encode("123456"));
    }
}
