package cn.econets.blossom.module.trade.service.order;

import cn.econets.blossom.module.trade.convert.order.TradeOrderLogConvert;
import cn.econets.blossom.module.trade.dal.dataobject.order.TradeOrderLogDO;
import cn.econets.blossom.module.trade.dal.mysql.order.TradeOrderLogMapper;
import cn.econets.blossom.module.trade.service.order.bo.TradeOrderLogCreateReqBO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 交易下单日志 Service 实现类
 *
 */
@Service
public class TradeOrderLogServiceImpl implements TradeOrderLogService {

    @Resource
    private TradeOrderLogMapper tradeOrderLogMapper;

    @Override
    public void createOrderLog(TradeOrderLogCreateReqBO createReqBO) {
        tradeOrderLogMapper.insert(TradeOrderLogConvert.INSTANCE.convert(createReqBO));
    }

    @Override
    public List<TradeOrderLogDO> getOrderLogListByOrderId(Long orderId) {
        return tradeOrderLogMapper.selectListByOrderId(orderId);
    }

}
