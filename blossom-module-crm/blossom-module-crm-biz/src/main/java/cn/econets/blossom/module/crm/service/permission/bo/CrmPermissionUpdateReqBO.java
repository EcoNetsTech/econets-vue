package cn.econets.blossom.module.crm.service.permission.bo;

import cn.econets.blossom.framework.common.validation.InEnum;
import cn.econets.blossom.module.crm.enums.permission.CrmPermissionLevelEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * crm 数据权限 Update Req BO
 *
 */
@Data
public class CrmPermissionUpdateReqBO {

    /**
     * 数据权限编号
     */
    @NotNull(message = "数据权限编号不能为空")
    private Long id;

    /**
     * 当前登录用户编号
     */
    @NotNull(message = "用户编号不能为空")
    private Long userId;

    /**
     * 权限级别
     */
    @NotNull(message = "权限级别不能为空")
    @InEnum(CrmPermissionLevelEnum.class)
    private Integer level;

}
