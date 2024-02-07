package cn.econets.blossom.module.crm.service.followup.handle;

import cn.hutool.core.util.ObjUtil;
import cn.econets.blossom.module.crm.dal.dataobject.followup.CrmFollowUpRecordDO;
import cn.econets.blossom.module.crm.enums.common.CrmBizTypeEnum;
import cn.econets.blossom.module.crm.service.customer.CrmCustomerService;
import cn.econets.blossom.module.crm.service.customer.bo.CrmCustomerUpdateFollowUpReqBO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * CRM 客户的 {@link CrmFollowUpHandler} 实现类
 *
 */
@Component
public class CrmCustomerFollowUpHandler implements CrmFollowUpHandler {

    @Resource
    private CrmCustomerService customerService;

    @Override
    public void execute(CrmFollowUpRecordDO followUpRecord, LocalDateTime now) {
        if (ObjUtil.notEqual(CrmBizTypeEnum.CRM_CUSTOMER.getType(), followUpRecord.getBizType())) {
            return;
        }

        // 更新客户跟进信息
        CrmCustomerUpdateFollowUpReqBO customerUpdateFollowUpReqBO = new CrmCustomerUpdateFollowUpReqBO();
        customerUpdateFollowUpReqBO.setId(followUpRecord.getBizId()).setContactNextTime(followUpRecord.getNextTime())
                .setContactLastTime(now).setContactLastContent(followUpRecord.getContent());
        customerService.updateCustomerFollowUp(customerUpdateFollowUpReqBO);
    }

}
