package cn.econets.blossom.module.crm.controller.admin.message;

import cn.econets.blossom.framework.common.pojo.CommonResult;
import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.framework.common.util.object.BeanUtils;
import cn.econets.blossom.module.crm.controller.admin.customer.vo.CrmCustomerRespVO;
import cn.econets.blossom.module.crm.controller.admin.message.vo.CrmTodayCustomerPageReqVO;
import cn.econets.blossom.module.crm.dal.dataobject.customer.CrmCustomerDO;
import cn.econets.blossom.module.crm.service.message.CrmMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

import static cn.econets.blossom.framework.common.pojo.CommonResult.success;
import static cn.econets.blossom.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - CRM消息")
@RestController
@RequestMapping("/crm/message")
@Validated
public class CrmMessageController {

    @Resource
    private CrmMessageService crmMessageService;

    // TODO 未来可能合并到 CrmCustomerController
    @GetMapping("/todayCustomer") // TODO 【优先级低】url 使用中划线，项目规范。然后叫 today-customer-page，通过 page 体现出它是个分页接口
    @Operation(summary = "今日需联系客户")
    @PreAuthorize("@ss.hasPermission('crm:message:todayCustomer')")
    public CommonResult<PageResult<CrmCustomerRespVO>> getTodayCustomerPage(@Valid CrmTodayCustomerPageReqVO pageReqVO) {
        PageResult<CrmCustomerDO> pageResult = crmMessageService.getTodayCustomerPage(pageReqVO, getLoginUserId());
        return success(BeanUtils.toBean(pageResult, CrmCustomerRespVO.class));
    }

}
