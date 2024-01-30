package cn.econets.blossom.module.member.service.signin;

import cn.econets.blossom.framework.common.pojo.PageParam;
import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.module.member.controller.admin.signin.vo.record.MemberSignInRecordPageReqVO;
import cn.econets.blossom.module.member.controller.app.signin.vo.record.AppMemberSignInRecordSummaryRespVO;
import cn.econets.blossom.module.member.dal.dataobject.signin.MemberSignInRecordDO;

/**
 * 签到记录 Service 接口
 *
 * 
 */
public interface MemberSignInRecordService {

    /**
     * 【管理员】获得签到记录分页
     *
     * @param pageReqVO 分页查询
     * @return 签到记录分页
     */
    PageResult<MemberSignInRecordDO> getSignInRecordPage(MemberSignInRecordPageReqVO pageReqVO);

    /**
     * 【会员】获得签到记录分页
     *
     * @param userId    用户编号
     * @param pageParam 分页查询
     * @return 签到记录分页
     */
    PageResult<MemberSignInRecordDO> getSignRecordPage(Long userId, PageParam pageParam);

    /**
     * 创建签到记录
     *
     * @param userId 用户编号
     * @return 签到记录
     */
    MemberSignInRecordDO createSignRecord(Long userId);

    /**
     * 根据用户编号，获得个人签到统计信息
     *
     * @param userId 用户编号
     * @return 个人签到统计信息
     */
    AppMemberSignInRecordSummaryRespVO getSignInRecordSummary(Long userId);


}
