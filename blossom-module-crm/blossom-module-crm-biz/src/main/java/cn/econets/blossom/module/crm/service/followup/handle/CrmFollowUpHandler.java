package cn.econets.blossom.module.crm.service.followup.handle;

import cn.econets.blossom.module.crm.dal.dataobject.followup.CrmFollowUpRecordDO;

import java.time.LocalDateTime;

/**
 * CRM 跟进信息处理器 handler 接口
 *
 */
public interface CrmFollowUpHandler {

    // TODO 需要考虑，下次联系时间为空；
    /**
     * 执行更新
     *
     * @param followUpRecord 跟进记录
     * @param now            跟进时间
     */
    void execute(CrmFollowUpRecordDO followUpRecord, LocalDateTime now);

}
