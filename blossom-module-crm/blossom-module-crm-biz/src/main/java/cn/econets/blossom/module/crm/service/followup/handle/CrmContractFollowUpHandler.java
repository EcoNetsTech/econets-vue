package cn.econets.blossom.module.crm.service.followup.handle;

import cn.hutool.core.util.ObjUtil;
import cn.econets.blossom.module.crm.dal.dataobject.followup.CrmFollowUpRecordDO;
import cn.econets.blossom.module.crm.enums.common.CrmBizTypeEnum;
import cn.econets.blossom.module.crm.service.contract.CrmContractService;
import cn.econets.blossom.module.crm.service.contract.bo.CrmContractUpdateFollowUpReqBO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * CRM 合同的 {@link CrmFollowUpHandler} 实现类
 *
 */
@Component
public class CrmContractFollowUpHandler implements CrmFollowUpHandler {

    @Resource
    private CrmContractService contractService;

    @Override
    public void execute(CrmFollowUpRecordDO followUpRecord, LocalDateTime now) {
        if (ObjUtil.notEqual(CrmBizTypeEnum.CRM_CONTRACT.getType(), followUpRecord.getBizType())) {
            return;
        }

        // 更新合同跟进信息
        CrmContractUpdateFollowUpReqBO contractUpdateFollowUpReqBO = new CrmContractUpdateFollowUpReqBO();
        contractUpdateFollowUpReqBO.setId(followUpRecord.getBizId()).setContactNextTime(followUpRecord.getNextTime())
                .setContactLastTime(now).setContactLastContent(followUpRecord.getContent());
        contractService.updateContractFollowUp(contractUpdateFollowUpReqBO);
    }

}
