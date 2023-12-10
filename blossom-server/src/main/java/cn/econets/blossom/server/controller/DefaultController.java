package cn.econets.blossom.server.controller;

import cn.econets.blossom.framework.common.pojo.CommonResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class DefaultController {

    @RequestMapping("/test")
    public CommonResult<String> test() {
        return CommonResult.success("测试接口");
    }

}
