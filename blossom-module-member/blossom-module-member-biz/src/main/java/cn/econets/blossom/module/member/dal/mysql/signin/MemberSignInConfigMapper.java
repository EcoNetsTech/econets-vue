package cn.econets.blossom.module.member.dal.mysql.signin;

import cn.econets.blossom.framework.mybatis.core.mapper.BaseMapperX;
import cn.econets.blossom.module.member.dal.dataobject.signin.MemberSignInConfigDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 签到规则 Mapper
 *
 * 
 */
@Mapper
public interface MemberSignInConfigMapper extends BaseMapperX<MemberSignInConfigDO> {

    default MemberSignInConfigDO selectByDay(Integer day) {
        return selectOne(MemberSignInConfigDO::getDay, day);
    }

    default List <MemberSignInConfigDO> selectListByStatus(Integer status) {
        return selectList(MemberSignInConfigDO::getStatus, status);
    }
}
