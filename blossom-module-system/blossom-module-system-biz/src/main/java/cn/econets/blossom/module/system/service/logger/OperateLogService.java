package cn.econets.blossom.module.system.service.logger;


import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.module.system.api.logger.dto.OperateLogCreateReqDTO;
import cn.econets.blossom.module.system.api.logger.dto.OperateLogV2CreateReqDTO;
import cn.econets.blossom.module.system.api.logger.dto.OperateLogV2PageReqDTO;
import cn.econets.blossom.module.system.controller.admin.logger.vo.operatelog.OperateLogPageReqVO;
import cn.econets.blossom.module.system.dal.dataobject.logger.OperateLogDO;
import cn.econets.blossom.module.system.dal.dataobject.logger.OperateLogV2DO;

/**
 * 操作日志 Service 接口
 *
 */
public interface OperateLogService {

    /**
     * 记录操作日志
     *
     * @param createReqDTO 操作日志请求
     */
    void createOperateLog(OperateLogCreateReqDTO createReqDTO);

    /**
     * 获得操作日志分页列表
     *
     * @param pageReqVO 分页条件
     * @return 操作日志分页列表
     */
    PageResult<OperateLogDO> getOperateLogPage(OperateLogPageReqVO pageReqVO);

    // ======================= LOG V2 =======================

    /**
     * 记录操作日志 V2
     *
     * @param createReqDTO 创建请求
     */
    void createOperateLogV2(OperateLogV2CreateReqDTO createReqDTO);

    /**
     * 获得操作日志分页列表
     *
     * @param pageReqVO 分页条件
     * @return 操作日志分页列表
     */
    PageResult<OperateLogV2DO> getOperateLogPage(OperateLogV2PageReqDTO pageReqVO);
}
