package cn.econets.blossom.module.member.service.signin;

import cn.econets.blossom.module.member.controller.admin.signin.vo.config.MemberSignInConfigCreateReqVO;
import cn.econets.blossom.module.member.controller.admin.signin.vo.config.MemberSignInConfigUpdateReqVO;
import cn.econets.blossom.module.member.convert.signin.MemberSignInConfigConvert;
import cn.econets.blossom.module.member.dal.dataobject.signin.MemberSignInConfigDO;
import cn.econets.blossom.module.member.dal.mysql.signin.MemberSignInConfigMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;

import static cn.econets.blossom.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.econets.blossom.module.member.enums.ErrorCodeConstants.SIGN_IN_CONFIG_EXISTS;
import static cn.econets.blossom.module.member.enums.ErrorCodeConstants.SIGN_IN_CONFIG_NOT_EXISTS;

/**
 * 签到规则 Service 实现类
 *
 * 
 */
@Service
@Validated
public class MemberSignInConfigServiceImpl implements MemberSignInConfigService {

    @Resource
    private MemberSignInConfigMapper memberSignInConfigMapper;

    @Override
    public Long createSignInConfig(MemberSignInConfigCreateReqVO createReqVO) {
        // 判断是否重复插入签到天数
        validateSignInConfigDayDuplicate(createReqVO.getDay(), null);

        // 插入
        MemberSignInConfigDO signInConfig = MemberSignInConfigConvert.INSTANCE.convert(createReqVO);
        memberSignInConfigMapper.insert(signInConfig);
        // 返回
        return signInConfig.getId();
    }

    @Override
    public void updateSignInConfig(MemberSignInConfigUpdateReqVO updateReqVO) {
        // 校验存在
        validateSignInConfigExists(updateReqVO.getId());
        // 判断是否重复插入签到天数
        validateSignInConfigDayDuplicate(updateReqVO.getDay(), updateReqVO.getId());

        // 判断更新
        MemberSignInConfigDO updateObj = MemberSignInConfigConvert.INSTANCE.convert(updateReqVO);
        memberSignInConfigMapper.updateById(updateObj);
    }

    @Override
    public void deleteSignInConfig(Long id) {
        // 校验存在
        validateSignInConfigExists(id);
        // 删除
        memberSignInConfigMapper.deleteById(id);
    }

    private void validateSignInConfigExists(Long id) {
        if (memberSignInConfigMapper.selectById(id) == null) {
            throw exception(SIGN_IN_CONFIG_NOT_EXISTS);
        }
    }

    /**
     * 校验 day 是否重复
     *
     * @param day 天
     * @param id  编号，只有更新的时候会传递
     */
    private void validateSignInConfigDayDuplicate(Integer day, Long id) {
        MemberSignInConfigDO config = memberSignInConfigMapper.selectByDay(day);
        // 1. 新增时，config 非空，则说明重复
        if (id == null && config != null) {
            throw exception(SIGN_IN_CONFIG_EXISTS);
        }
        // 2. 更新时，如果 config 非空，且 id 不相等，则说明重复
        if (id != null && config != null && !config.getId().equals(id)) {
            throw exception(SIGN_IN_CONFIG_EXISTS);
        }
    }

    @Override
    public MemberSignInConfigDO getSignInConfig(Long id) {
        return memberSignInConfigMapper.selectById(id);
    }

    @Override
    public List<MemberSignInConfigDO> getSignInConfigList() {
        List<MemberSignInConfigDO> list = memberSignInConfigMapper.selectList();
        list.sort(Comparator.comparing(MemberSignInConfigDO::getDay));
        return list;
    }

    @Override
    public List<MemberSignInConfigDO> getSignInConfigList(Integer status) {
        List<MemberSignInConfigDO> list = memberSignInConfigMapper.selectListByStatus(status);
        list.sort(Comparator.comparing(MemberSignInConfigDO::getDay));
        return list;
    }

}
