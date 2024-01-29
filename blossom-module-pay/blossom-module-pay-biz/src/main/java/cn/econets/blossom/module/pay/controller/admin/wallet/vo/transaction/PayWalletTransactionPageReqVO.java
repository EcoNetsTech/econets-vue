package cn.econets.blossom.module.pay.controller.admin.wallet.vo.transaction;

import cn.econets.blossom.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 钱包流水分页 Request VO")
@Data
public class PayWalletTransactionPageReqVO extends PageParam  {

    @Schema(description = "钱包编号",  example = "1")
    private Long walletId;

}
