package cn.econets.blossom.module.crm.service.message;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.module.crm.controller.admin.message.vo.CrmTodayCustomerPageReqVO;
import cn.econets.blossom.module.crm.dal.dataobject.customer.CrmCustomerDO;

import javax.validation.Valid;

/**
 * CRM 代办消息 Service 接口
 *
 */
public interface CrmMessageService {

    /**
     * TODO 注释要写下
     *
     * @param pageReqVO
     * @return
     */
    PageResult<CrmCustomerDO> getTodayCustomerPage(@Valid CrmTodayCustomerPageReqVO pageReqVO, Long userId);

}
