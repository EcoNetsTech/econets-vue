package cn.econets.blossom.module.trade.controller.admin.aftersale.vo;

import cn.econets.blossom.framework.common.pojo.PageParam;
import cn.econets.blossom.framework.common.validation.InEnum;
import cn.econets.blossom.module.trade.enums.aftersale.AfterSaleStatusEnum;
import cn.econets.blossom.module.trade.enums.aftersale.AfterSaleTypeEnum;
import cn.econets.blossom.module.trade.enums.aftersale.AfterSaleWayEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.econets.blossom.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 交易售后分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AfterSalePageReqVO extends PageParam {

    @Schema(description = "售后流水号", example = "202211190847450020500077")
    private String no;

    @Schema(description = "售后状态", example = "10")
    @InEnum(value = AfterSaleStatusEnum.class, message = "售后状态必须是 {value}")
    private Integer status;

    @Schema(description = "售后类型", example = "20")
    @InEnum(value = AfterSaleTypeEnum.class, message = "售后类型必须是 {value}")
    private Integer type;

    @Schema(description = "售后方式", example = "10")
    @InEnum(value = AfterSaleWayEnum.class, message = "售后方式必须是 {value}")
    private Integer way;

    @Schema(description = "订单编号", example = "18078")
    private String orderNo;

    @Schema(description = "商品 SPU 名称", example = "李四")
    private String spuName;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
