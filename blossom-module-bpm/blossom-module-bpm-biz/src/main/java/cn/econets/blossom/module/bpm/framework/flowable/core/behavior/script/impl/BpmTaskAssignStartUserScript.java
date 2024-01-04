package cn.econets.blossom.module.bpm.framework.flowable.core.behavior.script.impl;

import cn.econets.blossom.framework.common.util.collection.SetUtils;
import cn.econets.blossom.framework.common.util.number.NumberUtils;
import cn.econets.blossom.module.bpm.enums.definition.BpmTaskRuleScriptEnum;
import cn.econets.blossom.module.bpm.framework.flowable.core.behavior.script.BpmTaskAssignScript;
import cn.econets.blossom.module.bpm.service.task.BpmProcessInstanceService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 分配给发起人审批的 Script 实现类
 *
 */
@Component
public class BpmTaskAssignStartUserScript implements BpmTaskAssignScript {

    @Resource
    @Lazy // 解决循环依赖
    private BpmProcessInstanceService bpmProcessInstanceService;

    @Override
    public Set<Long> calculateTaskCandidateUsers(DelegateExecution execution) {
        ProcessInstance processInstance = bpmProcessInstanceService.getProcessInstance(execution.getProcessInstanceId());
        Long startUserId = NumberUtils.parseLong(processInstance.getStartUserId());
        return SetUtils.asSet(startUserId);
    }

    @Override
    public BpmTaskRuleScriptEnum getEnum() {
        return BpmTaskRuleScriptEnum.START_USER;
    }

}
