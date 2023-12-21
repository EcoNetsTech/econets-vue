package cn.econets.blossom.module.system.service.tenant;

import cn.econets.blossom.framework.common.enums.CommonStatusEnum;
import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.framework.common.util.object.BeanUtils;
import cn.econets.blossom.module.system.controller.tenant.vo.packages.TenantPackagePageReqVO;
import cn.econets.blossom.module.system.controller.tenant.vo.packages.TenantPackageSaveReqVO;
import cn.econets.blossom.module.system.dal.dao.tenant.TenantPackageMapper;
import cn.econets.blossom.module.system.dal.dataobject.tenant.TenantDO;
import cn.econets.blossom.module.system.dal.dataobject.tenant.TenantPackageDO;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static cn.econets.blossom.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.econets.blossom.module.system.enums.ErrorCodeConstants.*;

/**
 * 租户套餐 Service 实现类
 *
 *
 */
@Service
@Validated
public class TenantPackageServiceImpl implements TenantPackageService {

    @Resource
    private TenantPackageMapper tenantPackageMapper;

    @Resource
    @Lazy // 避免循环依赖的报错
    private TenantService tenantService;

    @Override
    public Long createTenantPackage(TenantPackageSaveReqVO createReqVO) {
        // 插入
        TenantPackageDO tenantPackage = BeanUtils.toBean(createReqVO, TenantPackageDO.class);
        tenantPackageMapper.insert(tenantPackage);
        // 返回
        return tenantPackage.getId();
    }

    @Override
    @DSTransactional // 多数据源，使用 @DSTransactional 保证本地事务，以及数据源的切换
    public void updateTenantPackage(TenantPackageSaveReqVO updateReqVO) {
        // 校验存在
        TenantPackageDO tenantPackage = validateTenantPackageExists(updateReqVO.getId());
        // 更新
        TenantPackageDO updateObj = BeanUtils.toBean(updateReqVO, TenantPackageDO.class);
        tenantPackageMapper.updateById(updateObj);
        // 如果菜单发生变化，则修改每个租户的菜单
        if (!CollUtil.isEqualList(tenantPackage.getMenuIds(), updateReqVO.getMenuIds())) {
            List<TenantDO> tenants = tenantService.getTenantListByPackageId(tenantPackage.getId());
            tenants.forEach(tenant -> tenantService.updateTenantRoleMenu(tenant.getId(), updateReqVO.getMenuIds()));
        }
    }

    @Override
    public void deleteTenantPackage(Long id) {
        // 校验存在
        validateTenantPackageExists(id);
        // 校验正在使用
        validateTenantUsed(id);
        // 删除
        tenantPackageMapper.deleteById(id);
    }

    private TenantPackageDO validateTenantPackageExists(Long id) {
        TenantPackageDO tenantPackage = tenantPackageMapper.selectById(id);
        if (tenantPackage == null) {
            throw exception(TENANT_PACKAGE_NOT_EXISTS);
        }
        return tenantPackage;
    }

    private void validateTenantUsed(Long id) {
        if (tenantService.getTenantCountByPackageId(id) > 0) {
            throw exception(TENANT_PACKAGE_USED);
        }
    }

    @Override
    public TenantPackageDO getTenantPackage(Long id) {
        return tenantPackageMapper.selectById(id);
    }

    @Override
    public PageResult<TenantPackageDO> getTenantPackagePage(TenantPackagePageReqVO pageReqVO) {
        return tenantPackageMapper.selectPage(pageReqVO);
    }

    @Override
    public TenantPackageDO validTenantPackage(Long id) {
        TenantPackageDO tenantPackage = tenantPackageMapper.selectById(id);
        if (tenantPackage == null) {
            throw exception(TENANT_PACKAGE_NOT_EXISTS);
        }
        if (tenantPackage.getStatus().equals(CommonStatusEnum.DISABLE.getStatus())) {
            throw exception(TENANT_PACKAGE_DISABLE, tenantPackage.getName());
        }
        return tenantPackage;
    }

    @Override
    public List<TenantPackageDO> getTenantPackageListByStatus(Integer status) {
        return tenantPackageMapper.selectListByStatus(status);
    }

}
