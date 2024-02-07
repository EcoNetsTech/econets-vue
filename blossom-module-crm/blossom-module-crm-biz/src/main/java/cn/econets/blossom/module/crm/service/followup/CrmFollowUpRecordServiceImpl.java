package cn.econets.blossom.module.crm.service.followup;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.framework.common.util.object.BeanUtils;
import cn.econets.blossom.module.crm.controller.admin.followup.vo.CrmFollowUpRecordPageReqVO;
import cn.econets.blossom.module.crm.controller.admin.followup.vo.CrmFollowUpRecordSaveReqVO;
import cn.econets.blossom.module.crm.dal.dataobject.followup.CrmFollowUpRecordDO;
import cn.econets.blossom.module.crm.dal.dataobject.permission.CrmPermissionDO;
import cn.econets.blossom.module.crm.dal.mysql.followup.CrmFollowUpRecordMapper;
import cn.econets.blossom.module.crm.enums.permission.CrmPermissionLevelEnum;
import cn.econets.blossom.module.crm.framework.permission.core.annotations.CrmPermission;
import cn.econets.blossom.module.crm.service.business.CrmBusinessService;
import cn.econets.blossom.module.crm.service.business.bo.CrmBusinessUpdateFollowUpReqBO;
import cn.econets.blossom.module.crm.service.contact.CrmContactService;
import cn.econets.blossom.module.crm.service.contact.bo.CrmContactUpdateFollowUpReqBO;
import cn.econets.blossom.module.crm.service.followup.handle.CrmFollowUpHandler;
import cn.econets.blossom.module.crm.service.permission.CrmPermissionService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static cn.econets.blossom.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.econets.blossom.framework.common.util.collection.CollectionUtils.anyMatch;
import static cn.econets.blossom.framework.common.util.collection.CollectionUtils.convertList;
import static cn.econets.blossom.module.crm.enums.ErrorCodeConstants.FOLLOW_UP_RECORD_DELETE_DENIED;
import static cn.econets.blossom.module.crm.enums.ErrorCodeConstants.FOLLOW_UP_RECORD_NOT_EXISTS;

/**
 * 跟进记录 Service 实现类
 *
 */
@Service
@Validated
public class CrmFollowUpRecordServiceImpl implements CrmFollowUpRecordService {

    @Resource
    private CrmFollowUpRecordMapper crmFollowUpRecordMapper;

    @Resource
    private CrmPermissionService permissionService;
    @Resource
    private List<CrmFollowUpHandler> followUpHandlers;
    @Resource
    private CrmBusinessService businessService;
    @Resource
    private CrmContactService contactService;

    @Override
    @CrmPermission(bizTypeValue = "#createReqVO.bizType", bizId = "#createReqVO.bizId", level = CrmPermissionLevelEnum.WRITE)
    public Long createFollowUpRecord(CrmFollowUpRecordSaveReqVO createReqVO) {
        // 创建更进记录
        CrmFollowUpRecordDO followUpRecord = BeanUtils.toBean(createReqVO, CrmFollowUpRecordDO.class);
        crmFollowUpRecordMapper.insert(followUpRecord);

        LocalDateTime now = LocalDateTime.now();
        // 2. 更新 bizId 对应的记录；
        followUpHandlers.forEach(handler -> handler.execute(followUpRecord, now));
        // 3.1 更新 contactIds 对应的记录
        if (CollUtil.isNotEmpty(createReqVO.getContactIds())) {
            // TODO 可以用链式设置哈
            contactService.updateContactFollowUpBatch(convertList(createReqVO.getContactIds(), contactId -> {
                CrmContactUpdateFollowUpReqBO crmContactUpdateFollowUpReqBO = new CrmContactUpdateFollowUpReqBO();
                crmContactUpdateFollowUpReqBO.setId(contactId).setContactNextTime(followUpRecord.getNextTime())
                        .setContactLastTime(now).setContactLastContent(followUpRecord.getContent());
                return crmContactUpdateFollowUpReqBO;
            }));
        }
        // 3.2 需要更新 businessIds、contactIds 对应的记录
        if (CollUtil.isNotEmpty(createReqVO.getBusinessIds())) {
            businessService.updateContactFollowUpBatch(convertList(createReqVO.getBusinessIds(), businessId -> {
                CrmBusinessUpdateFollowUpReqBO crmBusinessUpdateFollowUpReqBO = new CrmBusinessUpdateFollowUpReqBO();
                crmBusinessUpdateFollowUpReqBO.setId(businessId).setContactNextTime(followUpRecord.getNextTime())
                        .setContactLastTime(now).setContactLastContent(followUpRecord.getContent());
                return crmBusinessUpdateFollowUpReqBO;
            }));
        }
        return followUpRecord.getId();
    }

    @Override
    public void deleteFollowUpRecord(Long id, Long userId) {
        // 校验存在
        CrmFollowUpRecordDO followUpRecord = validateFollowUpRecordExists(id);
        // 校验权限
        List<CrmPermissionDO> permissionList = permissionService.getPermissionListByBiz(
                followUpRecord.getBizType(), followUpRecord.getBizId());
        boolean condition = anyMatch(permissionList, permission ->
                ObjUtil.equal(permission.getUserId(), userId) && ObjUtil.equal(permission.getLevel(), CrmPermissionLevelEnum.OWNER.getLevel()));
        if (!condition) {
            throw exception(FOLLOW_UP_RECORD_DELETE_DENIED);
        }

        // 删除
        crmFollowUpRecordMapper.deleteById(id);
    }

    private CrmFollowUpRecordDO validateFollowUpRecordExists(Long id) {
        CrmFollowUpRecordDO followUpRecord = crmFollowUpRecordMapper.selectById(id);
        if (followUpRecord == null) {
            throw exception(FOLLOW_UP_RECORD_NOT_EXISTS);
        }
        return followUpRecord;
    }

    @Override
    public CrmFollowUpRecordDO getFollowUpRecord(Long id) {
        return crmFollowUpRecordMapper.selectById(id);
    }


    @Override
    @CrmPermission(bizTypeValue = "#pageReqVO.bizType", bizId = "#pageReqVO.bizId", level = CrmPermissionLevelEnum.READ)
    public PageResult<CrmFollowUpRecordDO> getFollowUpRecordPage(CrmFollowUpRecordPageReqVO pageReqVO) {
        return crmFollowUpRecordMapper.selectPage(pageReqVO);
    }

}
