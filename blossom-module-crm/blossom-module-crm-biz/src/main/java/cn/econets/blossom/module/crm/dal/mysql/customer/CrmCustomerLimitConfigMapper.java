package cn.econets.blossom.module.crm.dal.mysql.customer;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.framework.mybatis.core.mapper.BaseMapperX;
import cn.econets.blossom.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.econets.blossom.module.crm.controller.admin.customer.vo.limitconfig.CrmCustomerLimitConfigPageReqVO;
import cn.econets.blossom.module.crm.dal.dataobject.customer.CrmCustomerLimitConfigDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 客户限制配置 Mapper
 *
 */
@Mapper
public interface CrmCustomerLimitConfigMapper extends BaseMapperX<CrmCustomerLimitConfigDO> {

    default PageResult<CrmCustomerLimitConfigDO> selectPage(CrmCustomerLimitConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CrmCustomerLimitConfigDO>()
                .eqIfPresent(CrmCustomerLimitConfigDO::getType, reqVO.getType())
                .orderByDesc(CrmCustomerLimitConfigDO::getId));
    }

    default List<CrmCustomerLimitConfigDO> selectListByTypeAndUserIdAndDeptId(
            Integer type, Long userId, Long deptId) {
        LambdaQueryWrapperX<CrmCustomerLimitConfigDO> query = new LambdaQueryWrapperX<CrmCustomerLimitConfigDO>()
                .eq(CrmCustomerLimitConfigDO::getType, type);
        query.apply("FIND_IN_SET({0}, user_ids) > 0", userId);
        if (deptId != null) {
            query.apply("FIND_IN_SET({0}, dept_ids) > 0", deptId);
        }
        return selectList(query);
    }

}
