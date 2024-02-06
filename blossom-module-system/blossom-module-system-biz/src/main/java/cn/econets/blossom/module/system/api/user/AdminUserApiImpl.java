package cn.econets.blossom.module.system.api.user;

import cn.econets.blossom.framework.common.util.collection.CollectionUtils;
import cn.econets.blossom.framework.common.util.object.BeanUtils;
import cn.econets.blossom.module.system.api.user.dto.AdminUserRespDTO;
import cn.econets.blossom.module.system.dal.dataobject.dept.DeptDO;
import cn.econets.blossom.module.system.dal.dataobject.user.AdminUserDO;
import cn.econets.blossom.module.system.service.dept.DeptService;
import cn.econets.blossom.module.system.service.user.AdminUserService;
import cn.hutool.core.util.ObjUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;


/**
 * Admin 用户 API 实现类
 *
 */
@Service
public class AdminUserApiImpl implements AdminUserApi {

    @Resource
    private AdminUserService userService;
    @Resource
    private DeptService deptService;

    @Override
    public AdminUserRespDTO getUser(Long id) {
        AdminUserDO user = userService.getUser(id);
        return BeanUtils.toBean(user, AdminUserRespDTO.class);
    }

    @Override
    public Set<Long> getSubordinateIds(Long id) {
        AdminUserDO user = userService.getUser(id);
        if (user == null) {
            return null;
        }

        Set<Long> subordinateIds = null; // 下属用户编号
        DeptDO dept = deptService.getDept(user.getDeptId());
        // TODO 需要递归查询到子部门；并且要排除到自己噢。
        // TODO 保持 if return 原则，这里其实要判断不等于，则返回 null；最好返回 空集合，上面也是
        if (ObjUtil.equal(dept.getLeaderUserId(), id)) { // 校验是否是该部门的负责人
            List<AdminUserDO> users = userService.getUserListByDeptIds(Collections.singletonList(dept.getId()));
            subordinateIds = CollectionUtils.convertSet(users, AdminUserDO::getId);
        }
        return subordinateIds;
    }

    @Override
    public List<AdminUserRespDTO> getUserList(Collection<Long> ids) {
        List<AdminUserDO> users = userService.getUserList(ids);
        return BeanUtils.toBean(users, AdminUserRespDTO.class);
    }

    @Override
    public List<AdminUserRespDTO> getUserListByDeptIds(Collection<Long> deptIds) {
        List<AdminUserDO> users = userService.getUserListByDeptIds(deptIds);
        return BeanUtils.toBean(users, AdminUserRespDTO.class);
    }

    @Override
    public List<AdminUserRespDTO> getUserListByPostIds(Collection<Long> postIds) {
        List<AdminUserDO> users = userService.getUserListByPostIds(postIds);
        return BeanUtils.toBean(users, AdminUserRespDTO.class);
    }

    @Override
    public void validateUserList(Collection<Long> ids) {
        userService.validateUserList(ids);
    }

}
