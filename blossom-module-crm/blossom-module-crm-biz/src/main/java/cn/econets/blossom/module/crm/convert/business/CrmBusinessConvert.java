package cn.econets.blossom.module.crm.convert.business;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.framework.common.util.collection.CollectionUtils;
import cn.econets.blossom.framework.common.util.object.BeanUtils;
import cn.econets.blossom.module.crm.controller.admin.business.vo.business.CrmBusinessRespVO;
import cn.econets.blossom.module.crm.controller.admin.business.vo.business.CrmBusinessTransferReqVO;
import cn.econets.blossom.module.crm.dal.dataobject.business.CrmBusinessDO;
import cn.econets.blossom.module.crm.dal.dataobject.business.CrmBusinessStatusDO;
import cn.econets.blossom.module.crm.dal.dataobject.business.CrmBusinessStatusTypeDO;
import cn.econets.blossom.module.crm.dal.dataobject.customer.CrmCustomerDO;
import cn.econets.blossom.module.crm.service.followup.bo.CrmUpdateFollowUpReqBO;
import cn.econets.blossom.module.crm.service.permission.bo.CrmPermissionTransferReqBO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

import static cn.econets.blossom.framework.common.util.collection.CollectionUtils.convertMap;

/**
 * 商机 Convert
 *
 */
@Mapper
public interface CrmBusinessConvert {

    CrmBusinessConvert INSTANCE = Mappers.getMapper(CrmBusinessConvert.class);

    @Mapping(target = "bizId", source = "reqVO.id")
    CrmPermissionTransferReqBO convert(CrmBusinessTransferReqVO reqVO, Long userId);

    default PageResult<CrmBusinessRespVO> convertPage(PageResult<CrmBusinessDO> pageResult, List<CrmCustomerDO> customerList,
                                                      List<CrmBusinessStatusTypeDO> statusTypeList, List<CrmBusinessStatusDO> statusList) {
        PageResult<CrmBusinessRespVO> voPageResult = BeanUtils.toBean(pageResult, CrmBusinessRespVO.class);
        // 拼接关联字段
        Map<Long, String> customerMap = convertMap(customerList, CrmCustomerDO::getId, CrmCustomerDO::getName);
        Map<Long, String> statusTypeMap = convertMap(statusTypeList, CrmBusinessStatusTypeDO::getId, CrmBusinessStatusTypeDO::getName);
        Map<Long, String> statusMap = convertMap(statusList, CrmBusinessStatusDO::getId, CrmBusinessStatusDO::getName);
        voPageResult.getList().forEach(type -> type
                .setCustomerName(customerMap.get(type.getCustomerId()))
                .setStatusTypeName(statusTypeMap.get(type.getStatusTypeId()))
                .setStatusName(statusMap.get(type.getStatusId())));
        return voPageResult;
    }

    @Mapping(target = "id", source = "reqBO.bizId")
    CrmBusinessDO convert(CrmUpdateFollowUpReqBO reqBO);

    default List<CrmBusinessDO> convertList(List<CrmUpdateFollowUpReqBO> list) {
        return CollectionUtils.convertList(list, INSTANCE::convert);
    }

}
