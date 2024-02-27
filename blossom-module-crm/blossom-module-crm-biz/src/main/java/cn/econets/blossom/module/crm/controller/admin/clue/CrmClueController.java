package cn.econets.blossom.module.crm.controller.admin.clue;

import cn.econets.blossom.framework.common.pojo.CommonResult;
import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.framework.common.util.object.BeanUtils;
import cn.econets.blossom.framework.excel.core.util.ExcelUtils;
import cn.econets.blossom.framework.operatelog.core.annotations.OperateLog;
import cn.econets.blossom.module.crm.controller.admin.clue.vo.*;
import cn.econets.blossom.module.crm.dal.dataobject.clue.CrmClueDO;
import cn.econets.blossom.module.crm.service.clue.CrmClueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static cn.econets.blossom.framework.common.pojo.CommonResult.success;
import static cn.econets.blossom.framework.common.pojo.PageParam.PAGE_SIZE_NONE;
import static cn.econets.blossom.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;
import static cn.econets.blossom.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - 线索")
@RestController
@RequestMapping("/crm/clue")
@Validated
public class CrmClueController {

    @Resource
    private CrmClueService clueService;

    @PostMapping("/create")
    @Operation(summary = "创建线索")
    @PreAuthorize("@ss.hasPermission('crm:clue:create')")
    public CommonResult<Long> createClue(@Valid @RequestBody CrmClueSaveReqVO createReqVO) {
        return success(clueService.createClue(createReqVO, getLoginUserId()));
    }

    @PutMapping("/update")
    @Operation(summary = "更新线索")
    @PreAuthorize("@ss.hasPermission('crm:clue:update')")
    public CommonResult<Boolean> updateClue(@Valid @RequestBody CrmClueSaveReqVO updateReqVO) {
        clueService.updateClue(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除线索")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:clue:delete')")
    public CommonResult<Boolean> deleteClue(@RequestParam("id") Long id) {
        clueService.deleteClue(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得线索")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:clue:query')")
    public CommonResult<CrmClueRespVO> getClue(@RequestParam("id") Long id) {
        CrmClueDO clue = clueService.getClue(id);
        return success(BeanUtils.toBean(clue, CrmClueRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得线索分页")
    @PreAuthorize("@ss.hasPermission('crm:clue:query')")
    public CommonResult<PageResult<CrmClueRespVO>> getCluePage(@Valid CrmCluePageReqVO pageVO) {
        PageResult<CrmClueDO> pageResult = clueService.getCluePage(pageVO, getLoginUserId());
        return success(BeanUtils.toBean(pageResult, CrmClueRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出线索 Excel")
    @PreAuthorize("@ss.hasPermission('crm:clue:export')")
    @OperateLog(type = EXPORT)
    public void exportClueExcel(@Valid CrmCluePageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PAGE_SIZE_NONE);
        List<CrmClueDO> list = clueService.getCluePage(pageReqVO, getLoginUserId()).getList();
        // 导出 Excel
        List<CrmClueRespVO> datas = BeanUtils.toBean(list, CrmClueRespVO.class);
        ExcelUtils.write(response, "线索.xls", "数据", CrmClueRespVO.class, datas);
    }

    @PutMapping("/transfer")
    @Operation(summary = "线索转移")
    @PreAuthorize("@ss.hasPermission('crm:clue:update')")
    public CommonResult<Boolean> transferClue(@Valid @RequestBody CrmClueTransferReqVO reqVO) {
        clueService.transferClue(reqVO, getLoginUserId());
        return success(true);
    }

    @PostMapping("/transform")
    @Operation(summary = "线索转化为客户")
    @PreAuthorize("@ss.hasPermission('crm:clue:update')")
    public CommonResult<Boolean> translateCustomer(@Valid @RequestBody CrmClueTranslateReqVO reqVO) {
        clueService.translateCustomer(reqVO, getLoginUserId());
        return success(Boolean.TRUE);
    }

}
