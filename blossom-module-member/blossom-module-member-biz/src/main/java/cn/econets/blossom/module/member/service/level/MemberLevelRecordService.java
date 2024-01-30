package cn.econets.blossom.module.member.service.level;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.module.member.controller.admin.level.vo.record.MemberLevelRecordPageReqVO;
import cn.econets.blossom.module.member.dal.dataobject.level.MemberLevelRecordDO;

/**
 * 会员等级记录 Service 接口
 *
 * 
 */
public interface MemberLevelRecordService {

    /**
     * 获得会员等级记录
     *
     * @param id 编号
     * @return 会员等级记录
     */
    MemberLevelRecordDO getLevelRecord(Long id);

    /**
     * 获得会员等级记录分页
     *
     * @param pageReqVO 分页查询
     * @return 会员等级记录分页
     */
    PageResult<MemberLevelRecordDO> getLevelRecordPage(MemberLevelRecordPageReqVO pageReqVO);

    /**
     * 创建会员等级记录
     *
     * @param levelRecord 会员等级记录
     */
    void createLevelRecord(MemberLevelRecordDO levelRecord);

}
