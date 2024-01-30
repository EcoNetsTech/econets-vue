package cn.econets.blossom.module.member.service.level;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.module.member.controller.admin.level.vo.record.MemberLevelRecordPageReqVO;
import cn.econets.blossom.module.member.dal.dataobject.level.MemberLevelRecordDO;
import cn.econets.blossom.module.member.dal.mysql.level.MemberLevelRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * 会员等级记录 Service 实现类
 *
 * 
 */
@Service
@Validated
public class MemberLevelRecordServiceImpl implements MemberLevelRecordService {

    @Resource
    private MemberLevelRecordMapper levelLogMapper;

    @Override
    public MemberLevelRecordDO getLevelRecord(Long id) {
        return levelLogMapper.selectById(id);
    }

    @Override
    public PageResult<MemberLevelRecordDO> getLevelRecordPage(MemberLevelRecordPageReqVO pageReqVO) {
        return levelLogMapper.selectPage(pageReqVO);
    }

    @Override
    public void createLevelRecord(MemberLevelRecordDO levelRecord) {
        levelLogMapper.insert(levelRecord);
    }

}
