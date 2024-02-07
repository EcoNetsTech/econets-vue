package cn.econets.blossom.module.promotion.controller.app.banner;

import cn.econets.blossom.framework.common.pojo.CommonResult;
import cn.econets.blossom.module.promotion.controller.app.banner.vo.AppBannerRespVO;
import cn.econets.blossom.module.promotion.convert.banner.BannerConvert;
import cn.econets.blossom.module.promotion.dal.dataobject.banner.BannerDO;
import cn.econets.blossom.module.promotion.service.banner.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static cn.econets.blossom.framework.common.pojo.CommonResult.success;

@RestController
@RequestMapping("/promotion/banner")
@Tag(name = "用户 APP - 首页 Banner")
@Validated
public class AppBannerController {

    @Resource
    private BannerService bannerService;

    @GetMapping("/list")
    @Operation(summary = "获得 banner 列表")
    @Parameter(name = "position", description = "Banner position", example = "1")
    public CommonResult<List<AppBannerRespVO>> getBannerList(@RequestParam("position") Integer position) {
        List<BannerDO> bannerList = bannerService.getBannerListByPosition(position);
        return success(BannerConvert.INSTANCE.convertList01(bannerList));
    }

    @PutMapping("/add-browse-count")
    @Operation(summary = "增加 Banner 点击量")
    @Parameter(name = "id", description = "Banner 编号", example = "1024")
    public CommonResult<Boolean> addBrowseCount(@RequestParam("id") Long id) {
        bannerService.addBannerBrowseCount(id);
        return success(true);
    }

}
