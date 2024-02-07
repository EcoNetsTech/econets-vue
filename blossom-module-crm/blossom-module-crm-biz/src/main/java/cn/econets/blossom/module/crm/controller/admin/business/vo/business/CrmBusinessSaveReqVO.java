package cn.econets.blossom.module.crm.controller.admin.business.vo.business;

import cn.econets.blossom.framework.common.validation.InEnum;
import cn.econets.blossom.module.crm.controller.admin.business.vo.product.CrmBusinessProductSaveReqVO;
import cn.econets.blossom.module.crm.enums.business.CrmBizEndStatus;
import cn.econets.blossom.module.crm.framework.operatelog.core.CrmCustomerParseFunction;
import com.mzt.logapi.starter.annotation.DiffLogField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cn.econets.blossom.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - CRM 商机创建/更新 Request VO")
@Data
public class CrmBusinessSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "32129")
    private Long id;

    @Schema(description = "商机名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @DiffLogField(name = "商机名称")
    @NotNull(message = "商机名称不能为空")
    private String name;

    @Schema(description = "商机状态类型编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "25714")
    @DiffLogField(name = "商机状态")
    @NotNull(message = "商机状态类型不能为空")
    private Long statusTypeId;

    @Schema(description = "商机状态编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "30320")
    @DiffLogField(name = "商机状态")
    @NotNull(message = "商机状态不能为空")
    private Long statusId;

    @Schema(description = "下次联系时间")
    @DiffLogField(name = "下次联系时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime contactNextTime;

    @Schema(description = "客户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "10299")
    @DiffLogField(name = "客户", function = CrmCustomerParseFunction.NAME)
    @NotNull(message = "客户不能为空")
    private Long customerId;

    @Schema(description = "预计成交日期")
    @DiffLogField(name = "预计成交日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime dealTime;

    @Schema(description = "商机金额", example = "12371")
    @DiffLogField(name = "商机金额")
    private Integer price;

    // TODO 折扣使用 Integer 类型，存储时，默认 * 100；展示的时候，前端需要 / 100；避免精度丢失问题
    @Schema(description = "整单折扣")
    @DiffLogField(name = "整单折扣")
    private Integer discountPercent;

    @Schema(description = "产品总金额", example = "12025")
    @DiffLogField(name = "产品总金额")
    private BigDecimal productPrice;

    @Schema(description = "备注", example = "随便")
    @DiffLogField(name = "备注")
    private String remark;

    @Schema(description = "结束状态", example = "1")
    @InEnum(CrmBizEndStatus.class)
    private Integer endStatus;

    // TODO 不设置默认 new ArrayList<>()；一般 pojo 不设置默认值哈
    @Schema(description = "商机产品列表")
    private List<CrmBusinessProductSaveReqVO> products = new ArrayList<>();

    @Schema(description = "联系人编号", example = "110")
    private Long contactId; // 使用场景，在【联系人详情】添加商机时，如果需要关联两者，需要传递 contactId 字段

}
