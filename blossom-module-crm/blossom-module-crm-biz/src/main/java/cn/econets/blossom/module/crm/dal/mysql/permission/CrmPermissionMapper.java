package cn.econets.blossom.module.crm.dal.mysql.permission;

import cn.econets.blossom.framework.mybatis.core.mapper.BaseMapperX;
import cn.econets.blossom.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.econets.blossom.module.crm.dal.dataobject.permission.CrmPermissionDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * crm 数据权限 mapper
 *
 */
@Mapper
public interface CrmPermissionMapper extends BaseMapperX<CrmPermissionDO> {

    default CrmPermissionDO selectByBizTypeAndBizIdByUserId(Integer bizType, Long bizId, Long userId) {
        return selectOne(new LambdaQueryWrapperX<CrmPermissionDO>()
                .eq(CrmPermissionDO::getBizType, bizType)
                .eq(CrmPermissionDO::getBizId, bizId)
                .eq(CrmPermissionDO::getUserId, userId));
    }

    default List<CrmPermissionDO> selectByBizTypeAndBizId(Integer bizType, Long bizId) {
        return selectList(new LambdaQueryWrapperX<CrmPermissionDO>()
                .eq(CrmPermissionDO::getBizType, bizType)
                .eq(CrmPermissionDO::getBizId, bizId));
    }

    default List<CrmPermissionDO> selectByBizTypeAndBizIds(Integer bizType, Collection<Long> bizIds) {
        return selectList(new LambdaQueryWrapperX<CrmPermissionDO>()
                .eq(CrmPermissionDO::getBizType, bizType)
                .in(CrmPermissionDO::getBizId, bizIds));
    }

    default List<CrmPermissionDO> selectListByBizTypeAndUserId(Integer bizType, Long userId) {
        return selectList(new LambdaQueryWrapperX<CrmPermissionDO>()
                .eq(CrmPermissionDO::getBizType, bizType)
                .eq(CrmPermissionDO::getUserId, userId));
    }

    default List<CrmPermissionDO> selectListByBizTypeAndBizIdAndLevel(Integer bizType, Long bizId, Integer level) {
        return selectList(new LambdaQueryWrapperX<CrmPermissionDO>()
                .eq(CrmPermissionDO::getBizType, bizType)
                .eq(CrmPermissionDO::getBizId, bizId)
                .eq(CrmPermissionDO::getLevel, level));
    }

    default CrmPermissionDO selectByIdAndUserId(Long id, Long userId) {
        return selectOne(new LambdaQueryWrapperX<CrmPermissionDO>()
                .eq(CrmPermissionDO::getId, id).eq(CrmPermissionDO::getUserId, userId));
    }

    default int deletePermission(Integer bizType, Long bizId) {
        return delete(new LambdaQueryWrapperX<CrmPermissionDO>()
                .eq(CrmPermissionDO::getBizType, bizType)
                .eq(CrmPermissionDO::getBizId, bizId));
    }

    default Long selectListByBiz(Collection<Integer> bizTypes, Collection<Long> bizIds, Collection<Long> userIds) {
        return selectCount(new LambdaQueryWrapperX<CrmPermissionDO>()
                .in(CrmPermissionDO::getBizType, bizTypes)
                .in(CrmPermissionDO::getBizId, bizIds)
                .in(CrmPermissionDO::getUserId, userIds));
    }

}
