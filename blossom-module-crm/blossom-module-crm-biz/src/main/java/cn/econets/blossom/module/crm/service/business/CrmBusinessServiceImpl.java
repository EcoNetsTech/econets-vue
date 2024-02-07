package cn.econets.blossom.module.crm.service.business;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.framework.common.util.object.BeanUtils;
import cn.econets.blossom.module.crm.controller.admin.business.vo.business.CrmBusinessPageReqVO;
import cn.econets.blossom.module.crm.controller.admin.business.vo.business.CrmBusinessSaveReqVO;
import cn.econets.blossom.module.crm.controller.admin.business.vo.business.CrmBusinessTransferReqVO;
import cn.econets.blossom.module.crm.controller.admin.business.vo.product.CrmBusinessProductSaveReqVO;
import cn.econets.blossom.module.crm.convert.business.CrmBusinessConvert;
import cn.econets.blossom.module.crm.convert.businessproduct.CrmBusinessProductConvert;
import cn.econets.blossom.module.crm.dal.dataobject.business.CrmBusinessDO;
import cn.econets.blossom.module.crm.dal.dataobject.business.CrmBusinessProductDO;
import cn.econets.blossom.module.crm.dal.dataobject.contact.CrmContactBusinessDO;
import cn.econets.blossom.module.crm.dal.dataobject.contract.CrmContractDO;
import cn.econets.blossom.module.crm.dal.mysql.business.CrmBusinessMapper;
import cn.econets.blossom.module.crm.dal.mysql.business.CrmBusinessProductMapper;
import cn.econets.blossom.module.crm.dal.mysql.contactbusinesslink.CrmContactBusinessMapper;
import cn.econets.blossom.module.crm.dal.mysql.contract.CrmContractMapper;
import cn.econets.blossom.module.crm.enums.common.CrmBizTypeEnum;
import cn.econets.blossom.module.crm.enums.permission.CrmPermissionLevelEnum;
import cn.econets.blossom.module.crm.framework.permission.core.annotations.CrmPermission;
import cn.econets.blossom.module.crm.service.business.bo.CrmBusinessUpdateFollowUpReqBO;
import cn.econets.blossom.module.crm.service.contact.CrmContactBusinessService;
import cn.econets.blossom.module.crm.service.permission.CrmPermissionService;
import cn.econets.blossom.module.crm.service.permission.bo.CrmPermissionCreateReqBO;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.impl.DiffParseFunction;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static cn.econets.blossom.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.econets.blossom.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.econets.blossom.module.crm.enums.ErrorCodeConstants.BUSINESS_CONTRACT_EXISTS;
import static cn.econets.blossom.module.crm.enums.ErrorCodeConstants.BUSINESS_NOT_EXISTS;
import static cn.econets.blossom.module.crm.enums.LogRecordConstants.*;

/**
 * 商机 Service 实现类
 *
 */
@Service
@Validated
public class CrmBusinessServiceImpl implements CrmBusinessService {

    @Resource
    private CrmBusinessMapper businessMapper;

    @Resource
    private CrmBusinessProductMapper businessProductMapper;
    // TODO 不直接调用这个 mapper，要调用对方的 service；每个业务独立收敛
    @Resource
    private CrmContractMapper contractMapper;

    // TODO 不直接调用这个 mapper，要调用对方的 service；每个业务独立收敛
    @Resource
    private CrmContactBusinessMapper contactBusinessMapper;
    @Resource
    private CrmPermissionService permissionService;
    @Resource
    private CrmContactBusinessService contactBusinessService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_BUSINESS_TYPE, subType = CRM_BUSINESS_CREATE_SUB_TYPE, bizNo = "{{#business.id}}",
            success = CRM_BUSINESS_CREATE_SUCCESS)
    public Long createBusiness(CrmBusinessSaveReqVO createReqVO, Long userId) {
        createReqVO.setId(null);
        // 1. 插入商机
        CrmBusinessDO business = BeanUtils.toBean(createReqVO, CrmBusinessDO.class)
                .setOwnerUserId(userId);
        businessMapper.insert(business);
        // TODO 商机待定：插入商机与产品的关联表；校验商品存在
        // TODO lzxhqs：新增时，是不是不用调用这个方法哈；
        verifyCrmBusinessProduct(business.getId());
        // TODO 用 CollUtils.isNotEmpty；
        if (!createReqVO.getProducts().isEmpty()) {
            createBusinessProducts(createReqVO.getProducts(), business.getId());
        }
        // TODO 商机待定：在联系人的详情页，如果直接【新建商机】，则需要关联下。这里要搞个 CrmContactBusinessDO 表
        createContactBusiness(business.getId(), createReqVO.getContactId());

        // 2. 创建数据权限
        // 设置当前操作的人为负责人
        permissionService.createPermission(new CrmPermissionCreateReqBO().setBizType(CrmBizTypeEnum.CRM_BUSINESS.getType())
                .setBizId(business.getId()).setUserId(userId).setLevel(CrmPermissionLevelEnum.OWNER.getLevel()));

        // 4. 记录操作日志上下文
        LogRecordContext.putVariable("business", business);
        return business.getId();
    }

    // TODO CrmContactBusinessService 调用这个；这样逻辑才能收敛哈；
    /**
     * @param businessId 商机id
     * @param contactId  联系人id
     * @throws
     * @description 联系人与商机的关联
     */
    private void createContactBusiness(Long businessId, Long contactId) {
        CrmContactBusinessDO contactBusiness = new CrmContactBusinessDO();
        contactBusiness.setBusinessId(businessId);
        contactBusiness.setContactId(contactId);
        contactBusinessMapper.insert(contactBusiness);

    }

    // TODO 这个方法注释格式不对；删除@description，然后把 插入商机产品关联表 作为方法注释；
    /**
     * @param products 产品集合
     * @description 插入商机产品关联表
     */
    private void createBusinessProducts(List<CrmBusinessProductSaveReqVO> products, Long businessId) {
        // TODO 可以用 CollectionUtils.convertList；
        List<CrmBusinessProductDO> list = new ArrayList<>();
        for (CrmBusinessProductSaveReqVO product : products) {
            CrmBusinessProductDO businessProductDO = CrmBusinessProductConvert.INSTANCE.convert(product);
            businessProductDO.setBusinessId(businessId);
            list.add(businessProductDO);
        }
        businessProductMapper.insertBatch(list);
    }

    /**
     * @param id businessId
     * @description 校验管理的产品存在则删除
     */
    private void verifyCrmBusinessProduct(Long id) {
        CrmBusinessProductDO businessProductDO = businessProductMapper.selectByBusinessId(id);
        if (businessProductDO != null) {
            //通过商机Id删除
            businessProductMapper.deleteByBusinessId(id);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_BUSINESS_TYPE, subType = CRM_BUSINESS_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = CRM_BUSINESS_UPDATE_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_BUSINESS, bizId = "#updateReqVO.id", level = CrmPermissionLevelEnum.WRITE)
    public void updateBusiness(CrmBusinessSaveReqVO updateReqVO) {
        // 1. 校验存在
        CrmBusinessDO oldBusiness = validateBusinessExists(updateReqVO.getId());

        // 2. 更新商机
        CrmBusinessDO updateObj = BeanUtils.toBean(updateReqVO, CrmBusinessDO.class);
        businessMapper.updateById(updateObj);
        // TODO 商机待定：插入商机与产品的关联表；校验商品存在
        // TODO 更新时，可以调用 CollectionUtils 的 diffList，尽量避免这种先删除再插入；而是新增的插入、变更的更新，没的删除；不然这个表每次更新，会多好多数据；
        verifyCrmBusinessProduct(updateReqVO.getId());
        if (!updateReqVO.getProducts().isEmpty()) {
            createBusinessProducts(updateReqVO.getProducts(), updateReqVO.getId());
        }

        // TODO 如果状态发生变化，插入商机状态变更记录表
        // 3. 记录操作日志上下文
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, BeanUtils.toBean(oldBusiness, CrmBusinessSaveReqVO.class));
        LogRecordContext.putVariable("businessName", oldBusiness.getName());
    }

    @Override
    public void updateContactFollowUpBatch(List<CrmBusinessUpdateFollowUpReqBO> updateFollowUpReqBOList) {
        businessMapper.updateBatch(BeanUtils.toBean(updateFollowUpReqBOList, CrmBusinessDO.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_BUSINESS_TYPE, subType = CRM_BUSINESS_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_BUSINESS_DELETE_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_BUSINESS, bizId = "#id", level = CrmPermissionLevelEnum.OWNER)
    public void deleteBusiness(Long id) {
        // 校验存在
        CrmBusinessDO business = validateBusinessExists(id);
        // TODO 需要校验有没关联合同。CrmContractDO 的 businessId 字段
        validateContractExists(id);

        // 删除
        businessMapper.deleteById(id);
        // 删除数据权限
        permissionService.deletePermission(CrmBizTypeEnum.CRM_BUSINESS.getType(), id);

        // 记录操作日志上下文
        LogRecordContext.putVariable("businessName", business.getName());
    }

    /**
     * @param businessId 商机id
     * @throws
     * @description 删除校验合同是关联合同
     */
    private void validateContractExists(Long businessId) {
        CrmContractDO contract = contractMapper.selectByBizId(businessId);
        if (contract != null) {
            throw exception(BUSINESS_CONTRACT_EXISTS);
        }
    }

    private CrmBusinessDO validateBusinessExists(Long id) {
        CrmBusinessDO crmBusiness = businessMapper.selectById(id);
        if (crmBusiness == null) {
            throw exception(BUSINESS_NOT_EXISTS);
        }
        return crmBusiness;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_BUSINESS_TYPE, subType = CRM_BUSINESS_TRANSFER_SUB_TYPE, bizNo = "{{#reqVO.id}}",
            success = CRM_BUSINESS_TRANSFER_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_BUSINESS, bizId = "#reqVO.id", level = CrmPermissionLevelEnum.OWNER)
    public void transferBusiness(CrmBusinessTransferReqVO reqVO, Long userId) {
        // 1 校验商机是否存在
        CrmBusinessDO business = validateBusinessExists(reqVO.getId());

        // 2.1 数据权限转移
        permissionService.transferPermission(
                CrmBusinessConvert.INSTANCE.convert(reqVO, userId).setBizType(CrmBizTypeEnum.CRM_BUSINESS.getType()));
        // 2.2 设置新的负责人
        businessMapper.updateOwnerUserIdById(reqVO.getId(), reqVO.getNewOwnerUserId());

        // 记录操作日志上下文
        LogRecordContext.putVariable("business", business);
    }

    //======================= 查询相关 =======================

    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_BUSINESS, bizId = "#id", level = CrmPermissionLevelEnum.READ)
    public CrmBusinessDO getBusiness(Long id) {
        return businessMapper.selectById(id);
    }

    @Override
    public List<CrmBusinessDO> getBusinessList(Collection<Long> ids, Long userId) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return businessMapper.selectBatchIds(ids, userId);
    }

    @Override
    public List<CrmBusinessDO> getBusinessList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return businessMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<CrmBusinessDO> getBusinessPage(CrmBusinessPageReqVO pageReqVO, Long userId) {
        return businessMapper.selectPage(pageReqVO, userId);
    }

    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_CUSTOMER, bizId = "#pageReqVO.customerId", level = CrmPermissionLevelEnum.READ)
    public PageResult<CrmBusinessDO> getBusinessPageByCustomerId(CrmBusinessPageReqVO pageReqVO) {
        return businessMapper.selectPageByCustomerId(pageReqVO);
    }

    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_CONTACT, bizId = "#pageReqVO.contactId", level = CrmPermissionLevelEnum.READ)
    public PageResult<CrmBusinessDO> getBusinessPageByContact(CrmBusinessPageReqVO pageReqVO) {
        // 1. 查询关联的商机编号
        List<CrmContactBusinessDO> contactBusinessList = contactBusinessService.getContactBusinessListByContactId(
                pageReqVO.getContactId());
        if (CollUtil.isEmpty(contactBusinessList)) {
            return PageResult.empty();
        }
        // 2. 查询商机分页
        return businessMapper.selectPageByContactId(pageReqVO,
                convertSet(contactBusinessList, CrmContactBusinessDO::getBusinessId));
    }

    @Override
    public Long getBusinessCountByCustomerId(Long customerId) {
        return businessMapper.selectCount(CrmBusinessDO::getCustomerId, customerId);
    }

}
