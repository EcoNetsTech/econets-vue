package cn.econets.blossom.module.crm.service.contract.listener;

import cn.econets.blossom.module.bpm.api.listener.BpmResultListenerApi;
import cn.econets.blossom.module.bpm.api.listener.dto.BpmResultListenerRespDTO;
import cn.econets.blossom.module.crm.service.contract.CrmContractService;
import cn.econets.blossom.module.crm.service.contract.CrmContractServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

// TODO @后续改成支持 RPC
/**
 * 合同审批的结果的监听器实现类
 *
 */
@Component
public class CrmContractResultListener implements BpmResultListenerApi {

    @Resource
    private CrmContractService contractService;

    @Override
    public String getProcessDefinitionKey() {
        return CrmContractServiceImpl.CONTRACT_APPROVE;
    }

    @Override
    public void onEvent(BpmResultListenerRespDTO event) {
        contractService.updateContractAuditStatus(event);
    }

}
