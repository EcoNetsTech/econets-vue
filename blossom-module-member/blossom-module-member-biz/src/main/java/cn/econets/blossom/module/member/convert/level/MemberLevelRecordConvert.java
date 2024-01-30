package cn.econets.blossom.module.member.convert.level;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.module.member.controller.admin.level.vo.record.MemberLevelRecordRespVO;
import cn.econets.blossom.module.member.dal.dataobject.level.MemberLevelDO;
import cn.econets.blossom.module.member.dal.dataobject.level.MemberLevelRecordDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 会员等级记录 Convert
 *
 *
 */
@Mapper
public interface MemberLevelRecordConvert {

    MemberLevelRecordConvert INSTANCE = Mappers.getMapper(MemberLevelRecordConvert.class);

    MemberLevelRecordRespVO convert(MemberLevelRecordDO bean);

    List<MemberLevelRecordRespVO> convertList(List<MemberLevelRecordDO> list);

    PageResult<MemberLevelRecordRespVO> convertPage(PageResult<MemberLevelRecordDO> page);

    default MemberLevelRecordDO copyTo(MemberLevelDO from, MemberLevelRecordDO to) {
        if (from != null) {
            to.setLevelId(from.getId());
            to.setLevel(from.getLevel());
            to.setDiscountPercent(from.getDiscountPercent());
            to.setExperience(from.getExperience());
        }
        return to;
    }
}
