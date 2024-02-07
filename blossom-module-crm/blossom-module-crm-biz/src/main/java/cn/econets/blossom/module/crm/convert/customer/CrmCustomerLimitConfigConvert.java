package cn.econets.blossom.module.crm.convert.customer;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.framework.common.util.collection.CollectionUtils;
import cn.econets.blossom.framework.common.util.object.BeanUtils;
import cn.econets.blossom.module.crm.controller.admin.customer.vo.limitconfig.CrmCustomerLimitConfigRespVO;
import cn.econets.blossom.module.crm.dal.dataobject.customer.CrmCustomerLimitConfigDO;
import cn.econets.blossom.module.system.api.dept.dto.DeptRespDTO;
import cn.econets.blossom.module.system.api.user.dto.AdminUserRespDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

/**
 * 客户限制配置 Convert
 *
 */
@Mapper
public interface CrmCustomerLimitConfigConvert {

    CrmCustomerLimitConfigConvert INSTANCE = Mappers.getMapper(CrmCustomerLimitConfigConvert.class);

    default PageResult<CrmCustomerLimitConfigRespVO> convertPage(
            PageResult<CrmCustomerLimitConfigDO> pageResult,
            Map<Long, AdminUserRespDTO> userMap, Map<Long, DeptRespDTO> deptMap) {
        List<CrmCustomerLimitConfigRespVO> list = CollectionUtils.convertList(pageResult.getList(),
                limitConfig -> convert(limitConfig, userMap, deptMap));
        return new PageResult<>(list, pageResult.getTotal());
    }

    default CrmCustomerLimitConfigRespVO convert(CrmCustomerLimitConfigDO limitConfig,
                                                 Map<Long, AdminUserRespDTO> userMap, Map<Long, DeptRespDTO> deptMap) {
        CrmCustomerLimitConfigRespVO limitConfigVO = BeanUtils.toBean(limitConfig, CrmCustomerLimitConfigRespVO.class);
        limitConfigVO.setUsers(CollectionUtils.convertList(limitConfigVO.getUserIds(), userMap::get));
        limitConfigVO.setDepts(CollectionUtils.convertList(limitConfigVO.getDeptIds(), deptMap::get));
        return limitConfigVO;
    }

}
