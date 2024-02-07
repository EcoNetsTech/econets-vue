package cn.econets.blossom.module.trade.service.aftersale;


import cn.econets.blossom.module.trade.dal.dataobject.aftersale.AfterSaleLogDO;
import cn.econets.blossom.module.trade.service.aftersale.bo.AfterSaleLogCreateReqBO;

import java.util.List;

/**
 * 交易售后日志 Service 接口
 *
 */
public interface AfterSaleLogService {

    /**
     * 创建售后日志
     *
     * @param createReqBO 日志记录
     */
    void createAfterSaleLog(AfterSaleLogCreateReqBO createReqBO);

    /**
     * 获取售后日志
     *
     * @param afterSaleId 售后编号
     * @return 售后日志
     */
    List<AfterSaleLogDO> getAfterSaleLogList(Long afterSaleId);

}
