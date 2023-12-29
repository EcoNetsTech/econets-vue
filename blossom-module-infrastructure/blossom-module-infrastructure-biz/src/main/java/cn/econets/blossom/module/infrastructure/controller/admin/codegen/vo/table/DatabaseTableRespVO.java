package cn.econets.blossom.module.infrastructure.controller.admin.codegen.vo.table;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 数据库的表定义 Response VO")
@Data
public class DatabaseTableRespVO {

    @Schema(description = "表名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "表名称")
    private String name;

    @Schema(description = "表描述", requiredMode = Schema.RequiredMode.REQUIRED, example = "表描述")
    private String comment;

}
