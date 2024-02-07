package cn.econets.blossom.module.crm.controller.admin.clue.vo;

import cn.econets.blossom.framework.common.validation.Mobile;
import cn.econets.blossom.framework.common.validation.Telephone;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

import static cn.econets.blossom.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - CRM 线索 创建/更新 Request VO")
@Data
public class CrmClueSaveReqVO {

    @Schema(description = "编号", example = "10969")
    private Long id;

    @Schema(description = "线索名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "线索xxx")
    @NotEmpty(message = "线索名称不能为空")
    private String name;

    @Schema(description = "下次联系时间", example = "2023-10-18 01:00:00")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime contactNextTime;

    @Schema(description = "电话", example = "18000000000")
    @Telephone
    private String telephone;

    @Schema(description = "手机号", example = "18000000000")
    @Mobile
    private String mobile;

    @Schema(description = "地址", example = "北京市海淀区")
    private String address;

    @Schema(description = "最后跟进时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime contactLastTime;

    @Schema(description = "负责人编号", example = "2048")
    private Long ownerUserId;

    @Schema(description = "备注", example = "随便")
    private String remark;

}
