package cn.econets.blossom.module.crm.controller.admin.product;

import cn.hutool.core.collection.CollUtil;
import cn.econets.blossom.framework.common.pojo.CommonResult;
import cn.econets.blossom.framework.common.pojo.PageParam;
import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.framework.common.util.collection.SetUtils;
import cn.econets.blossom.framework.common.util.object.BeanUtils;
import cn.econets.blossom.framework.excel.core.util.ExcelUtils;
import cn.econets.blossom.framework.operatelog.core.annotations.OperateLog;
import cn.econets.blossom.module.crm.controller.admin.product.vo.product.CrmProductPageReqVO;
import cn.econets.blossom.module.crm.controller.admin.product.vo.product.CrmProductRespVO;
import cn.econets.blossom.module.crm.controller.admin.product.vo.product.CrmProductSaveReqVO;
import cn.econets.blossom.module.crm.convert.product.CrmProductConvert;
import cn.econets.blossom.module.crm.dal.dataobject.product.CrmProductCategoryDO;
import cn.econets.blossom.module.crm.dal.dataobject.product.CrmProductDO;
import cn.econets.blossom.module.crm.service.product.CrmProductCategoryService;
import cn.econets.blossom.module.crm.service.product.CrmProductService;
import cn.econets.blossom.module.system.api.logger.OperateLogApi;
import cn.econets.blossom.module.system.api.logger.dto.OperateLogV2PageReqDTO;
import cn.econets.blossom.module.system.api.logger.dto.OperateLogV2RespDTO;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static cn.econets.blossom.framework.common.pojo.CommonResult.success;
import static cn.econets.blossom.framework.common.pojo.PageParam.PAGE_SIZE_NONE;
import static cn.econets.blossom.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.econets.blossom.framework.common.util.collection.CollectionUtils.convertSetByFlatMap;
import static cn.econets.blossom.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;
import static cn.econets.blossom.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static cn.econets.blossom.module.crm.enums.LogRecordConstants.CRM_PRODUCT_TYPE;

@Tag(name = "管理后台 - CRM 产品")
@RestController
@RequestMapping("/crm/product")
@Validated
public class CrmProductController {

    @Resource
    private CrmProductService productService;
    @Resource
    private CrmProductCategoryService productCategoryService;
    @Resource
    private OperateLogApi operateLogApi;
    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/create")
    @Operation(summary = "创建产品")
    @PreAuthorize("@ss.hasPermission('crm:product:create')")
    public CommonResult<Long> createProduct(@Valid @RequestBody CrmProductSaveReqVO createReqVO) {
        return success(productService.createProduct(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新产品")
    @PreAuthorize("@ss.hasPermission('crm:product:update')")
    public CommonResult<Boolean> updateProduct(@Valid @RequestBody CrmProductSaveReqVO updateReqVO) {
        productService.updateProduct(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除产品")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:product:delete')")
    public CommonResult<Boolean> deleteProduct(@RequestParam("id") Long id) {
        productService.deleteProduct(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得产品")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:product:query')")
    public CommonResult<CrmProductRespVO> getProduct(@RequestParam("id") Long id) {
        CrmProductDO product = productService.getProduct(id);
        if (product == null) {
            return success(null);
        }
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(
                SetUtils.asSet(Long.valueOf(product.getCreator()), product.getOwnerUserId()));
        CrmProductCategoryDO category = productCategoryService.getProductCategory(product.getCategoryId());
        return success(CrmProductConvert.INSTANCE.convert(product, userMap, category));
    }

    @GetMapping("/page")
    @Operation(summary = "获得产品分页")
    @PreAuthorize("@ss.hasPermission('crm:product:query')")
    public CommonResult<PageResult<CrmProductRespVO>> getProductPage(@Valid CrmProductPageReqVO pageVO) {
        PageResult<CrmProductDO> pageResult = productService.getProductPage(pageVO, getLoginUserId());
        return success(new PageResult<>(getProductDetailList(pageResult.getList()), pageResult.getTotal()));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出产品 Excel")
    @PreAuthorize("@ss.hasPermission('crm:product:export')")
    @OperateLog(type = EXPORT)
    public void exportProductExcel(@Valid CrmProductPageReqVO exportReqVO,
                                   HttpServletResponse response) throws IOException {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CrmProductDO> list = productService.getProductPage(exportReqVO, getLoginUserId()).getList();
        // 导出 Excel
        ExcelUtils.write(response, "产品.xls", "数据", CrmProductRespVO.class,
                getProductDetailList(list));
    }

    private List<CrmProductRespVO> getProductDetailList(List<CrmProductDO> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(
                convertSetByFlatMap(list, user -> Stream.of(Long.valueOf(user.getCreator()), user.getOwnerUserId())));
        List<CrmProductCategoryDO> productCategoryList = productCategoryService.getProductCategoryList(
                convertSet(list, CrmProductDO::getCategoryId));
        return CrmProductConvert.INSTANCE.convertList(list, userMap, productCategoryList);
    }

    @GetMapping("/operate-log-page")
    @Operation(summary = "获得产品操作日志")
    @PreAuthorize("@ss.hasPermission('crm:product:query')")
    public CommonResult<PageResult<OperateLogV2RespDTO>> getProductOperateLog(@RequestParam("bizId") Long bizId) {
        OperateLogV2PageReqDTO reqVO = new OperateLogV2PageReqDTO();
        reqVO.setPageSize(PAGE_SIZE_NONE); // 不分页
        reqVO.setBizType(CRM_PRODUCT_TYPE).setBizId(bizId);
        return success(operateLogApi.getOperateLogPage(BeanUtils.toBean(reqVO, OperateLogV2PageReqDTO.class)));
    }

}
