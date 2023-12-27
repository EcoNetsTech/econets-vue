package cn.econets.blossom.module.system.enums.sms;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 短信的接收状态枚举
 *
 *
 */
@Getter
@AllArgsConstructor
public enum SmsReceiveStatusEnum {

    INIT(0), // 初始化
    SUCCESS(10), // 接收成功
    FAILURE(20), // 接收失败
    ;

    private final int status;

}
