package cn.econets.blossom.module.statistics.service.trade.bo;

import lombok.Data;

/**
 * 售后统计 Response DTO
 *
 */
@Data
public class AfterSaleSummaryRespBO {

    /**
     * 退款订单数
     */
    private Integer afterSaleCount;
    /**
     * 总退款金额，单位：分
     */
    private Integer afterSaleRefundPrice;

}
