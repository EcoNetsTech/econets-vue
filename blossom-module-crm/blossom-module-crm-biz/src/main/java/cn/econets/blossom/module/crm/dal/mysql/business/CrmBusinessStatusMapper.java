package cn.econets.blossom.module.crm.dal.mysql.business;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.framework.mybatis.core.mapper.BaseMapperX;
import cn.econets.blossom.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.econets.blossom.module.crm.controller.admin.business.vo.status.CrmBusinessStatusPageReqVO;
import cn.econets.blossom.module.crm.controller.admin.business.vo.status.CrmBusinessStatusQueryVO;
import cn.econets.blossom.module.crm.dal.dataobject.business.CrmBusinessStatusDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 商机状态 Mapper
 *
 */
@Mapper
public interface CrmBusinessStatusMapper extends BaseMapperX<CrmBusinessStatusDO> {

    default PageResult<CrmBusinessStatusDO> selectPage(CrmBusinessStatusPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CrmBusinessStatusDO>()
                .orderByDesc(CrmBusinessStatusDO::getId));
    }

    default List<CrmBusinessStatusDO> selectList(CrmBusinessStatusQueryVO queryVO) {
        return selectList(new LambdaQueryWrapperX<CrmBusinessStatusDO>()
                .eqIfPresent(CrmBusinessStatusDO::getTypeId, queryVO.getTypeId())
                .inIfPresent(CrmBusinessStatusDO::getId, queryVO.getIdList())
                .orderByDesc(CrmBusinessStatusDO::getId));
    }

    default int delete(Long typeId) {
        return delete(CrmBusinessStatusDO::getTypeId, typeId);
    }

}
