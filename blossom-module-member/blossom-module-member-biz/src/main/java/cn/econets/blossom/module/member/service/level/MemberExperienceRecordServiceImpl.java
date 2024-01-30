package cn.econets.blossom.module.member.service.level;

import cn.hutool.core.util.StrUtil;
import cn.econets.blossom.framework.common.pojo.PageParam;
import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.module.member.controller.admin.level.vo.experience.MemberExperienceRecordPageReqVO;
import cn.econets.blossom.module.member.convert.level.MemberExperienceRecordConvert;
import cn.econets.blossom.module.member.dal.dataobject.level.MemberExperienceRecordDO;
import cn.econets.blossom.module.member.dal.mysql.level.MemberExperienceRecordMapper;
import cn.econets.blossom.module.member.enums.MemberExperienceBizTypeEnum;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 会员经验记录 Service 实现类
 *
 *
 */
@Service
@Validated
public class MemberExperienceRecordServiceImpl implements MemberExperienceRecordService {

    @Resource
    private MemberExperienceRecordMapper experienceLogMapper;

    @Override
    public MemberExperienceRecordDO getExperienceRecord(Long id) {
        return experienceLogMapper.selectById(id);
    }

    @Override
    public PageResult<MemberExperienceRecordDO> getExperienceRecordPage(MemberExperienceRecordPageReqVO pageReqVO) {
        return experienceLogMapper.selectPage(pageReqVO);
    }

    @Override
    public PageResult<MemberExperienceRecordDO> getExperienceRecordPage(Long userId, PageParam pageParam) {
         return experienceLogMapper.selectPage(userId, pageParam);
    }

    @Override
    public void createExperienceRecord(Long userId, Integer experience, Integer totalExperience,
                                       MemberExperienceBizTypeEnum bizType, String bizId) {
        String description = StrUtil.format(bizType.getDescription(), experience);
        MemberExperienceRecordDO record = MemberExperienceRecordConvert.INSTANCE.convert(
                userId, experience, totalExperience,
                bizId, bizType.getType(), bizType.getTitle(), description);
        experienceLogMapper.insert(record);
    }

}
