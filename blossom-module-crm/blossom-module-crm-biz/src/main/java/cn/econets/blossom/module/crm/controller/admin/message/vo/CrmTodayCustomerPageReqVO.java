package cn.econets.blossom.module.crm.controller.admin.message.vo;

import cn.econets.blossom.framework.common.pojo.PageParam;
import cn.econets.blossom.framework.common.validation.InEnum;
import cn.econets.blossom.module.crm.enums.common.CrmSceneTypeEnum;
import cn.econets.blossom.module.crm.enums.message.CrmContactStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 今日需联系客户 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmTodayCustomerPageReqVO extends PageParam {

    // TODO CrmContactStatusEnum 可以直接枚举三个 Integer；一般来说，枚举类尽量给数据模型用，这样枚举类少，更聚焦；这里的枚举，更多是专门给这个接口用的哈

    @Schema(description = "联系状态", example = "1")
    @InEnum(CrmContactStatusEnum.class)
    private Integer contactStatus;

    @Schema(description = "场景类型", example = "1")
    @InEnum(CrmSceneTypeEnum.class)
    private Integer sceneType;

}
