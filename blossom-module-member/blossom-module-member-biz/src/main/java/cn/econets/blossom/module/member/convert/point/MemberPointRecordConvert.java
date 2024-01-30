package cn.econets.blossom.module.member.convert.point;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.framework.common.util.collection.MapUtils;
import cn.econets.blossom.module.member.controller.admin.point.vo.recrod.MemberPointRecordRespVO;
import cn.econets.blossom.module.member.controller.app.point.vo.AppMemberPointRecordRespVO;
import cn.econets.blossom.module.member.dal.dataobject.point.MemberPointRecordDO;
import cn.econets.blossom.module.member.dal.dataobject.user.MemberUserDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

import static cn.econets.blossom.framework.common.util.collection.CollectionUtils.convertMap;

/**
 * 用户积分记录 Convert
 *
 * 
 */
@Mapper
public interface MemberPointRecordConvert {

    MemberPointRecordConvert INSTANCE = Mappers.getMapper(MemberPointRecordConvert.class);

    default PageResult<MemberPointRecordRespVO> convertPage(PageResult<MemberPointRecordDO> pageResult, List<MemberUserDO> users) {
        PageResult<MemberPointRecordRespVO> voPageResult = convertPage(pageResult);
        // user 拼接
        Map<Long, MemberUserDO> userMap = convertMap(users, MemberUserDO::getId);
        voPageResult.getList().forEach(record -> MapUtils.findAndThen(userMap, record.getUserId(),
                memberUserRespDTO -> record.setNickname(memberUserRespDTO.getNickname())));
        return voPageResult;
    }
    PageResult<MemberPointRecordRespVO> convertPage(PageResult<MemberPointRecordDO> pageResult);

}
