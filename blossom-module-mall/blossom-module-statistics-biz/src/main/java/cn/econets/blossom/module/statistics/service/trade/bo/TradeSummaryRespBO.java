package cn.econets.blossom.module.statistics.service.trade.bo;

import lombok.Data;

/**
 * 交易统计 Resp BO
 *
 */
@Data
public class TradeSummaryRespBO {

    /**
     * 数量
     */
    private Integer count;

    /**
     * 合计
     */
    private Integer summary;

}
