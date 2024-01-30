package cn.econets.blossom.module.member.controller.admin.signin;

import cn.econets.blossom.framework.common.pojo.CommonResult;
import cn.econets.blossom.module.member.controller.admin.signin.vo.config.MemberSignInConfigCreateReqVO;
import cn.econets.blossom.module.member.controller.admin.signin.vo.config.MemberSignInConfigRespVO;
import cn.econets.blossom.module.member.controller.admin.signin.vo.config.MemberSignInConfigUpdateReqVO;
import cn.econets.blossom.module.member.convert.signin.MemberSignInConfigConvert;
import cn.econets.blossom.module.member.dal.dataobject.signin.MemberSignInConfigDO;
import cn.econets.blossom.module.member.service.signin.MemberSignInConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.econets.blossom.framework.common.pojo.CommonResult.success;

// TODO url
@Tag(name = "管理后台 - 签到规则")
@RestController
@RequestMapping("/member/sign-in/config")
@Validated
public class MemberSignInConfigController {

    @Resource
    private MemberSignInConfigService signInConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建签到规则")
    @PreAuthorize("@ss.hasPermission('point:sign-in-config:create')")
    public CommonResult<Long> createSignInConfig(@Valid @RequestBody MemberSignInConfigCreateReqVO createReqVO) {
        return success(signInConfigService.createSignInConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新签到规则")
    @PreAuthorize("@ss.hasPermission('point:sign-in-config:update')")
    public CommonResult<Boolean> updateSignInConfig(@Valid @RequestBody MemberSignInConfigUpdateReqVO updateReqVO) {
        signInConfigService.updateSignInConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除签到规则")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('point:sign-in-config:delete')")
    public CommonResult<Boolean> deleteSignInConfig(@RequestParam("id") Long id) {
        signInConfigService.deleteSignInConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得签到规则")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('point:sign-in-config:query')")
    public CommonResult<MemberSignInConfigRespVO> getSignInConfig(@RequestParam("id") Long id) {
        MemberSignInConfigDO signInConfig = signInConfigService.getSignInConfig(id);
        return success(MemberSignInConfigConvert.INSTANCE.convert(signInConfig));
    }

    @GetMapping("/list")
    @Operation(summary = "获得签到规则列表")
    @PreAuthorize("@ss.hasPermission('point:sign-in-config:query')")
    public CommonResult<List<MemberSignInConfigRespVO>> getSignInConfigList() {
        List<MemberSignInConfigDO> list = signInConfigService.getSignInConfigList();
        return success(MemberSignInConfigConvert.INSTANCE.convertList(list));
    }

}
