package cn.econets.blossom.module.system.mq.producer.sms;

import cn.econets.blossom.framework.common.core.KeyValue;
import cn.econets.blossom.module.system.mq.message.sms.SmsSendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Sms 短信相关消息的 Producer
 *
 */
@Slf4j
@Component
public class SmsProducer {

    @Resource
    private ApplicationContext applicationContext;

    /**
     * 发送 {@link SmsSendMessage} 消息
     *
     * @param logId 短信日志编号
     * @param mobile 手机号
     * @param channelId 渠道编号
     * @param apiTemplateId 短信模板编号
     * @param templateParams 短信模板参数
     */
    public void sendSmsSendMessage(Long logId, String mobile,
                                   Long channelId, String apiTemplateId, List<KeyValue<String, Object>> templateParams) {
        SmsSendMessage message = new SmsSendMessage();
        message.setLogId(logId);
        message.setMobile(mobile);
        message.setChannelId(channelId);
        message.setApiTemplateId(apiTemplateId);
        message.setTemplateParams(templateParams);
        applicationContext.publishEvent(message);
    }

}
