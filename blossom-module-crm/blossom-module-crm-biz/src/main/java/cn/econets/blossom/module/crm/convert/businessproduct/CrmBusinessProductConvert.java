package cn.econets.blossom.module.crm.convert.businessproduct;

import cn.econets.blossom.module.crm.controller.admin.business.vo.product.CrmBusinessProductSaveReqVO;
import cn.econets.blossom.module.crm.dal.dataobject.business.CrmBusinessProductDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

// TODO 看看是不是用 BeanUtils 替代了
/**
 * @version 1.0
 * @title CrmBusinessProductConvert
 * @description
 */
@Mapper
public interface CrmBusinessProductConvert {
    CrmBusinessProductConvert INSTANCE = Mappers.getMapper(CrmBusinessProductConvert.class);

    CrmBusinessProductDO convert(CrmBusinessProductSaveReqVO product);
}
