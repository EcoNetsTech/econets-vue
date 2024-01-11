package cn.econets.blossom.module.system.dal.mysql.tenant;

import cn.econets.blossom.framework.mybatis.core.mapper.BaseMapperX;
import cn.econets.blossom.module.system.dal.dataobject.tenant.TenantPackageMenuDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface TenantPackageMenuMapper extends BaseMapperX<TenantPackageMenuDO> {

    default List<TenantPackageMenuDO> selectListByTenantPackageId(Long tenantPackageId) {
        return selectList(TenantPackageMenuDO::getTenantPackageId, tenantPackageId);
    }

    default List<TenantPackageMenuDO> selectListByTenantPackageIds(Collection<Long> tenantPackageIds) {
        return selectList(TenantPackageMenuDO::getTenantPackageId, tenantPackageIds);
    }

    default List<TenantPackageMenuDO> selectListByMenuId(Long menuId) {
        return selectList(TenantPackageMenuDO::getMenuId, menuId);
    }

    default void deleteListByTenantPackageIdAndMenuIds(Long tenantPackageId, Collection<Long> menuIds) {
        delete(new LambdaQueryWrapper<TenantPackageMenuDO>()
                .eq(TenantPackageMenuDO::getTenantPackageId, tenantPackageId)
                .in(TenantPackageMenuDO::getMenuId, menuIds));
    }

    default void deleteListByMenuId(Long menuId) {
        delete(new LambdaQueryWrapper<TenantPackageMenuDO>().eq(TenantPackageMenuDO::getMenuId, menuId));
    }

    default void deleteListByTenantPackageId(Long tenantPackageId) {
        delete(new LambdaQueryWrapper<TenantPackageMenuDO>().eq(TenantPackageMenuDO::getTenantPackageId, tenantPackageId));
    }

}
