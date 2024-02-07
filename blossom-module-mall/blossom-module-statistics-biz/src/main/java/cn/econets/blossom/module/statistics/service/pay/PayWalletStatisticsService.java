package cn.econets.blossom.module.statistics.service.pay;

import cn.econets.blossom.module.statistics.service.pay.bo.RechargeSummaryRespBO;
import cn.econets.blossom.module.statistics.service.trade.bo.WalletSummaryRespBO;

import java.time.LocalDateTime;

/**
 * 钱包的统计 Service 接口
 *
 */
public interface PayWalletStatisticsService {

    // TODO 已经 review
    /**
     * 获取钱包统计
     *
     * @param beginTime 起始时间
     * @param endTime   截止时间
     * @return 钱包统计
     */
    WalletSummaryRespBO getWalletSummary(LocalDateTime beginTime, LocalDateTime endTime);

    // TODO 已经 review
    /**
     * 获取钱包充值统计
     *
     * @param beginTime 起始时间
     * @param endTime   截止时间
     * @return 钱包充值统计
     */
    RechargeSummaryRespBO getUserRechargeSummary(LocalDateTime beginTime, LocalDateTime endTime);

    // TODO 已经 review
    /**
     * 获取充值金额合计
     *
     * @return 充值金额合计
     */
    Integer getRechargePriceSummary();

}
