package cn.econets.blossom.module.statistics.dal.mysql.pay;

import cn.econets.blossom.framework.mybatis.core.mapper.BaseMapperX;
import cn.econets.blossom.module.statistics.service.pay.bo.RechargeSummaryRespBO;
import cn.econets.blossom.module.statistics.service.trade.bo.WalletSummaryRespBO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * 支付钱包的统计 Mapper
 *
 */
@Mapper
@SuppressWarnings("rawtypes")
public interface PayWalletStatisticsMapper extends BaseMapperX {

    // TODO 已经 review；
    WalletSummaryRespBO selectRechargeSummaryByPayTimeBetween(@Param("beginTime") LocalDateTime beginTime,
                                                              @Param("endTime") LocalDateTime endTime,
                                                              @Param("payStatus") Boolean payStatus);

    // TODO 已经 review；
    WalletSummaryRespBO selectRechargeSummaryByRefundTimeBetween(@Param("beginTime") LocalDateTime beginTime,
                                                                 @Param("endTime") LocalDateTime endTime,
                                                                 @Param("refundStatus") Integer refundStatus);

    // TODO 已经 review；
    Integer selectPriceSummaryByBizTypeAndCreateTimeBetween(@Param("beginTime") LocalDateTime beginTime,
                                                            @Param("endTime") LocalDateTime endTime,
                                                            @Param("bizType") Integer bizType);

    // TODO 已经 review；
    RechargeSummaryRespBO selectRechargeSummaryGroupByWalletId(@Param("beginTime") LocalDateTime beginTime,
                                                               @Param("endTime") LocalDateTime endTime,
                                                               @Param("payStatus") Boolean payStatus);

    // TODO 已经 review；
    Integer selectRechargePriceSummary(@Param("payStatus") Integer payStatus);

}
