package cn.econets.blossom.module.crm.service.followup.handle;

import cn.hutool.core.util.ObjUtil;
import cn.econets.blossom.module.crm.dal.dataobject.followup.CrmFollowUpRecordDO;
import cn.econets.blossom.module.crm.enums.common.CrmBizTypeEnum;
import cn.econets.blossom.module.crm.service.clue.CrmClueService;
import cn.econets.blossom.module.crm.service.clue.bo.CrmClueUpdateFollowUpReqBO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * CRM 线索的 {@link CrmFollowUpHandler} 实现类
 *
 */
@Component
public class CrmClueFollowUpHandler implements CrmFollowUpHandler {

    @Resource
    private CrmClueService clueService;

    @Override
    public void execute(CrmFollowUpRecordDO followUpRecord, LocalDateTime now) {
        if (ObjUtil.notEqual(CrmBizTypeEnum.CRM_LEADS.getType(), followUpRecord.getBizType())) {
            return;
        }

        // 更新线索跟进信息
        CrmClueUpdateFollowUpReqBO clueUpdateFollowUpReqBO = new CrmClueUpdateFollowUpReqBO();
        clueUpdateFollowUpReqBO.setId(followUpRecord.getBizId()).setContactNextTime(followUpRecord.getNextTime())
                .setContactLastTime(now).setContactLastContent(followUpRecord.getContent());
        clueService.updateClueFollowUp(clueUpdateFollowUpReqBO);
    }

}
