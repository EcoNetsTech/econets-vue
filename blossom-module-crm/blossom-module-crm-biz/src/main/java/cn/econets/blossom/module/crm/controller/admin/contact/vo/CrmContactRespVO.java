package cn.econets.blossom.module.crm.controller.admin.contact.vo;

import cn.econets.blossom.framework.excel.core.annotations.DictFormat;
import cn.econets.blossom.framework.excel.core.convert.DictConvert;
import cn.econets.blossom.module.infrastructure.enums.DictTypeConstants;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - CRM 联系人 Response VO")
@Data
@ToString(callSuper = true)
@ExcelIgnoreUnannotated
public class CrmContactRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "3167")
    private Long id;

    @Schema(description = "姓名", example = "芋艿")
    @ExcelProperty(value = "姓名", order = 1)
    private String name;

    @Schema(description = "客户编号", example = "10795")
    private Long customerId;

    @Schema(description = "性别")
    @ExcelProperty(value = "性别", converter = DictConvert.class, order = 3)
    @DictFormat(cn.econets.blossom.module.system.enums.DictTypeConstants.USER_SEX)
    private Integer sex;

    @Schema(description = "职位")
    @ExcelProperty(value = "职位", order = 3)
    private String post;

    @Schema(description = "是否关键决策人")
    @ExcelProperty(value = "是否关键决策人", converter = DictConvert.class, order = 3)
    @DictFormat(DictTypeConstants.BOOLEAN_STRING)
    private Boolean master;

    @Schema(description = "直属上级", example = "23457")
    private Long parentId;

    @Schema(description = "手机号", example = "1387171766")
    @ExcelProperty(value = "手机号", order = 4)
    private String mobile;

    @Schema(description = "电话", example = "021-0029922")
    @ExcelProperty(value = "电话", order = 4)
    private String telephone;

    @Schema(description = "QQ", example = "197272662")
    @ExcelProperty(value = "QQ", order = 4)
    private Long qq;

    @Schema(description = "微信", example = "zzz3883")
    @ExcelProperty(value = "微信", order = 4)
    private String wechat;

    @Schema(description = "电子邮箱", example = "1111@22.com")
    @ExcelProperty(value = "邮箱", order = 4)
    private String email;

    @Schema(description = "地区编号", example = "20158")
    private Integer areaId;

    @Schema(description = "地址")
    @ExcelProperty(value = "地址", order = 5)
    private String detailAddress;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty(value = "备注", order = 6)
    private String remark;

    @Schema(description = "负责人用户编号", example = "14334")
    private Long ownerUserId;

    @Schema(description = "最后跟进时间")
    @ExcelProperty(value = "最后跟进时间", order = 6)
    private LocalDateTime contactLastTime;

    @Schema(description = "下次联系时间")
    @ExcelProperty(value = "下次联系时间", order = 6)
    private LocalDateTime contactNextTime;

    @Schema(description = "创建人", example = "25682")
    private String creator;

    @Schema(description = "创建人名字", example = "test")
    @ExcelProperty(value = "创建人", order = 8)
    private String creatorName;

    @ExcelProperty(value = "客户名称", order = 2)
    @Schema(description = "客户名字", example = "test")
    private String customerName;

    @Schema(description = "负责人", example = "test")
    @ExcelProperty(value = "负责人", order = 7)
    private String ownerUserName;

    @Schema(description = "直属上级名", example = "芋头")
    @ExcelProperty(value = "直属上级", order = 4)
    private String parentName;

    @Schema(description = "地区名", example = "上海上海市浦东新区")
    @ExcelProperty(value = "地区", order = 5)
    private String areaName;

}
