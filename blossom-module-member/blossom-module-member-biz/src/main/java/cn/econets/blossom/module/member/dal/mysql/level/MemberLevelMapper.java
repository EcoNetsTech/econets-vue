package cn.econets.blossom.module.member.dal.mysql.level;

import cn.econets.blossom.framework.mybatis.core.mapper.BaseMapperX;
import cn.econets.blossom.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.econets.blossom.module.member.controller.admin.level.vo.level.MemberLevelListReqVO;
import cn.econets.blossom.module.member.dal.dataobject.level.MemberLevelDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 会员等级 Mapper
 *
 * 
 */
@Mapper
public interface MemberLevelMapper extends BaseMapperX<MemberLevelDO> {

    default List<MemberLevelDO> selectList(MemberLevelListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<MemberLevelDO>()
                .likeIfPresent(MemberLevelDO::getName, reqVO.getName())
                .eqIfPresent(MemberLevelDO::getStatus, reqVO.getStatus())
                .orderByAsc(MemberLevelDO::getLevel));
    }


    default List<MemberLevelDO> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapperX<MemberLevelDO>()
                .eq(MemberLevelDO::getStatus, status)
                .orderByAsc(MemberLevelDO::getLevel));
    }

}
