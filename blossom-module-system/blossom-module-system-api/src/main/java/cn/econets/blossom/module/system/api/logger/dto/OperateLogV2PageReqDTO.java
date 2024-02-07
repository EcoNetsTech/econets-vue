package cn.econets.blossom.module.system.api.logger.dto;

import cn.econets.blossom.framework.common.pojo.PageParam;
import lombok.Data;

/**
 * 操作日志分页 Request DTO
 *
 */
@Data
public class OperateLogV2PageReqDTO extends PageParam {

    /**
     * 模块类型
     */
    private String bizType;
    /**
     * 模块数据编号
     */
    private Long bizId;

    /**
     * 用户编号
     */
    private Long userId;

}
