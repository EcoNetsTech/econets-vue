package cn.econets.blossom.module.system.service.logger;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.module.system.api.logger.dto.LoginLogCreateReqDTO;
import cn.econets.blossom.module.system.controller.admin.logger.vo.loginlog.LoginLogPageReqVO;
import cn.econets.blossom.module.system.dal.dataobject.logger.LoginLogDO;

import javax.validation.Valid;

/**
 * 登录日志 Service 接口
 */
public interface LoginLogService {

    /**
     * 获得登录日志分页
     *
     * @param pageReqVO 分页条件
     * @return 登录日志分页
     */
    PageResult<LoginLogDO> getLoginLogPage(LoginLogPageReqVO pageReqVO);

    /**
     * 创建登录日志
     *
     * @param reqDTO 日志信息
     */
    void createLoginLog(@Valid LoginLogCreateReqDTO reqDTO);

}
