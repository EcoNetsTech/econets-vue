package cn.econets.blossom.module.crm.service.business.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 更新商机商品 Update Req BO
 *
 */
@Data
public class CrmBusinessUpdateProductReqBO {

    /**
     * 商机编号
     */
    @NotNull(message = "商机编号不能为空")
    private Long id;

    // TODO @再想想
    @NotEmpty(message = "产品列表不能为空")
    private List<CrmBusinessProductItem> productItems;

    @Schema(description = "产品列表")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CrmBusinessProductItem {

        @Schema(description = "产品编号", example = "20529")
        @NotNull(message = "产品编号不能为空")
        private Long id;

        @Schema(description = "产品数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "8911")
        @NotNull(message = "产品数量不能为空")
        private Integer count;

        @Schema(description = "产品折扣")
        private Integer discountPercent;

    }

}
