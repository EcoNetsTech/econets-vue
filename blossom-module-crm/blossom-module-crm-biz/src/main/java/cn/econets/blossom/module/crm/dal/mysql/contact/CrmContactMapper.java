package cn.econets.blossom.module.crm.dal.mysql.contact;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.framework.mybatis.core.mapper.BaseMapperX;
import cn.econets.blossom.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.econets.blossom.framework.mybatis.core.query.MPJLambdaWrapperX;
import cn.econets.blossom.module.crm.controller.admin.contact.vo.CrmContactPageReqVO;
import cn.econets.blossom.module.crm.dal.dataobject.contact.CrmContactDO;
import cn.econets.blossom.module.crm.enums.common.CrmBizTypeEnum;
import cn.econets.blossom.module.crm.util.CrmQueryWrapperUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * CRM 联系人 Mapper
 *
 */
@Mapper
public interface CrmContactMapper extends BaseMapperX<CrmContactDO> {

    default int updateOwnerUserIdById(Long id, Long ownerUserId) {
        return update(new LambdaUpdateWrapper<CrmContactDO>()
                .eq(CrmContactDO::getId, id)
                .set(CrmContactDO::getOwnerUserId, ownerUserId));
    }

    default int updateOwnerUserIdByCustomerId(Long customerId, Long ownerUserId) {
        return update(new LambdaUpdateWrapper<CrmContactDO>()
                .eq(CrmContactDO::getCustomerId, customerId)
                .set(CrmContactDO::getOwnerUserId, ownerUserId));
    }

    default PageResult<CrmContactDO> selectPageByCustomerId(CrmContactPageReqVO pageVO) {
        return selectPage(pageVO, new LambdaQueryWrapperX<CrmContactDO>()
                .eq(CrmContactDO::getCustomerId, pageVO.getCustomerId()) // 指定客户编号
                .likeIfPresent(CrmContactDO::getName, pageVO.getName())
                .eqIfPresent(CrmContactDO::getMobile, pageVO.getMobile())
                .eqIfPresent(CrmContactDO::getTelephone, pageVO.getTelephone())
                .eqIfPresent(CrmContactDO::getEmail, pageVO.getEmail())
                .eqIfPresent(CrmContactDO::getQq, pageVO.getQq())
                .eqIfPresent(CrmContactDO::getWechat, pageVO.getWechat())
                .orderByDesc(CrmContactDO::getId));
    }

    default PageResult<CrmContactDO> selectPage(CrmContactPageReqVO pageReqVO, Long userId) {
        MPJLambdaWrapperX<CrmContactDO> query = new MPJLambdaWrapperX<>();
        // 拼接数据权限的查询条件
        CrmQueryWrapperUtils.appendPermissionCondition(query, CrmBizTypeEnum.CRM_CONTACT.getType(),
                CrmContactDO::getId, userId, pageReqVO.getSceneType(), Boolean.FALSE);
        // 拼接自身的查询条件
        query.selectAll(CrmContactDO.class)
                .likeIfPresent(CrmContactDO::getName, pageReqVO.getName())
                .eqIfPresent(CrmContactDO::getMobile, pageReqVO.getMobile())
                .eqIfPresent(CrmContactDO::getTelephone, pageReqVO.getTelephone())
                .eqIfPresent(CrmContactDO::getEmail, pageReqVO.getEmail())
                .eqIfPresent(CrmContactDO::getQq, pageReqVO.getQq())
                .eqIfPresent(CrmContactDO::getWechat, pageReqVO.getWechat())
                .orderByDesc(CrmContactDO::getId);
        return selectJoinPage(pageReqVO, CrmContactDO.class, query);
    }

    default List<CrmContactDO> selectBatchIds(Collection<Long> ids, Long userId) {
        MPJLambdaWrapperX<CrmContactDO> query = new MPJLambdaWrapperX<>();
        // 拼接数据权限的查询条件
        CrmQueryWrapperUtils.appendPermissionCondition(query, CrmBizTypeEnum.CRM_CONTACT.getType(), ids, userId);
        return selectJoinList(CrmContactDO.class, query);
    }

}
