package cn.econets.blossom.module.product.controller.app.favorite.vo;

import cn.econets.blossom.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 App - 商品收藏分页查询 Request VO")
@Data
public class AppFavoritePageReqVO extends PageParam {
}
