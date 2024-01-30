package cn.econets.blossom.module.member.controller.app.signin;

import cn.econets.blossom.framework.common.pojo.CommonResult;
import cn.econets.blossom.framework.common.pojo.PageParam;
import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.framework.security.core.annotations.PreAuthenticated;
import cn.econets.blossom.module.member.controller.app.signin.vo.record.AppMemberSignInRecordRespVO;
import cn.econets.blossom.module.member.controller.app.signin.vo.record.AppMemberSignInRecordSummaryRespVO;
import cn.econets.blossom.module.member.convert.signin.MemberSignInRecordConvert;
import cn.econets.blossom.module.member.dal.dataobject.signin.MemberSignInRecordDO;
import cn.econets.blossom.module.member.service.signin.MemberSignInRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static cn.econets.blossom.framework.common.pojo.CommonResult.success;
import static cn.econets.blossom.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - 签到记录")
@RestController
@RequestMapping("/member/sign-in/record")
@Validated
public class AppMemberSignInRecordController {

    @Resource
    private MemberSignInRecordService signInRecordService;

    @GetMapping("/get-summary")
    @Operation(summary = "获得个人签到统计")
    @PreAuthenticated
    public CommonResult<AppMemberSignInRecordSummaryRespVO> getSignInRecordSummary() {
        return success(signInRecordService.getSignInRecordSummary(getLoginUserId()));
    }

    @PostMapping("/create")
    @Operation(summary = "签到")
    @PreAuthenticated
    public CommonResult<AppMemberSignInRecordRespVO> createSignInRecord() {
        MemberSignInRecordDO recordDO = signInRecordService.createSignRecord(getLoginUserId());
        return success(MemberSignInRecordConvert.INSTANCE.coverRecordToAppRecordVo(recordDO));
    }

    @GetMapping("/page")
    @Operation(summary = "获得签到记录分页")
    @PreAuthenticated
    public CommonResult<PageResult<AppMemberSignInRecordRespVO>> getSignRecordPage(PageParam pageParam) {
        PageResult<MemberSignInRecordDO> pageResult = signInRecordService.getSignRecordPage(getLoginUserId(), pageParam);
        return success(MemberSignInRecordConvert.INSTANCE.convertPage02(pageResult));
    }

}
