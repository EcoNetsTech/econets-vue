package cn.econets.blossom.module.crm.service.message;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.module.crm.controller.admin.message.vo.CrmTodayCustomerPageReqVO;
import cn.econets.blossom.module.crm.dal.dataobject.customer.CrmCustomerDO;
import cn.econets.blossom.module.crm.dal.mysql.customer.CrmCustomerMapper;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

// TODO 注释要写下
@Component
@Validated
public class CrmMessageServiceImpl implements CrmMessageService {

    @Resource
    private CrmCustomerMapper customerMapper;

    @Override
    public PageResult<CrmCustomerDO> getTodayCustomerPage(CrmTodayCustomerPageReqVO pageReqVO, Long userId) {
        return customerMapper.selectTodayCustomerPage(pageReqVO, userId);
    }

}
