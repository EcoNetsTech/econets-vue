package cn.econets.blossom.module.trade.dal.mysql.aftersale;

import cn.econets.blossom.framework.mybatis.core.mapper.BaseMapperX;
import cn.econets.blossom.module.trade.dal.dataobject.aftersale.AfterSaleLogDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AfterSaleLogMapper extends BaseMapperX<AfterSaleLogDO> {

    default List<AfterSaleLogDO> selectListByAfterSaleId(Long afterSaleId) {
        return selectList(AfterSaleLogDO::getAfterSaleId, afterSaleId);
    }

}
