package cn.econets.blossom.module.crm.controller.admin.contact.vo;

import cn.econets.blossom.framework.common.pojo.PageParam;
import cn.econets.blossom.framework.common.validation.InEnum;
import cn.econets.blossom.module.crm.enums.common.CrmSceneTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - CRM 联系人分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmContactPageReqVO extends PageParam {

    @Schema(description = "姓名", example = "econets")
    private String name;

    @Schema(description = "客户编号", example = "10795")
    private Long customerId;

    @Schema(description = "手机号", example = "13898273941")
    private String mobile;

    @Schema(description = "电话", example = "021-383773")
    private String telephone;

    @Schema(description = "电子邮箱", example = "111@22.com")
    private String email;

    @Schema(description = "QQ", example = "3882872")
    private Long qq;

    @Schema(description = "微信", example = "zzZ98373")
    private String wechat;

    @Schema(description = "场景类型", example = "1")
    @InEnum(CrmSceneTypeEnum.class)
    private Integer sceneType; // 场景类型，为 null 时则表示全部

}
