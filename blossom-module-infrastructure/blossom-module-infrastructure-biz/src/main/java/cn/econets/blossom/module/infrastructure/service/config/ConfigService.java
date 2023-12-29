package cn.econets.blossom.module.infrastructure.service.config;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.module.infrastructure.controller.admin.config.vo.ConfigPageReqVO;
import cn.econets.blossom.module.infrastructure.controller.admin.config.vo.ConfigSaveReqVO;
import cn.econets.blossom.module.infrastructure.dal.dataobject.config.ConfigDO;

import javax.validation.Valid;

/**
 * 参数配置 Service 接口
 *
 *
 */
public interface ConfigService {

    /**
     * 创建参数配置
     *
     * @param createReqVO 创建信息
     * @return 配置编号
     */
    Long createConfig(@Valid ConfigSaveReqVO createReqVO);

    /**
     * 更新参数配置
     *
     * @param updateReqVO 更新信息
     */
    void updateConfig(@Valid ConfigSaveReqVO updateReqVO);

    /**
     * 删除参数配置
     *
     * @param id 配置编号
     */
    void deleteConfig(Long id);

    /**
     * 获得参数配置
     *
     * @param id 配置编号
     * @return 参数配置
     */
    ConfigDO getConfig(Long id);

    /**
     * 根据参数键，获得参数配置
     *
     * @param key 配置键
     * @return 参数配置
     */
    ConfigDO getConfigByKey(String key);

    /**
     * 获得参数配置分页列表
     *
     * @param reqVO 分页条件
     * @return 分页列表
     */
    PageResult<ConfigDO> getConfigPage(@Valid ConfigPageReqVO reqVO);

}
