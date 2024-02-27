package cn.econets.blossom.module.crm.service.message;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.module.crm.controller.admin.backlog.vo.CrmTodayCustomerPageReqVO;
import cn.econets.blossom.module.crm.dal.dataobject.customer.CrmCustomerDO;

import javax.validation.Valid;

/**
 * CRM 待办消息 Service 接口
 *
 */
public interface CrmBacklogService {

    /**
     * 根据【联系状态】、【场景类型】筛选客户分页
     *
     * @param pageReqVO 分页查询
     * @return 分页数据
     */
    PageResult<CrmCustomerDO> getTodayCustomerPage(@Valid CrmTodayCustomerPageReqVO pageReqVO, Long userId);

}
