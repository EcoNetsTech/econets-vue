package cn.econets.blossom.module.trade.convert.aftersale;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.module.member.api.user.dto.MemberUserRespDTO;
import cn.econets.blossom.module.pay.api.refund.dto.PayRefundCreateReqDTO;
import cn.econets.blossom.module.product.api.property.dto.ProductPropertyValueDetailRespDTO;
import cn.econets.blossom.module.trade.controller.admin.aftersale.vo.AfterSaleDetailRespVO;
import cn.econets.blossom.module.trade.controller.admin.aftersale.vo.AfterSaleRespPageItemVO;
import cn.econets.blossom.module.trade.controller.admin.aftersale.vo.log.AfterSaleLogRespVO;
import cn.econets.blossom.module.trade.controller.admin.base.member.user.MemberUserRespVO;
import cn.econets.blossom.module.trade.controller.admin.base.product.property.ProductPropertyValueDetailRespVO;
import cn.econets.blossom.module.trade.controller.admin.order.vo.TradeOrderBaseVO;
import cn.econets.blossom.module.trade.controller.app.aftersale.vo.AppAfterSaleCreateReqVO;
import cn.econets.blossom.module.trade.controller.app.aftersale.vo.AppAfterSaleRespVO;
import cn.econets.blossom.module.trade.dal.dataobject.aftersale.AfterSaleDO;
import cn.econets.blossom.module.trade.dal.dataobject.aftersale.AfterSaleLogDO;
import cn.econets.blossom.module.trade.dal.dataobject.order.TradeOrderDO;
import cn.econets.blossom.module.trade.dal.dataobject.order.TradeOrderItemDO;
import cn.econets.blossom.module.trade.framework.order.config.TradeOrderProperties;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

@Mapper
public interface AfterSaleConvert {

    AfterSaleConvert INSTANCE = Mappers.getMapper(AfterSaleConvert.class);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "creator", ignore = true),
            @Mapping(target = "updater", ignore = true),
    })
    AfterSaleDO convert(AppAfterSaleCreateReqVO createReqVO, TradeOrderItemDO tradeOrderItem);

    @Mappings({
            @Mapping(source = "afterSale.orderId", target = "merchantOrderId"),
            @Mapping(source = "afterSale.id", target = "merchantRefundId"),
            @Mapping(source = "afterSale.applyReason", target = "reason"),
            @Mapping(source = "afterSale.refundPrice", target = "price")
    })
    PayRefundCreateReqDTO convert(String userIp, AfterSaleDO afterSale,
                                  TradeOrderProperties orderProperties);

    MemberUserRespVO convert(MemberUserRespDTO bean);

    PageResult<AfterSaleRespPageItemVO> convertPage(PageResult<AfterSaleDO> page);

    default PageResult<AfterSaleRespPageItemVO> convertPage(PageResult<AfterSaleDO> pageResult,
                                                            Map<Long, MemberUserRespDTO> memberUsers) {
        PageResult<AfterSaleRespPageItemVO> voPageResult = convertPage(pageResult);
        // 处理会员
        voPageResult.getList().forEach(afterSale -> afterSale.setUser(
                convert(memberUsers.get(afterSale.getUserId()))));
        return voPageResult;
    }

    ProductPropertyValueDetailRespVO convert(ProductPropertyValueDetailRespDTO bean);

    AppAfterSaleRespVO convert(AfterSaleDO bean);

    PageResult<AppAfterSaleRespVO> convertPage02(PageResult<AfterSaleDO> page);

    default AfterSaleDetailRespVO convert(AfterSaleDO afterSale, TradeOrderDO order, TradeOrderItemDO orderItem,
                                          MemberUserRespDTO user, List<AfterSaleLogDO> logs) {
        AfterSaleDetailRespVO respVO = convert02(afterSale);
        // 处理用户信息
        respVO.setUser(convert(user));
        // 处理订单信息
        respVO.setOrder(convert(order));
        respVO.setOrderItem(convert02(orderItem));
        // 处理售后日志
        respVO.setLogs(convertList1(logs));
        return respVO;
    }

    List<AfterSaleLogRespVO> convertList1(List<AfterSaleLogDO> list);
    AfterSaleDetailRespVO convert02(AfterSaleDO bean);
    AfterSaleDetailRespVO.OrderItem convert02(TradeOrderItemDO bean);
    TradeOrderBaseVO convert(TradeOrderDO bean);

}
