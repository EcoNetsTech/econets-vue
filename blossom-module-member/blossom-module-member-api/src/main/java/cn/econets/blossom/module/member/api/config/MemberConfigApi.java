package cn.econets.blossom.module.member.api.config;

import cn.econets.blossom.module.member.api.config.dto.MemberConfigRespDTO;

/**
 * 用户配置 API 接口
 *
 * 
 */
public interface MemberConfigApi {

    /**
     * 获得积分配置
     *
     * @return 积分配置
     */
    MemberConfigRespDTO getConfig();
}
