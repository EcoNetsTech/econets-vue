package cn.econets.blossom.module.crm.controller.admin.receivable;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.econets.blossom.framework.common.pojo.CommonResult;
import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.framework.common.util.number.NumberUtils;
import cn.econets.blossom.framework.excel.core.util.ExcelUtils;
import cn.econets.blossom.framework.operatelog.core.annotations.OperateLog;
import cn.econets.blossom.module.crm.controller.admin.receivable.vo.receivable.CrmReceivableCreateReqVO;
import cn.econets.blossom.module.crm.controller.admin.receivable.vo.receivable.CrmReceivablePageReqVO;
import cn.econets.blossom.module.crm.controller.admin.receivable.vo.receivable.CrmReceivableRespVO;
import cn.econets.blossom.module.crm.controller.admin.receivable.vo.receivable.CrmReceivableUpdateReqVO;
import cn.econets.blossom.module.crm.convert.receivable.CrmReceivableConvert;
import cn.econets.blossom.module.crm.dal.dataobject.contract.CrmContractDO;
import cn.econets.blossom.module.crm.dal.dataobject.customer.CrmCustomerDO;
import cn.econets.blossom.module.crm.dal.dataobject.receivable.CrmReceivableDO;
import cn.econets.blossom.module.crm.service.contract.CrmContractService;
import cn.econets.blossom.module.crm.service.customer.CrmCustomerService;
import cn.econets.blossom.module.crm.service.receivable.CrmReceivableService;
import cn.econets.blossom.module.system.api.user.AdminUserApi;
import cn.econets.blossom.module.system.api.user.dto.AdminUserRespDTO;
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
import java.util.Map;
import java.util.stream.Stream;

import static cn.econets.blossom.framework.common.pojo.CommonResult.success;
import static cn.econets.blossom.framework.common.pojo.PageParam.PAGE_SIZE_NONE;
import static cn.econets.blossom.framework.common.util.collection.CollectionUtils.convertListByFlatMap;
import static cn.econets.blossom.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.econets.blossom.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;
import static cn.econets.blossom.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - CRM 回款")
@RestController
@RequestMapping("/crm/receivable")
@Validated
public class CrmReceivableController {

    @Resource
    private CrmReceivableService receivableService;
    @Resource
    private CrmContractService contractService;
    @Resource
    private CrmCustomerService customerService;

    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/create")
    @Operation(summary = "创建回款")
    @PreAuthorize("@ss.hasPermission('crm:receivable:create')")
    public CommonResult<Long> createReceivable(@Valid @RequestBody CrmReceivableCreateReqVO createReqVO) {
        return success(receivableService.createReceivable(createReqVO, getLoginUserId()));
    }

    @PutMapping("/update")
    @Operation(summary = "更新回款")
    @PreAuthorize("@ss.hasPermission('crm:receivable:update')")
    public CommonResult<Boolean> updateReceivable(@Valid @RequestBody CrmReceivableUpdateReqVO updateReqVO) {
        receivableService.updateReceivable(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除回款")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:receivable:delete')")
    public CommonResult<Boolean> deleteReceivable(@RequestParam("id") Long id) {
        receivableService.deleteReceivable(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得回款")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:receivable:query')")
    public CommonResult<CrmReceivableRespVO> getReceivable(@RequestParam("id") Long id) {
        CrmReceivableDO receivable = receivableService.getReceivable(id);
        return success(CrmReceivableConvert.INSTANCE.convert(receivable));
    }

    @GetMapping("/page")
    @Operation(summary = "获得回款分页")
    @PreAuthorize("@ss.hasPermission('crm:receivable:query')")
    public CommonResult<PageResult<CrmReceivableRespVO>> getReceivablePage(@Valid CrmReceivablePageReqVO pageReqVO) {
        PageResult<CrmReceivableDO> pageResult = receivableService.getReceivablePage(pageReqVO, getLoginUserId());
        return success(buildReceivableDetailPage(pageResult));
    }

    @GetMapping("/page-by-customer")
    @Operation(summary = "获得回款分页，基于指定客户")
    public CommonResult<PageResult<CrmReceivableRespVO>> getReceivablePageByCustomer(@Valid CrmReceivablePageReqVO pageReqVO) {
        Assert.notNull(pageReqVO.getCustomerId(), "客户编号不能为空");
        PageResult<CrmReceivableDO> pageResult = receivableService.getReceivablePageByCustomerId(pageReqVO);
        return success(buildReceivableDetailPage(pageResult));
    }

    // TODO 后面在优化导出
    @GetMapping("/export-excel")
    @Operation(summary = "导出回款 Excel")
    @PreAuthorize("@ss.hasPermission('crm:receivable:export')")
    @OperateLog(type = EXPORT)
    public void exportReceivableExcel(@Valid CrmReceivablePageReqVO exportReqVO,
                                      HttpServletResponse response) throws IOException {
        exportReqVO.setPageSize(PAGE_SIZE_NONE);
        PageResult<CrmReceivableDO> pageResult = receivableService.getReceivablePage(exportReqVO, getLoginUserId());
        // 导出 Excel
        ExcelUtils.write(response, "回款.xls", "数据", CrmReceivableRespVO.class,
                buildReceivableDetailPage(pageResult).getList());
    }

    /**
     * 构建详细的回款分页结果
     *
     * @param pageResult 简单的回款分页结果
     * @return 详细的回款分页结果
     */
    private PageResult<CrmReceivableRespVO> buildReceivableDetailPage(PageResult<CrmReceivableDO> pageResult) {
        List<CrmReceivableDO> receivableList = pageResult.getList();
        if (CollUtil.isEmpty(receivableList)) {
            return PageResult.empty(pageResult.getTotal());
        }
        // 1. 获取客户列表
        List<CrmCustomerDO> customerList = customerService.getCustomerList(
                convertSet(receivableList, CrmReceivableDO::getCustomerId));
        // 2. 获取创建人、负责人列表
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(convertListByFlatMap(receivableList,
                contact -> Stream.of(NumberUtils.parseLong(contact.getCreator()), contact.getOwnerUserId())));
        // 3. 获得合同列表
        List<CrmContractDO> contractList = contractService.getContractList(
                convertSet(receivableList, CrmReceivableDO::getContractId));
        return CrmReceivableConvert.INSTANCE.convertPage(pageResult, userMap, customerList, contractList);
    }

}
