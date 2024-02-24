package cn.econets.blossom.module.crm.convert.receivable;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.framework.common.util.object.BeanUtils;
import cn.econets.blossom.module.crm.controller.admin.receivable.vo.receivable.CrmReceivableCreateReqVO;
import cn.econets.blossom.module.crm.controller.admin.receivable.vo.receivable.CrmReceivableRespVO;
import cn.econets.blossom.module.crm.controller.admin.receivable.vo.receivable.CrmReceivableUpdateReqVO;
import cn.econets.blossom.module.crm.dal.dataobject.contract.CrmContractDO;
import cn.econets.blossom.module.crm.dal.dataobject.customer.CrmCustomerDO;
import cn.econets.blossom.module.crm.dal.dataobject.receivable.CrmReceivableDO;
import cn.econets.blossom.module.system.api.user.dto.AdminUserRespDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

import static cn.econets.blossom.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.econets.blossom.framework.common.util.collection.MapUtils.findAndThen;

/**
 * 回款 Convert
 *
 */
@Mapper
public interface CrmReceivableConvert {

    CrmReceivableConvert INSTANCE = Mappers.getMapper(CrmReceivableConvert.class);

    CrmReceivableDO convert(CrmReceivableCreateReqVO bean);

    CrmReceivableDO convert(CrmReceivableUpdateReqVO bean);

    CrmReceivableRespVO convert(CrmReceivableDO bean);

    default PageResult<CrmReceivableRespVO> convertPage(PageResult<CrmReceivableDO> pageResult, Map<Long, AdminUserRespDTO> userMap,
                                                        List<CrmCustomerDO> customerList, List<CrmContractDO> contractList) {
        PageResult<CrmReceivableRespVO> voPageResult = BeanUtils.toBean(pageResult, CrmReceivableRespVO.class);
        // 拼接关联字段
        Map<Long, CrmCustomerDO> customerMap = convertMap(customerList, CrmCustomerDO::getId);
        Map<Long, CrmContractDO> contractMap = convertMap(contractList, CrmContractDO::getId);
        voPageResult.getList().forEach(receivable -> {
            findAndThen(userMap, receivable.getOwnerUserId(), user -> receivable.setOwnerUserName(user.getNickname()));
            findAndThen(userMap, Long.parseLong(receivable.getCreator()), user -> receivable.setCreatorName(user.getNickname()));
            findAndThen(customerMap, receivable.getCustomerId(), customer -> receivable.setCustomerName(customer.getName()));
            findAndThen(contractMap, receivable.getContractId(), contract -> receivable.setContractNo(contract.getNo()));
        });
        return voPageResult;
    }

}
