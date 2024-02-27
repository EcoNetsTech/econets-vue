package cn.econets.blossom.module.crm.controller.admin.operatelog;

import cn.econets.blossom.framework.common.pojo.CommonResult;
import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.framework.common.util.object.BeanUtils;
import cn.econets.blossom.module.crm.controller.admin.operatelog.vo.CrmOperateLogPageReqVO;
import cn.econets.blossom.module.crm.controller.admin.operatelog.vo.CrmOperateLogV2RespVO;
import cn.econets.blossom.module.crm.enums.LogRecordConstants;
import cn.econets.blossom.module.crm.enums.common.CrmBizTypeEnum;
import cn.econets.blossom.module.system.api.logger.OperateLogApi;
import cn.econets.blossom.module.system.api.logger.dto.OperateLogV2PageReqDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static cn.econets.blossom.framework.common.pojo.CommonResult.success;
import static cn.econets.blossom.framework.common.pojo.PageParam.PAGE_SIZE_NONE;
import static cn.econets.blossom.module.crm.enums.LogRecordConstants.*;

@Tag(name = "管理后台 - CRM 操作日志")
@RestController
@RequestMapping("/crm/operate-log")
@Validated
public class CrmOperateLogController {

    @Resource
    private OperateLogApi operateLogApi;

    /**
     * {@link CrmBizTypeEnum} 与 {@link LogRecordConstants} 的映射关系
     */
    private static final Map<Integer, String> BIZ_TYPE_MAP = new HashMap<>();

    static {
        BIZ_TYPE_MAP.put(CrmBizTypeEnum.CRM_LEADS.getType(), CRM_LEADS_TYPE);
        BIZ_TYPE_MAP.put(CrmBizTypeEnum.CRM_CUSTOMER.getType(), CRM_CUSTOMER_TYPE);
        BIZ_TYPE_MAP.put(CrmBizTypeEnum.CRM_CONTACT.getType(), CRM_CONTACT_TYPE);
        BIZ_TYPE_MAP.put(CrmBizTypeEnum.CRM_BUSINESS.getType(), CRM_BUSINESS_TYPE);
        BIZ_TYPE_MAP.put(CrmBizTypeEnum.CRM_CONTRACT.getType(), CRM_CONTRACT_TYPE);
        BIZ_TYPE_MAP.put(CrmBizTypeEnum.CRM_PRODUCT.getType(), CRM_PRODUCT_TYPE);
        BIZ_TYPE_MAP.put(CrmBizTypeEnum.CRM_RECEIVABLE.getType(), CRM_RECEIVABLE_TYPE);
        BIZ_TYPE_MAP.put(CrmBizTypeEnum.CRM_RECEIVABLE_PLAN.getType(), CRM_RECEIVABLE_PLAN_TYPE);
    }

    @GetMapping("/page")
    @Operation(summary = "获得操作日志")
    @PreAuthorize("@ss.hasPermission('crm:operate-log:query')")
    public CommonResult<PageResult<CrmOperateLogV2RespVO>> getCustomerOperateLog(@Valid CrmOperateLogPageReqVO pageReqVO) {
        OperateLogV2PageReqDTO reqDTO = new OperateLogV2PageReqDTO();
        reqDTO.setPageSize(PAGE_SIZE_NONE); // 默认不分页，需要分页需注释
        reqDTO.setBizType(BIZ_TYPE_MAP.get(pageReqVO.getBizType())).setBizId(pageReqVO.getBizId());
        return success(BeanUtils.toBean(operateLogApi.getOperateLogPage(reqDTO), CrmOperateLogV2RespVO.class));
    }

}
