package cn.econets.blossom.module.crm.dal.mysql.contract;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.framework.mybatis.core.mapper.BaseMapperX;
import cn.econets.blossom.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.econets.blossom.framework.mybatis.core.query.MPJLambdaWrapperX;
import cn.econets.blossom.module.crm.controller.admin.contract.vo.CrmContractPageReqVO;
import cn.econets.blossom.module.crm.dal.dataobject.contract.CrmContractDO;
import cn.econets.blossom.module.crm.enums.common.CrmBizTypeEnum;
import cn.econets.blossom.module.crm.util.CrmQueryWrapperUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * CRM 合同 Mapper
 *
 */
@Mapper
public interface CrmContractMapper extends BaseMapperX<CrmContractDO> {

    default int updateOwnerUserIdById(Long id, Long ownerUserId) {
        return update(new LambdaUpdateWrapper<CrmContractDO>()
                .eq(CrmContractDO::getId, id)
                .set(CrmContractDO::getOwnerUserId, ownerUserId));
    }

    default PageResult<CrmContractDO> selectPageByCustomerId(CrmContractPageReqVO pageReqVO) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<CrmContractDO>()
                .eq(CrmContractDO::getCustomerId, pageReqVO.getCustomerId())
                .likeIfPresent(CrmContractDO::getNo, pageReqVO.getNo())
                .likeIfPresent(CrmContractDO::getName, pageReqVO.getName())
                .eqIfPresent(CrmContractDO::getCustomerId, pageReqVO.getCustomerId())
                .eqIfPresent(CrmContractDO::getBusinessId, pageReqVO.getBusinessId())
                .orderByDesc(CrmContractDO::getId));
    }

    default PageResult<CrmContractDO> selectPage(CrmContractPageReqVO pageReqVO, Long userId) {
        MPJLambdaWrapperX<CrmContractDO> mpjLambdaWrapperX = new MPJLambdaWrapperX<>();
        // 拼接数据权限的查询条件
        CrmQueryWrapperUtils.appendPermissionCondition(mpjLambdaWrapperX, CrmBizTypeEnum.CRM_CONTACT.getType(),
                CrmContractDO::getId, userId, pageReqVO.getSceneType(), Boolean.FALSE);
        // 拼接自身的查询条件
        mpjLambdaWrapperX.selectAll(CrmContractDO.class)
                .likeIfPresent(CrmContractDO::getNo, pageReqVO.getNo())
                .likeIfPresent(CrmContractDO::getName, pageReqVO.getName())
                .eqIfPresent(CrmContractDO::getCustomerId, pageReqVO.getCustomerId())
                .eqIfPresent(CrmContractDO::getBusinessId, pageReqVO.getBusinessId())
                .orderByDesc(CrmContractDO::getId);
        return selectJoinPage(pageReqVO, CrmContractDO.class, mpjLambdaWrapperX);
    }

    default List<CrmContractDO> selectBatchIds(Collection<Long> ids, Long userId) {
        MPJLambdaWrapperX<CrmContractDO> mpjLambdaWrapperX = new MPJLambdaWrapperX<>();
        // 构建数据权限连表条件
        CrmQueryWrapperUtils.appendPermissionCondition(mpjLambdaWrapperX, CrmBizTypeEnum.CRM_CONTACT.getType(), ids, userId);
        return selectJoinList(CrmContractDO.class, mpjLambdaWrapperX);
    }

    default Long selectCountByContactId(Long contactId) {
        return selectCount(CrmContractDO::getContactId, contactId);
    }
    default CrmContractDO selectByBizId(Long businessId) { // TODO 1）方法和方法之间要有空行；2）selectCountByBusinessId，一个是应该求数量，一个是不要缩写 BizId 可读性；
        return selectOne(CrmContractDO::getBusinessId, businessId);
    }

}
