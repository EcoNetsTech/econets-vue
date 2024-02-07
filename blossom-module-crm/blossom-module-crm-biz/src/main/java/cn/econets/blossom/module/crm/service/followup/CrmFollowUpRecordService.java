package cn.econets.blossom.module.crm.service.followup;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.module.crm.controller.admin.followup.vo.CrmFollowUpRecordPageReqVO;
import cn.econets.blossom.module.crm.controller.admin.followup.vo.CrmFollowUpRecordSaveReqVO;
import cn.econets.blossom.module.crm.dal.dataobject.followup.CrmFollowUpRecordDO;

import javax.validation.Valid;

/**
 * 跟进记录 Service 接口
 *
 */
public interface CrmFollowUpRecordService {

    /**
     * 创建跟进记录 (数据权限基于 bizType、 bizId)
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createFollowUpRecord(@Valid CrmFollowUpRecordSaveReqVO createReqVO);

    /**
     * 删除跟进记录 (数据权限基于 bizType、 bizId)
     *
     * @param id     编号
     * @param userId 用户编号
     */
    void deleteFollowUpRecord(Long id, Long userId);

    /**
     * 获得跟进记录
     *
     * @param id 编号
     * @return 跟进记录
     */
    CrmFollowUpRecordDO getFollowUpRecord(Long id);

    /**
     * 获得跟进记录分页 (数据权限基于 bizType、 bizId)
     *
     * @param pageReqVO 分页查询
     * @return 跟进记录分页
     */
    PageResult<CrmFollowUpRecordDO> getFollowUpRecordPage(CrmFollowUpRecordPageReqVO pageReqVO);

}
