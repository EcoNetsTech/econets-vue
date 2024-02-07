package cn.econets.blossom.module.crm.convert.clue;

import cn.econets.blossom.module.crm.controller.admin.clue.vo.CrmClueTransferReqVO;
import cn.econets.blossom.module.crm.service.permission.bo.CrmPermissionTransferReqBO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * 线索 Convert
 *
 */
@Mapper
public interface CrmClueConvert {

    CrmClueConvert INSTANCE = Mappers.getMapper(CrmClueConvert.class);

    @Mapping(target = "bizId", source = "reqVO.id")
    CrmPermissionTransferReqBO convert(CrmClueTransferReqVO reqVO, Long userId);

}
