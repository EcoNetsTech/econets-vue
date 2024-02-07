package cn.econets.blossom.module.crm.convert.permission;

import cn.hutool.core.collection.CollUtil;
import cn.econets.blossom.framework.common.util.collection.CollectionUtils;
import cn.econets.blossom.framework.common.util.collection.MapUtils;
import cn.econets.blossom.module.crm.controller.admin.permission.vo.CrmPermissionCreateReqVO;
import cn.econets.blossom.module.crm.controller.admin.permission.vo.CrmPermissionRespVO;
import cn.econets.blossom.module.crm.controller.admin.permission.vo.CrmPermissionUpdateReqVO;
import cn.econets.blossom.module.crm.dal.dataobject.permission.CrmPermissionDO;
import cn.econets.blossom.module.crm.service.permission.bo.CrmPermissionCreateReqBO;
import cn.econets.blossom.module.crm.service.permission.bo.CrmPermissionUpdateReqBO;
import cn.econets.blossom.module.system.api.dept.dto.DeptRespDTO;
import cn.econets.blossom.module.system.api.dept.dto.PostRespDTO;
import cn.econets.blossom.module.system.api.user.dto.AdminUserRespDTO;
import com.google.common.collect.Multimaps;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static cn.econets.blossom.framework.common.util.collection.MapUtils.findAndThen;

/**
 * Crm 数据权限 Convert
 *
 */
@Mapper
public interface CrmPermissionConvert {

    CrmPermissionConvert INSTANCE = Mappers.getMapper(CrmPermissionConvert.class);

    CrmPermissionDO convert(CrmPermissionCreateReqBO createBO);

    CrmPermissionDO convert(CrmPermissionUpdateReqBO updateBO);

    CrmPermissionCreateReqBO convert(CrmPermissionCreateReqVO reqVO);

    CrmPermissionUpdateReqBO convert(CrmPermissionUpdateReqVO updateReqVO);

    List<CrmPermissionRespVO> convert(List<CrmPermissionDO> permission);

    default List<CrmPermissionRespVO> convert(List<CrmPermissionDO> permission, List<AdminUserRespDTO> userList,
                                              Map<Long, DeptRespDTO> deptMap, Map<Long, PostRespDTO> postMap) {
        Map<Long, AdminUserRespDTO> userMap = CollectionUtils.convertMap(userList, AdminUserRespDTO::getId);
        return CollectionUtils.convertList(convert(permission), item -> {
            findAndThen(userMap, item.getUserId(), user -> {
                item.setNickname(user.getNickname());
                findAndThen(deptMap, user.getDeptId(), deptRespDTO -> item.setDeptName(deptRespDTO.getName()));
                List<PostRespDTO> postRespList = MapUtils.getList(Multimaps.forMap(postMap), user.getPostIds());
                if (CollUtil.isEmpty(postRespList)) {
                    item.setPostNames(Collections.emptySet());
                    return;
                }
                item.setPostNames(CollectionUtils.convertSet(postRespList, PostRespDTO::getName));
            });
            return item;
        });
    }

    default List<CrmPermissionDO> convertList(CrmPermissionUpdateReqVO updateReqVO) {
        return CollectionUtils.convertList(updateReqVO.getIds(),
                id -> new CrmPermissionDO().setId(id).setLevel(updateReqVO.getLevel()));
    }

    List<CrmPermissionDO> convertList(List<CrmPermissionCreateReqBO> list);

}
