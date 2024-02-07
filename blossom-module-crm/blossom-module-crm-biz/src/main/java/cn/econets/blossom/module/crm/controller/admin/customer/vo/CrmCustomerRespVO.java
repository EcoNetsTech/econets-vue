package cn.econets.blossom.module.crm.controller.admin.customer.vo;

import cn.econets.blossom.framework.excel.core.annotations.DictFormat;
import cn.econets.blossom.framework.excel.core.convert.DictConvert;
import cn.econets.blossom.module.infrastructure.enums.DictTypeConstants;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.econets.blossom.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - CRM 客户 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CrmCustomerRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "13563")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "客户名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "13563")
    @ExcelProperty("客户名称")
    private String name;

    @Schema(description = "跟进状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "13563")
    @ExcelProperty(value = "跟进状态", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.BOOLEAN_STRING)
    private Boolean followUpStatus;

    @Schema(description = "锁定状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "13563")
    @ExcelProperty(value = "锁定状态", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.BOOLEAN_STRING)
    private Boolean lockStatus;

    @Schema(description = "成交状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "13563")
    @ExcelProperty(value = "成交状态", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.BOOLEAN_STRING)
    private Boolean dealStatus;

    @Schema(description = "所属行业", requiredMode = Schema.RequiredMode.REQUIRED, example = "13563")
    @ExcelProperty(value = "所属行业", converter = DictConvert.class)
    @DictFormat(cn.econets.blossom.module.crm.enums.DictTypeConstants.CRM_CUSTOMER_INDUSTRY)
    private Integer industryId;

    @Schema(description = "客户等级", requiredMode = Schema.RequiredMode.REQUIRED, example = "13563")
    @ExcelProperty(value = "客户等级", converter = DictConvert.class)
    @DictFormat(cn.econets.blossom.module.crm.enums.DictTypeConstants.CRM_CUSTOMER_LEVEL)
    private Integer level;

    @Schema(description = "客户来源", requiredMode = Schema.RequiredMode.REQUIRED, example = "13563")
    @ExcelProperty(value = "客户来源", converter = DictConvert.class)
    @DictFormat(cn.econets.blossom.module.crm.enums.DictTypeConstants.CRM_CUSTOMER_SOURCE)
    private Integer source;

    @Schema(description = "负责人的用户编号", example = "25682")
    @ExcelProperty("手机")
    private String mobile;

    @Schema(description = "负责人的用户编号", example = "25682")
    @ExcelProperty("电话")
    private String telephone;

    @Schema(description = "负责人的用户编号", example = "25682")
    @ExcelProperty("网址")
    private String website;

    @Schema(description = "负责人的用户编号", example = "25682")
    @ExcelProperty("QQ")
    private String qq;

    @Schema(description = "负责人的用户编号", example = "25682")
    @ExcelProperty("wechat")
    private String wechat;

    @Schema(description = "负责人的用户编号", example = "25682")
    @ExcelProperty("email")
    private String email;

    @Schema(description = "负责人的用户编号", example = "25682")
    @ExcelProperty("客户描述")
    private String description;

    @Schema(description = "负责人的用户编号", example = "25682")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "负责人的用户编号", example = "25682")
    @ExcelProperty("负责人的用户编号")
    private Long ownerUserId;
    @Schema(description = "负责人名字", example = "25682")
    @ExcelProperty("负责人名字")
    private String ownerUserName;
    @Schema(description = "负责人部门")
    @ExcelProperty("负责人部门")
    private String ownerUserDeptName;

    @Schema(description = "地区编号", example = "1024")
    @ExcelProperty("地区编号")
    private Integer areaId;
    @Schema(description = "地区名称", example = "北京市")
    @ExcelProperty("地区名称")
    private String areaName;
    @Schema(description = "详细地址", example = "北京市成华大道")
    @ExcelProperty("详细地址")
    private String detailAddress;

    @Schema(description = "最后跟进时间")
    @ExcelProperty("最后跟进时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime contactLastTime;

    @Schema(description = "下次联系时间")
    @ExcelProperty("下次联系时间")
    private LocalDateTime contactNextTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "创建人", example = "1024")
    @ExcelProperty("创建人")
    private String creator;
    @Schema(description = "创建人名字", example = "芋道源码")
    @ExcelProperty("创建人名字")
    private String creatorName;

    @Schema(description = "距离加入公海时间", example = "1")
    private Long poolDay;

}
