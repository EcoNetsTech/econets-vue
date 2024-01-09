package cn.econets.blossom.module.system.enums.notify;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通知模板类型枚举
 *
 */
@Getter
@AllArgsConstructor
public enum NotifyTemplateTypeEnum {

    /**
     * 系统消息
     */
    SYSTEM_MESSAGE(2),
    /**
     * 通知消息
     */
    NOTIFICATION_MESSAGE(1);

    private final Integer type;

}
