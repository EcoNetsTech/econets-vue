package cn.econets.blossom.module.crm.convert.contract;

import cn.hutool.core.collection.CollUtil;
import cn.econets.blossom.framework.common.util.collection.CollectionUtils;
import cn.econets.blossom.framework.common.util.object.BeanUtils;
import cn.econets.blossom.module.crm.controller.admin.contract.vo.CrmContractRespVO;
import cn.econets.blossom.module.crm.controller.admin.contract.vo.CrmContractTransferReqVO;
import cn.econets.blossom.module.crm.dal.dataobject.business.CrmBusinessDO;
import cn.econets.blossom.module.crm.dal.dataobject.contact.CrmContactDO;
import cn.econets.blossom.module.crm.dal.dataobject.contract.CrmContractDO;
import cn.econets.blossom.module.crm.dal.dataobject.contract.CrmContractProductDO;
import cn.econets.blossom.module.crm.dal.dataobject.customer.CrmCustomerDO;
import cn.econets.blossom.module.crm.dal.dataobject.product.CrmProductDO;
import cn.econets.blossom.module.crm.service.permission.bo.CrmPermissionTransferReqBO;
import cn.econets.blossom.module.system.api.user.dto.AdminUserRespDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

import static cn.econets.blossom.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.econets.blossom.framework.common.util.collection.MapUtils.findAndThen;

/**
 * 合同 Convert
 *
 */
@Mapper
public interface CrmContractConvert {

    CrmContractConvert INSTANCE = Mappers.getMapper(CrmContractConvert.class);

    @Mapping(target = "bizId", source = "reqVO.id")
    CrmPermissionTransferReqBO convert(CrmContractTransferReqVO reqVO, Long userId);

    default List<CrmContractRespVO> convertList(List<CrmContractDO> contractList, Map<Long, AdminUserRespDTO> userMap,
                                                List<CrmCustomerDO> customerList, Map<Long, CrmContactDO> contactMap,
                                                Map<Long, CrmBusinessDO> businessMap, Map<Long, CrmContractProductDO> contractProductMap,
                                                List<CrmProductDO> productList) {
        List<CrmContractRespVO> respVOList = BeanUtils.toBean(contractList, CrmContractRespVO.class);
        // 拼接关联字段
        Map<Long, CrmCustomerDO> customerMap = convertMap(customerList, CrmCustomerDO::getId);
        respVOList.forEach(contract -> {
            findAndThen(userMap, contract.getOwnerUserId(), user -> contract.setOwnerUserName(user.getNickname()));
            findAndThen(userMap, Long.parseLong(contract.getCreator()), user -> contract.setCreatorName(user.getNickname()));
            findAndThen(userMap, contract.getSignUserId(), user -> contract.setSignUserName(user.getNickname()));
            findAndThen(customerMap, contract.getCustomerId(), customer -> contract.setCustomerName(customer.getName()));
            findAndThen(contactMap, contract.getContactId(), contact -> contract.setContactName(contact.getName()));
            findAndThen(businessMap, contract.getBusinessId(), business -> contract.setBusinessName(business.getName()));
        });
        if (CollUtil.isNotEmpty(respVOList) && respVOList.size() == 1) {
            setContractRespVOProductItems(respVOList.get(0), contractProductMap, productList);
        }
        return respVOList;
    }

    default void setContractRespVOProductItems(CrmContractRespVO respVO, Map<Long, CrmContractProductDO> contractProductMap,
                                               List<CrmProductDO> productList) {
        respVO.setProductItems(CollectionUtils.convertList(productList, product -> {
            CrmContractRespVO.CrmContractProductItemRespVO productItemRespVO = BeanUtils.toBean(product, CrmContractRespVO.CrmContractProductItemRespVO.class);
            findAndThen(contractProductMap, product.getId(), contractProduct ->
                    productItemRespVO.setCount(contractProduct.getCount()).setDiscountPercent(contractProduct.getDiscountPercent()));
            return productItemRespVO;
        }));
    }

}
