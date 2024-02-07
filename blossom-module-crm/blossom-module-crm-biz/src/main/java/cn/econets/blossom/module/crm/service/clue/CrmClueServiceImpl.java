package cn.econets.blossom.module.crm.service.clue;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.framework.common.util.object.BeanUtils;
import cn.econets.blossom.module.crm.controller.admin.clue.vo.CrmCluePageReqVO;
import cn.econets.blossom.module.crm.controller.admin.clue.vo.CrmClueSaveReqVO;
import cn.econets.blossom.module.crm.controller.admin.clue.vo.CrmClueTransferReqVO;
import cn.econets.blossom.module.crm.controller.admin.clue.vo.CrmClueTransformReqVO;
import cn.econets.blossom.module.crm.controller.admin.customer.vo.CrmCustomerSaveReqVO;
import cn.econets.blossom.module.crm.convert.clue.CrmClueConvert;
import cn.econets.blossom.module.crm.dal.dataobject.clue.CrmClueDO;
import cn.econets.blossom.module.crm.dal.mysql.clue.CrmClueMapper;
import cn.econets.blossom.module.crm.enums.common.CrmBizTypeEnum;
import cn.econets.blossom.module.crm.enums.permission.CrmPermissionLevelEnum;
import cn.econets.blossom.module.crm.framework.permission.core.annotations.CrmPermission;
import cn.econets.blossom.module.crm.service.clue.bo.CrmClueUpdateFollowUpReqBO;
import cn.econets.blossom.module.crm.service.customer.CrmCustomerService;
import cn.econets.blossom.module.crm.service.permission.CrmPermissionService;
import cn.econets.blossom.module.system.api.user.AdminUserApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.econets.blossom.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.econets.blossom.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.econets.blossom.module.crm.enums.ErrorCodeConstants.*;
import static cn.econets.blossom.module.system.enums.ErrorCodeConstants.USER_NOT_EXISTS;

/**
 * 线索 Service 实现类
 *
 */
@Service
@Validated
public class CrmClueServiceImpl implements CrmClueService {

    @Resource
    private CrmClueMapper clueMapper;

    @Resource
    private CrmCustomerService customerService;

    @Resource
    private CrmPermissionService crmPermissionService;

    @Resource
    private AdminUserApi adminUserApi;

    @Override
    // TODO 补充相关几个方法的操作日志；
    public Long createClue(CrmClueSaveReqVO createReqVO) {
        // 校验关联数据
        validateRelationDataExists(createReqVO);

        // 插入
        CrmClueDO clue = BeanUtils.toBean(createReqVO, CrmClueDO.class);
        clueMapper.insert(clue);
        // 返回
        return clue.getId();
    }

    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_LEADS, bizId = "#updateReqVO.id", level = CrmPermissionLevelEnum.WRITE)
    public void updateClue(CrmClueSaveReqVO updateReqVO) {
        // 校验线索是否存在
        validateClueExists(updateReqVO.getId());
        // 校验关联数据
        validateRelationDataExists(updateReqVO);

        // 更新
        CrmClueDO updateObj = BeanUtils.toBean(updateReqVO, CrmClueDO.class);
        clueMapper.updateById(updateObj);
    }

    @Override
    public void updateClueFollowUp(CrmClueUpdateFollowUpReqBO clueUpdateFollowUpReqBO) {
        clueMapper.updateById(BeanUtils.toBean(clueUpdateFollowUpReqBO, CrmClueDO.class));
    }

    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_LEADS, bizId = "#id", level = CrmPermissionLevelEnum.OWNER)
    public void deleteClue(Long id) {
        // 校验存在
        validateClueExists(id);
        // 删除
        clueMapper.deleteById(id);
        // 删除数据权限
        crmPermissionService.deletePermission(CrmBizTypeEnum.CRM_LEADS.getType(), id);
    }

    private void validateClueExists(Long id) {
        if (clueMapper.selectById(id) == null) {
            throw exception(CLUE_NOT_EXISTS);
        }
    }

    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_LEADS, bizId = "#id", level = CrmPermissionLevelEnum.READ)
    public CrmClueDO getClue(Long id) {
        return clueMapper.selectById(id);
    }

    @Override
    public List<CrmClueDO> getClueList(Collection<Long> ids, Long userId) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return clueMapper.selectBatchIds(ids, userId);
    }

    @Override
    public PageResult<CrmClueDO> getCluePage(CrmCluePageReqVO pageReqVO, Long userId) {
        return clueMapper.selectPage(pageReqVO, userId);
    }

    @Override
    public void transferClue(CrmClueTransferReqVO reqVO, Long userId) {
        // 1 校验线索是否存在
        validateClueExists(reqVO.getId());

        // 2.1 数据权限转移
        crmPermissionService.transferPermission(CrmClueConvert.INSTANCE.convert(reqVO, userId).setBizType(CrmBizTypeEnum.CRM_LEADS.getType()));
        // 2.2 设置新的负责人
        clueMapper.updateOwnerUserIdById(reqVO.getId(), reqVO.getNewOwnerUserId());

        // 3. TODO 记录转移日志
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void translateCustomer(CrmClueTransformReqVO reqVO, Long userId) {
        // 校验线索都存在
        Set<Long> clueIds = reqVO.getIds();
        List<CrmClueDO> clues = getClueList(clueIds, userId);
        if (CollUtil.isEmpty(clues) || ObjectUtil.notEqual(clues.size(), clueIds.size())) {
            clueIds.removeAll(convertSet(clues, CrmClueDO::getId));
            // TODO 可以使用 StrUtil.join(",", clueIds) 简化这种常见操作
            throw exception(CLUE_ANY_CLUE_NOT_EXISTS, clueIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
        }

        // 过滤出未转化的客户
        // TODO 1）存在已经转化的，直接提示哈。避免操作的用户，以为都转化成功了；2）常见的过滤逻辑，可以使用 CollectionUtils.filterList()
        List<CrmClueDO> unTransformClues = clues.stream()
                .filter(clue -> ObjectUtil.notEqual(Boolean.TRUE, clue.getTransformStatus())).collect(Collectors.toList());
        // 传入的线索中包含已经转化的情况，抛出业务异常
        if (ObjectUtil.notEqual(clues.size(), unTransformClues.size())) {
            // TODO 可以使用 StrUtil.join(",", clueIds) 简化这种常见操作
            clueIds.removeAll(convertSet(unTransformClues, CrmClueDO::getId));
            throw exception(CLUE_ANY_CLUE_ALREADY_TRANSLATED, clueIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
        }

        // 遍历线索(未转化的线索)，创建对应的客户
        unTransformClues.forEach(clue -> {
            // 1. 创建客户
            CrmCustomerSaveReqVO customerSaveReqVO = BeanUtils.toBean(clue, CrmCustomerSaveReqVO.class).setId(null);
            Long customerId = customerService.createCustomer(customerSaveReqVO, userId);
            // TODO 如果有跟进记录，需要一起转过去；提问：艿艿这里是复制线索所有的跟进吗？还是直接把线索相关的跟进 bizType、bizId 全改为关联客户？
            // 2. 更新线索
            clueMapper.updateById(new CrmClueDO().setId(clue.getId())
                    .setTransformStatus(Boolean.TRUE).setCustomerId(customerId));
        });
    }

    private void validateRelationDataExists(CrmClueSaveReqVO reqVO) {
        // 校验负责人
        if (Objects.nonNull(reqVO.getOwnerUserId()) &&
                Objects.isNull(adminUserApi.getUser(reqVO.getOwnerUserId()))) {
            throw exception(USER_NOT_EXISTS);
        }
    }

}
