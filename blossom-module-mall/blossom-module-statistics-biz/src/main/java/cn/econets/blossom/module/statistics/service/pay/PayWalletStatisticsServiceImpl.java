package cn.econets.blossom.module.statistics.service.pay;

import cn.econets.blossom.module.pay.enums.wallet.PayWalletBizTypeEnum;
import cn.econets.blossom.module.pay.enums.order.PayOrderStatusEnum;
import cn.econets.blossom.module.pay.enums.refund.PayRefundStatusEnum;
import cn.econets.blossom.module.statistics.dal.mysql.pay.PayWalletStatisticsMapper;
import cn.econets.blossom.module.statistics.service.pay.bo.RechargeSummaryRespBO;
import cn.econets.blossom.module.statistics.service.trade.bo.WalletSummaryRespBO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 钱包的统计 Service 实现类
 *
 */
@Service
@Validated
public class PayWalletStatisticsServiceImpl implements PayWalletStatisticsService {

    @Resource
    private PayWalletStatisticsMapper payWalletStatisticsMapper;

    @Override
    public WalletSummaryRespBO getWalletSummary(LocalDateTime beginTime, LocalDateTime endTime) {
        WalletSummaryRespBO paySummary = payWalletStatisticsMapper.selectRechargeSummaryByPayTimeBetween(
                beginTime, endTime, true);
        WalletSummaryRespBO refundSummary = payWalletStatisticsMapper.selectRechargeSummaryByRefundTimeBetween(
                beginTime, endTime, PayRefundStatusEnum.SUCCESS.getStatus());
        Integer walletPayPrice = payWalletStatisticsMapper.selectPriceSummaryByBizTypeAndCreateTimeBetween(
                beginTime, endTime, PayWalletBizTypeEnum.PAYMENT.getType());
        // 拼接
        paySummary.setWalletPayPrice(walletPayPrice)
                .setRechargeRefundCount(refundSummary.getRechargeRefundCount())
                .setRechargeRefundPrice(refundSummary.getRechargeRefundPrice());
        return paySummary;
    }

    @Override
    public RechargeSummaryRespBO getUserRechargeSummary(LocalDateTime beginTime, LocalDateTime endTime) {
        return payWalletStatisticsMapper.selectRechargeSummaryGroupByWalletId(beginTime, endTime, true);
    }

    @Override
    public Integer getRechargePriceSummary() {
        return payWalletStatisticsMapper.selectRechargePriceSummary(PayOrderStatusEnum.SUCCESS.getStatus());
    }

}
