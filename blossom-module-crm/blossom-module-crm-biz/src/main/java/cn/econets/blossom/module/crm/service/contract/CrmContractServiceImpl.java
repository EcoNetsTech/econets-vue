package cn.econets.blossom.module.crm.service.contract;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.framework.common.util.number.MoneyUtils;
import cn.econets.blossom.framework.common.util.object.BeanUtils;
import cn.econets.blossom.framework.common.util.object.ObjectUtils;
import cn.econets.blossom.module.bpm.api.listener.dto.BpmResultListenerRespDTO;
import cn.econets.blossom.module.bpm.api.task.BpmProcessInstanceApi;
import cn.econets.blossom.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import cn.econets.blossom.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import cn.econets.blossom.module.crm.controller.admin.contract.vo.CrmContractPageReqVO;
import cn.econets.blossom.module.crm.controller.admin.contract.vo.CrmContractSaveReqVO;
import cn.econets.blossom.module.crm.controller.admin.contract.vo.CrmContractTransferReqVO;
import cn.econets.blossom.module.crm.convert.contract.CrmContractConvert;
import cn.econets.blossom.module.crm.dal.dataobject.contract.CrmContractDO;
import cn.econets.blossom.module.crm.dal.dataobject.contract.CrmContractProductDO;
import cn.econets.blossom.module.crm.dal.dataobject.product.CrmProductDO;
import cn.econets.blossom.module.crm.dal.mysql.contract.CrmContractMapper;
import cn.econets.blossom.module.crm.dal.mysql.contract.CrmContractProductMapper;
import cn.econets.blossom.module.crm.enums.common.CrmAuditStatusEnum;
import cn.econets.blossom.module.crm.enums.common.CrmBizTypeEnum;
import cn.econets.blossom.module.crm.enums.permission.CrmPermissionLevelEnum;
import cn.econets.blossom.module.crm.framework.permission.core.annotations.CrmPermission;
import cn.econets.blossom.module.crm.service.business.CrmBusinessService;
import cn.econets.blossom.module.crm.service.business.bo.CrmBusinessUpdateProductReqBO;
import cn.econets.blossom.module.crm.service.customer.CrmCustomerService;
import cn.econets.blossom.module.crm.service.followup.bo.CrmUpdateFollowUpReqBO;
import cn.econets.blossom.module.crm.service.permission.CrmPermissionService;
import cn.econets.blossom.module.crm.service.permission.bo.CrmPermissionCreateReqBO;
import cn.econets.blossom.module.crm.service.product.CrmProductService;
import cn.econets.blossom.module.system.api.user.AdminUserApi;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.impl.DiffParseFunction;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static cn.econets.blossom.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.econets.blossom.framework.common.util.collection.CollectionUtils.*;
import static cn.econets.blossom.module.crm.enums.ErrorCodeConstants.*;
import static cn.econets.blossom.module.crm.enums.LogRecordConstants.*;
import static cn.econets.blossom.module.system.enums.ErrorCodeConstants.USER_NOT_EXISTS;

/**
 * CRM 合同 Service 实现类
 *
 */
@Service
@Validated
public class CrmContractServiceImpl implements CrmContractService {

    /**
     * BPM 合同审批流程标识
     */
    public static final String CONTRACT_APPROVE = "contract-approve";

    @Resource
    private CrmContractMapper contractMapper;
    @Resource
    private CrmContractProductMapper contractProductMapper;

    @Resource
    private CrmPermissionService crmPermissionService;
    @Resource
    private CrmProductService productService;
    @Resource
    private CrmCustomerService customerService;
    @Resource
    private CrmBusinessService businessService;

    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_CONTRACT_TYPE, subType = CRM_CONTRACT_CREATE_SUB_TYPE, bizNo = "{{#contract.id}}",
            success = CRM_CONTRACT_CREATE_SUCCESS)
    public Long createContract(CrmContractSaveReqVO createReqVO, Long userId) {
        validateRelationDataExists(createReqVO);
        // 1.1 插入合同
        CrmContractDO contract = BeanUtils.toBean(createReqVO, CrmContractDO.class).setId(null);
        contractMapper.insert(contract);
        // 1.2 插入合同关联商品
        if (CollUtil.isNotEmpty(createReqVO.getProductItems())) { // 如果有的话
            List<CrmContractProductDO> productList = convertContractProductList(createReqVO, contract.getId());
            contractProductMapper.insertBatch(productList);
            // 更新合同商品总金额
            contractMapper.updateById(new CrmContractDO().setId(contract.getId()).setProductPrice(
                    getSumValue(productList, CrmContractProductDO::getTotalPrice, Integer::sum)));
            // 如果存在合同关联了商机则更新商机商品关联
            if (contract.getBusinessId() != null) {
                businessService.updateBusinessProduct(new CrmBusinessUpdateProductReqBO().setId(contract.getBusinessId())
                        .setProductItems(BeanUtils.toBean(createReqVO.getProductItems(), CrmBusinessUpdateProductReqBO.CrmBusinessProductItem.class)));
            }
        }

        // 2. 创建数据权限
        crmPermissionService.createPermission(new CrmPermissionCreateReqBO().setUserId(userId)
                .setBizType(CrmBizTypeEnum.CRM_CONTRACT.getType()).setBizId(contract.getId())
                .setLevel(CrmPermissionLevelEnum.OWNER.getLevel()));

        // 3. 记录操作日志上下文
        LogRecordContext.putVariable("contract", contract);
        return contract.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_CONTRACT_TYPE, subType = CRM_CONTRACT_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = CRM_CONTRACT_UPDATE_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_CONTRACT, bizId = "#updateReqVO.id", level = CrmPermissionLevelEnum.WRITE)
    public void updateContract(CrmContractSaveReqVO updateReqVO) {
        Assert.notNull(updateReqVO.getId(), "合同编号不能为空");
        // 1.1 校验存在
        CrmContractDO contract = validateContractExists(updateReqVO.getId());
        // 1.2 只有草稿、审批中，可以编辑；
        if (!ObjectUtils.equalsAny(contract.getAuditStatus(), CrmAuditStatusEnum.DRAFT.getStatus(),
                CrmAuditStatusEnum.PROCESS.getStatus())) {
            throw exception(CONTRACT_UPDATE_FAIL_EDITING_PROHIBITED);
        }
        validateRelationDataExists(updateReqVO);

        // 2.1 更新合同
        CrmContractDO updateObj = BeanUtils.toBean(updateReqVO, CrmContractDO.class);
        contractMapper.updateById(updateObj);
        // 2.2 更新合同关联商品
        updateContractProduct(updateReqVO, updateObj.getId());

        // 3. 记录操作日志上下文
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, BeanUtils.toBean(contract, CrmContractSaveReqVO.class));
        LogRecordContext.putVariable("contractName", contract.getName());
    }

    private void updateContractProduct(CrmContractSaveReqVO updateReqVO, Long contractId) {
        if (CollUtil.isEmpty(updateReqVO.getProductItems())) {
            return;
        }
        List<CrmContractProductDO> newProductList = convertContractProductList(updateReqVO, contractId);
        List<CrmContractProductDO> oldProductList = contractProductMapper.selectListByContractId(contractId);
        List<List<CrmContractProductDO>> diffList = diffList(oldProductList, newProductList, (oldObj, newObj) -> {
            boolean match = ObjUtil.equal(oldObj.getProductId(), newObj.getProductId());
            if (match) {
                newObj.setId(oldObj.getId()); // 设置一下老的编号更新时需要使用
            }
            return match;
        });
        if (CollUtil.isNotEmpty(diffList.get(0))) {
            contractProductMapper.insertBatch(diffList.get(0));
        }
        if (CollUtil.isNotEmpty(diffList.get(1))) {
            contractProductMapper.updateBatch(diffList.get(1));
        }
        if (CollUtil.isNotEmpty(diffList.get(2))) {
            contractProductMapper.deleteBatchIds(convertList(diffList.get(2), CrmContractProductDO::getId));
        }
    }

    // TODO @合同待定：缺一个取消合同的接口；只有草稿、审批中可以取消；CrmAuditStatusEnum

    private List<CrmContractProductDO> convertContractProductList(CrmContractSaveReqVO reqVO, Long contractId) {
        // 校验商品存在
        Set<Long> productIds = convertSet(reqVO.getProductItems(), CrmContractSaveReqVO.CrmContractProductItem::getId);
        List<CrmProductDO> productList = productService.getProductList(productIds);
        if (CollUtil.isEmpty(productIds) || productList.size() != productIds.size()) {
            throw exception(PRODUCT_NOT_EXISTS);
        }
        Map<Long, CrmProductDO> productMap = convertMap(productList, CrmProductDO::getId);
        return convertList(reqVO.getProductItems(), productItem -> {
            CrmProductDO product = productMap.get(productItem.getId());
            return BeanUtils.toBean(product, CrmContractProductDO.class)
                    .setId(null).setProductId(productItem.getId()).setContractId(contractId)
                    .setCount(productItem.getCount()).setDiscountPercent(productItem.getDiscountPercent())
                    .setTotalPrice(MoneyUtils.calculator(product.getPrice(), productItem.getCount(), productItem.getDiscountPercent()));
        });
    }

    /**
     * 校验关联数据是否存在
     *
     * @param reqVO 请求
     */
    private void validateRelationDataExists(CrmContractSaveReqVO reqVO) {
        // 1. 校验客户
        if (reqVO.getCustomerId() != null && customerService.getCustomer(reqVO.getCustomerId()) == null) {
            throw exception(CUSTOMER_NOT_EXISTS);
        }
        // 2. 校验负责人
        if (reqVO.getOwnerUserId() != null && adminUserApi.getUser(reqVO.getOwnerUserId()) == null) {
            throw exception(USER_NOT_EXISTS);
        }
        // 3. 如果有关联商机，则需要校验存在
        if (reqVO.getBusinessId() != null && businessService.getBusiness(reqVO.getBusinessId()) == null) {
            throw exception(BUSINESS_NOT_EXISTS);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_CONTRACT_TYPE, subType = CRM_CONTRACT_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_CONTRACT_DELETE_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_CONTRACT, bizId = "#id", level = CrmPermissionLevelEnum.OWNER)
    public void deleteContract(Long id) {
        // TODO @合同待定：如果被 CrmReceivableDO 所使用，则不允许删除
        // 校验存在
        CrmContractDO contract = validateContractExists(id);
        // 删除
        contractMapper.deleteById(id);
        // 删除数据权限
        crmPermissionService.deletePermission(CrmBizTypeEnum.CRM_CONTRACT.getType(), id);

        // 记录操作日志上下文
        LogRecordContext.putVariable("contractName", contract.getName());
    }

    private CrmContractDO validateContractExists(Long id) {
        CrmContractDO contract = contractMapper.selectById(id);
        if (contract == null) {
            throw exception(CONTRACT_NOT_EXISTS);
        }
        return contract;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_CONTRACT_TYPE, subType = CRM_CONTRACT_TRANSFER_SUB_TYPE, bizNo = "{{#reqVO.id}}",
            success = CRM_CONTRACT_TRANSFER_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_CONTRACT, bizId = "#reqVO.id", level = CrmPermissionLevelEnum.OWNER)
    public void transferContract(CrmContractTransferReqVO reqVO, Long userId) {
        // 1. 校验合同是否存在
        CrmContractDO contract = validateContractExists(reqVO.getId());

        // 2.1 数据权限转移
        crmPermissionService.transferPermission(
                CrmContractConvert.INSTANCE.convert(reqVO, userId).setBizType(CrmBizTypeEnum.CRM_CONTRACT.getType()));
        // 2.2 设置负责人
        contractMapper.updateOwnerUserIdById(reqVO.getId(), reqVO.getNewOwnerUserId());

        // 3. 记录转移日志
        LogRecordContext.putVariable("contract", contract);
    }

    @Override
    public void updateContractFollowUp(CrmUpdateFollowUpReqBO contractUpdateFollowUpReqBO) {
        contractMapper.updateById(BeanUtils.toBean(contractUpdateFollowUpReqBO, CrmContractDO.class).setId(contractUpdateFollowUpReqBO.getBizId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_CONTRACT_TYPE, subType = CRM_CONTRACT_SUBMIT_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_CONTRACT_SUBMIT_SUCCESS)
    public void submitContract(Long id, Long userId) {
        // 1. 校验合同是否在审批
        CrmContractDO contract = validateContractExists(id);
        if (ObjUtil.notEqual(contract.getAuditStatus(), CrmAuditStatusEnum.DRAFT.getStatus())) {
            throw exception(CONTRACT_SUBMIT_FAIL_NOT_DRAFT);
        }

        // 2. 创建合同审批流程实例
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(userId, new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(CONTRACT_APPROVE).setBusinessKey(String.valueOf(id)));

        // 3. 更新合同工作流编号
        contractMapper.updateById(new CrmContractDO().setId(id).setProcessInstanceId(processInstanceId)
                .setAuditStatus(CrmAuditStatusEnum.PROCESS.getStatus()));

        // 3. 记录日志
        LogRecordContext.putVariable("contractName", contract.getName());
    }

    @Override
    public void updateContractAuditStatus(BpmResultListenerRespDTO event) {
        // 判断下状态是否符合预期
        if (!isEndResult(event.getResult())) {
            return;
        }
        // 状态转换
        if (ObjUtil.equal(event.getResult(), BpmProcessInstanceResultEnum.APPROVE.getResult())) {
            event.setResult(CrmAuditStatusEnum.APPROVE.getStatus());
        }
        if (ObjUtil.equal(event.getResult(), BpmProcessInstanceResultEnum.REJECT.getResult())) {
            event.setResult(CrmAuditStatusEnum.REJECT.getStatus());
        }
        if (ObjUtil.equal(event.getResult(), BpmProcessInstanceResultEnum.CANCEL.getResult())) {
            event.setResult(CrmAuditStatusEnum.CANCEL.getStatus());
        }
        // 更新合同状态
        contractMapper.updateById(new CrmContractDO().setId(Long.parseLong(event.getBusinessKey()))
                .setAuditStatus(event.getResult()));
    }

    /**
     * 判断该结果是否处于 End 最终结果
     *
     * @param result 结果
     * @return 是否
     */
    public static boolean isEndResult(Integer result) {
        return ObjectUtils.equalsAny(result, BpmProcessInstanceResultEnum.APPROVE.getResult(),
                BpmProcessInstanceResultEnum.REJECT.getResult(), BpmProcessInstanceResultEnum.CANCEL.getResult());
    }

    //======================= 查询相关 =======================

    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_CONTRACT, bizId = "#id", level = CrmPermissionLevelEnum.READ)
    public CrmContractDO getContract(Long id) {
        return contractMapper.selectById(id);
    }

    @Override
    public List<CrmContractDO> getContractList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return contractMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<CrmContractDO> getContractPage(CrmContractPageReqVO pageReqVO, Long userId) {
        return contractMapper.selectPage(pageReqVO, userId);
    }

    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_CUSTOMER, bizId = "#pageReqVO.customerId", level = CrmPermissionLevelEnum.READ)
    public PageResult<CrmContractDO> getContractPageByCustomerId(CrmContractPageReqVO pageReqVO) {
        return contractMapper.selectPageByCustomerId(pageReqVO);
    }

    @Override
    public Long getContractCountByContactId(Long contactId) {
        return contractMapper.selectCountByContactId(contactId);
    }

    @Override
    public Long getContractCountByCustomerId(Long customerId) {
        return contractMapper.selectCount(CrmContractDO::getCustomerId, customerId);
    }

    @Override
    public Long getContractCountByBusinessId(Long businessId) {
        return contractMapper.selectCountByBusinessId(businessId);
    }

    @Override
    public List<CrmContractProductDO> getContractProductListByContractId(Long contactId) {
        return contractProductMapper.selectListByContractId(contactId);
    }

    // TODO @合同待定：需要新增一个 ContractConfigDO 表，合同配置，重点是到期提醒；
}
