package cn.econets.blossom.module.crm.controller.admin.customer.vo;

import cn.econets.blossom.framework.common.pojo.PageParam;
import cn.econets.blossom.framework.common.validation.InEnum;
import cn.econets.blossom.module.crm.enums.common.CrmSceneTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - CRM 客户分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmCustomerPageReqVO extends PageParam {

    @Schema(description = "客户名称", example = "赵六")
    private String name;

    @Schema(description = "手机", example = "18000000000")
    private String mobile;

    @Schema(description = "所属行业", example = "1")
    private Integer industryId;

    @Schema(description = "客户等级", example = "1")
    private Integer level;

    @Schema(description = "客户来源", example = "1")
    private Integer source;

    @Schema(description = "场景类型", example = "1")
    @InEnum(CrmSceneTypeEnum.class)
    private Integer sceneType; // 场景类型，为 null 时则表示全部

    @Schema(description = "是否为公海数据", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private Boolean pool; // null 则表示为不是公海数据

}
