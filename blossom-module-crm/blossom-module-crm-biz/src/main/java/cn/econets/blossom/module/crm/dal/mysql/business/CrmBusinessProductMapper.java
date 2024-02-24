package cn.econets.blossom.module.crm.dal.mysql.business;


import cn.econets.blossom.framework.mybatis.core.mapper.BaseMapperX;
import cn.econets.blossom.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.econets.blossom.module.crm.dal.dataobject.business.CrmBusinessProductDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 商机产品 Mapper
 *
 */
@Mapper
public interface CrmBusinessProductMapper extends BaseMapperX<CrmBusinessProductDO> {

    // TODO @puhui999：用不到的方法，看看是不是删除哈
    default void deleteByBusinessId(Long getBusinessId) { // TODO @lzxhqs：第一个方法，和类之间最好空一行；
        delete(CrmBusinessProductDO::getBusinessId, getBusinessId);
    }

    default CrmBusinessProductDO selectByBusinessId(Long getBusinessId) {
        return selectOne(CrmBusinessProductDO::getBusinessId, getBusinessId);
    }

    default List<CrmBusinessProductDO> selectListByBusinessId(Long businessId) {
        // TODO @puhui999：可以简化，selectList(CrmBusinessProductDO::getBusinessId, businessId)
        return selectList(new LambdaQueryWrapperX<CrmBusinessProductDO>().eq(CrmBusinessProductDO::getBusinessId, businessId));
    }

}
