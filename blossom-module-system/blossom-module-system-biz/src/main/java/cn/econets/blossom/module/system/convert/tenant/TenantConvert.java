package cn.econets.blossom.module.system.convert.tenant;

import cn.econets.blossom.module.system.controller.tenant.vo.tenant.TenantSaveReqVO;
import cn.econets.blossom.module.system.controller.user.vo.user.UserSaveReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 租户 Convert
 *
 */
@Mapper
public interface TenantConvert {

    TenantConvert INSTANCE = Mappers.getMapper(TenantConvert.class);

    default UserSaveReqVO convert02(TenantSaveReqVO bean) {
        UserSaveReqVO reqVO = new UserSaveReqVO();
        reqVO.setUsername(bean.getUsername());
        reqVO.setPassword(bean.getPassword());
        reqVO.setNickname(bean.getContactName());
        reqVO.setMobile(bean.getContactMobile());
        return reqVO;
    }

}