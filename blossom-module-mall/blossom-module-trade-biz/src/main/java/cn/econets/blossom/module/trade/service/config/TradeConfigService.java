package cn.econets.blossom.module.trade.service.config;

import cn.econets.blossom.module.trade.controller.admin.config.vo.TradeConfigSaveReqVO;
import cn.econets.blossom.module.trade.dal.dataobject.config.TradeConfigDO;

import javax.validation.Valid;

/**
 * 交易中心配置 Service 接口
 *
 */
public interface TradeConfigService {

    /**
     * 更新交易中心配置
     *
     * @param updateReqVO 更新信息
     */
    void saveTradeConfig(@Valid TradeConfigSaveReqVO updateReqVO);

    /**
     * 获得交易中心配置
     *
     * @return 交易中心配置
     */
    TradeConfigDO getTradeConfig();

}
