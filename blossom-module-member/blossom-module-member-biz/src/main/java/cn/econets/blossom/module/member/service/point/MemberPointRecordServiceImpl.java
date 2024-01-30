package cn.econets.blossom.module.member.service.point;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.econets.blossom.framework.common.pojo.PageParam;
import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.module.member.controller.admin.point.vo.recrod.MemberPointRecordPageReqVO;
import cn.econets.blossom.module.member.controller.app.point.vo.AppMemberPointRecordPageReqVO;
import cn.econets.blossom.module.member.dal.dataobject.point.MemberPointRecordDO;
import cn.econets.blossom.module.member.dal.dataobject.user.MemberUserDO;
import cn.econets.blossom.module.member.dal.mysql.point.MemberPointRecordMapper;
import cn.econets.blossom.module.member.enums.point.MemberPointBizTypeEnum;
import cn.econets.blossom.module.member.service.user.MemberUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

import static cn.econets.blossom.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.econets.blossom.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.econets.blossom.module.member.enums.ErrorCodeConstants.USER_POINT_NOT_ENOUGH;


/**
 * 积分记录 Service 实现类
 *
 * 
 */
@Slf4j
@Service
@Validated
public class MemberPointRecordServiceImpl implements MemberPointRecordService {

    @Resource
    private MemberPointRecordMapper memberPointRecordMapper;

    @Resource
    private MemberUserService memberUserService;

    @Override
    public PageResult<MemberPointRecordDO> getPointRecordPage(MemberPointRecordPageReqVO pageReqVO) {
        // 根据用户昵称查询出用户 ids
        Set<Long> userIds = null;
        if (StringUtils.isNotBlank(pageReqVO.getNickname())) {
            List<MemberUserDO> users = memberUserService.getUserListByNickname(pageReqVO.getNickname());
            // 如果查询用户结果为空直接返回无需继续查询
            if (CollectionUtils.isEmpty(users)) {
                return PageResult.empty();
            }
            userIds = convertSet(users, MemberUserDO::getId);
        }
        // 执行查询
        return memberPointRecordMapper.selectPage(pageReqVO, userIds);
    }

    @Override
    public PageResult<MemberPointRecordDO> getPointRecordPage(Long userId, AppMemberPointRecordPageReqVO pageReqVO) {
        return memberPointRecordMapper.selectPage(userId, pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPointRecord(Long userId, Integer point, MemberPointBizTypeEnum bizType, String bizId) {
        if (point == 0) {
            return;
        }
        // 1. 校验用户积分余额
        MemberUserDO user = memberUserService.getUser(userId);
        Integer userPoint = ObjectUtil.defaultIfNull(user.getPoint(), 0);
        int totalPoint = userPoint + point; // 用户变动后的积分
        if (totalPoint < 0) {
            throw exception(USER_POINT_NOT_ENOUGH);
        }

        // 2. 更新用户积分
        boolean success = memberUserService.updateUserPoint(userId, point);
        if (!success) {
            throw exception(USER_POINT_NOT_ENOUGH);
        }

        // 3. 增加积分记录
        MemberPointRecordDO record = new MemberPointRecordDO()
                .setUserId(userId).setBizId(bizId).setBizType(bizType.getType())
                .setTitle(bizType.getName()).setDescription(StrUtil.format(bizType.getDescription(), point))
                .setPoint(point).setTotalPoint(totalPoint);
        memberPointRecordMapper.insert(record);
    }

}
