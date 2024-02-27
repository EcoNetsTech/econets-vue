package cn.econets.blossom.module.crm.convert.business;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.module.crm.controller.admin.business.vo.type.CrmBusinessStatusTypeRespVO;
import cn.econets.blossom.module.crm.dal.dataobject.business.CrmBusinessStatusDO;
import cn.econets.blossom.module.crm.dal.dataobject.business.CrmBusinessStatusTypeDO;
import cn.econets.blossom.module.system.api.dept.dto.DeptRespDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

import static cn.econets.blossom.framework.common.util.collection.CollectionUtils.convertList;
import static cn.econets.blossom.framework.common.util.collection.CollectionUtils.convertMap;

// TODO @lzxhqs：看看是不是用 BeanUtils 替代了
/**
 * 商机状态类型 Convert
 *
 */
@Mapper
public interface CrmBusinessStatusTypeConvert {

    CrmBusinessStatusTypeConvert INSTANCE = Mappers.getMapper(CrmBusinessStatusTypeConvert.class);

    CrmBusinessStatusTypeRespVO convert(CrmBusinessStatusTypeDO bean);

    PageResult<CrmBusinessStatusTypeRespVO> convertPage(PageResult<CrmBusinessStatusTypeDO> page);

    default PageResult<CrmBusinessStatusTypeRespVO> convertPage(PageResult<CrmBusinessStatusTypeDO> page, List<DeptRespDTO> deptList) {
        PageResult<CrmBusinessStatusTypeRespVO> pageResult = convertPage(page);
        // 拼接关联字段
        Map<Long, String> deptMap = convertMap(deptList, DeptRespDTO::getId, DeptRespDTO::getName);
        pageResult.getList().forEach(type -> type.setDeptNames(convertList(type.getDeptIds(), deptMap::get)));
        return pageResult;
    }

    default CrmBusinessStatusTypeRespVO convert(CrmBusinessStatusTypeDO bean, List<CrmBusinessStatusDO> statusList) {
        return convert(bean).setStatusList(statusList);
    }

}
