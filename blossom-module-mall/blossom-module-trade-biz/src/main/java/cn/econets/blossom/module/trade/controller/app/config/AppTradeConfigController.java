package cn.econets.blossom.module.trade.controller.app.config;

import cn.hutool.core.util.ObjUtil;
import cn.econets.blossom.framework.common.pojo.CommonResult;
import cn.econets.blossom.module.trade.controller.app.config.vo.AppTradeConfigRespVO;
import cn.econets.blossom.module.trade.convert.config.TradeConfigConvert;
import cn.econets.blossom.module.trade.dal.dataobject.config.TradeConfigDO;
import cn.econets.blossom.module.trade.service.config.TradeConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static cn.econets.blossom.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 App - 交易配置")
@RestController
@RequestMapping("/trade/config")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AppTradeConfigController {

    @Resource
    private TradeConfigService tradeConfigService;

    @Value("${application.tencent-lbs-key}")
    private String tencentLbsKey;

    @GetMapping("/get")
    @Operation(summary = "获得交易配置")
    public CommonResult<AppTradeConfigRespVO> getTradeConfig() {
        TradeConfigDO config = ObjUtil.defaultIfNull(tradeConfigService.getTradeConfig(), new TradeConfigDO());
        return success(TradeConfigConvert.INSTANCE.convert02(config).setTencentLbsKey(tencentLbsKey));
    }

}
