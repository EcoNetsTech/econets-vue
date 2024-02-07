package cn.econets.blossom.module.trade.framework.delivery.core.client.dto;

import cn.econets.blossom.module.trade.dal.dataobject.delivery.DeliveryExpressDO;
import lombok.Data;

/**
 * 快递轨迹的查询 Req DTO
 *
 */
@Data
public class ExpressTrackQueryReqDTO {

    /**
     * 快递公司编码
     *
     * 对应 {@link DeliveryExpressDO#getCode()}
     */
    private String expressCode;

    /**
     * 发货快递单号
     */
    private String logisticsNo;

    /**
     * 收、寄件人的电话号码
     */
    private String phone;

}
