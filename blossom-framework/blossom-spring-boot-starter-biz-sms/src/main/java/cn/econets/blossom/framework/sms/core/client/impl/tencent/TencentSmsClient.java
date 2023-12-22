package cn.econets.blossom.framework.sms.core.client.impl.tencent;

import cn.econets.blossom.framework.common.core.KeyValue;
import cn.econets.blossom.framework.common.util.collection.ArrayUtils;
import cn.econets.blossom.framework.common.util.collection.CollectionUtils;
import cn.econets.blossom.framework.common.util.date.DateUtils;
import cn.econets.blossom.framework.common.util.json.JsonUtils;
import cn.econets.blossom.framework.sms.core.client.dto.SmsReceiveRespDTO;
import cn.econets.blossom.framework.sms.core.client.dto.SmsSendRespDTO;
import cn.econets.blossom.framework.sms.core.client.dto.SmsTemplateRespDTO;
import cn.econets.blossom.framework.sms.core.client.impl.AbstractSmsClient;
import cn.econets.blossom.framework.sms.core.enums.SmsTemplateAuditStatusEnum;
import cn.econets.blossom.framework.sms.core.property.SmsChannelProperties;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.VisibleForTesting;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 腾讯云短信功能实现
 *
 * 参见 <a href="https://cloud.tencent.com/document/product/382/52077">文档</a>
 *
 */
public class TencentSmsClient extends AbstractSmsClient {

    /**
     * 调用成功 code
     */
    public static final String API_CODE_SUCCESS = "Ok";

    /**
     * REGION，使用南京
     */
    private static final String ENDPOINT = "ap-nanjing";

    /**
     * 是否国际/港澳台短信：
     *
     * 0：表示国内短信。
     * 1：表示国际/港澳台短信。
     */
    private static final long INTERNATIONAL_CHINA = 0L;

    private SmsClient client;

    public TencentSmsClient(SmsChannelProperties properties) {
        super(properties);
        Assert.notEmpty(properties.getApiSecret(), "apiSecret 不能为空");
        validateSdkAppId(properties);
    }

    @Override
    protected void doInit() {
        // 实例化一个认证对象，入参需要传入腾讯云账户密钥对 secretId，secretKey
        Credential credential = new Credential(getApiKey(), properties.getApiSecret());
        client = new SmsClient(credential, ENDPOINT);
    }

    /**
     * 参数校验腾讯云的 SDK AppId
     *
     * 原因是：腾讯云发放短信的时候，需要额外的参数 sdkAppId
     *
     * 解决方案：考虑到不破坏原有的 apiKey + apiSecret 的结构，所以将 secretId 拼接到 apiKey 字段中，格式为 "secretId sdkAppId"。
     *
     * @param properties 配置
     */
    private static void validateSdkAppId(SmsChannelProperties properties) {
        String combineKey = properties.getApiKey();
        Assert.notEmpty(combineKey, "apiKey 不能为空");
        String[] keys = combineKey.trim().split(" ");
        Assert.isTrue(keys.length == 2, "腾讯云短信 apiKey 配置格式错误，请配置 为[secretId sdkAppId]");
    }

    private String getSdkAppId() {
        return StrUtil.subAfter(properties.getApiKey(), " ", true);
    }

    private String getApiKey() {
        return StrUtil.subBefore(properties.getApiKey(), " ", true);
    }

    @Override
    public SmsSendRespDTO sendSms(Long sendLogId, String mobile,
                                  String apiTemplateId, List<KeyValue<String, Object>> templateParams) throws Throwable {
        // 构建请求
        SendSmsRequest request = new SendSmsRequest();
        request.setSmsSdkAppId(getSdkAppId());
        request.setPhoneNumberSet(new String[]{mobile});
        request.setSignName(properties.getSignature());
        request.setTemplateId(apiTemplateId);
        request.setTemplateParamSet(ArrayUtils.toArray(templateParams, e -> String.valueOf(e.getValue())));
        SessionContext sessionContext = new SessionContext();
        sessionContext.setLogId(sendLogId);
        request.setSessionContext(JsonUtils.toJsonString(sessionContext));
        // 执行请求
        SendSmsResponse response = client.SendSms(request);
        SendStatus status = response.getSendStatusSet()[0];

        SmsSendRespDTO smsSendRespDTO = new SmsSendRespDTO();
        smsSendRespDTO.setSuccess(Objects.equals(status.getCode(), API_CODE_SUCCESS));
        smsSendRespDTO.setSerialNo(status.getSerialNo());
        smsSendRespDTO.setApiRequestId(response.getRequestId());
        smsSendRespDTO.setApiCode(status.getCode());
        smsSendRespDTO.setApiMsg(status.getMessage());
        return smsSendRespDTO;
    }

    @Override
    public List<SmsReceiveRespDTO> parseSmsReceiveStatus(String text) {
        List<SmsReceiveStatus> callback = JsonUtils.parseArray(text, SmsReceiveStatus.class);
        return CollectionUtils.convertList(callback, status -> {
            SmsReceiveRespDTO smsReceiveRespDTO = new SmsReceiveRespDTO();
            smsReceiveRespDTO.setSuccess(SmsReceiveStatus.SUCCESS_CODE.equalsIgnoreCase(status.getStatus()));
            smsReceiveRespDTO.setErrorCode(status.getErrCode());
            smsReceiveRespDTO.setErrorMsg(status.getDescription());
            smsReceiveRespDTO.setMobile(status.getMobile());
            smsReceiveRespDTO.setReceiveTime(status.getReceiveTime());
            smsReceiveRespDTO.setSerialNo(status.getSerialNo());
            smsReceiveRespDTO.setLogId(status.getSessionContext().getLogId());
            return smsReceiveRespDTO;
        });
    }

    @Override
    public SmsTemplateRespDTO getSmsTemplate(String apiTemplateId) throws Throwable {
        // 构建请求
        DescribeSmsTemplateListRequest request = new DescribeSmsTemplateListRequest();
        request.setTemplateIdSet(new Long[]{Long.parseLong(apiTemplateId)});
        request.setInternational(INTERNATIONAL_CHINA);
        // 执行请求
        DescribeSmsTemplateListResponse response = client.DescribeSmsTemplateList(request);
        DescribeTemplateListStatus status = response.getDescribeTemplateStatusSet()[0];
        if (status == null || status.getStatusCode() == null) {
            return null;
        }

        SmsTemplateRespDTO smsTemplateRespDTO = new SmsTemplateRespDTO();
        smsTemplateRespDTO.setId(status.getTemplateId().toString());
        smsTemplateRespDTO.setContent(status.getTemplateContent());
        smsTemplateRespDTO.setAuditStatus(convertSmsTemplateAuditStatus(status.getStatusCode().intValue()));
        smsTemplateRespDTO.setAuditReason(status.getReviewReply());
        return smsTemplateRespDTO;
    }

    @VisibleForTesting
    Integer convertSmsTemplateAuditStatus(int templateStatus) {
        switch (templateStatus) {
            case 1: return SmsTemplateAuditStatusEnum.CHECKING.getStatus();
            case 0: return SmsTemplateAuditStatusEnum.SUCCESS.getStatus();
            case -1: return SmsTemplateAuditStatusEnum.FAIL.getStatus();
            default: throw new IllegalArgumentException(String.format("未知审核状态(%d)", templateStatus));
        }
    }

    @Data
    private static class SmsReceiveStatus {

        /**
         * 短信接受成功 code
         */
        public static final String SUCCESS_CODE = "SUCCESS";

        /**
         * 用户实际接收到短信的时间
         */
        @JsonProperty("user_receive_time")
        @JsonFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND, timezone = DateUtils.TIME_ZONE_DEFAULT)
        private LocalDateTime receiveTime;

        /**
         * 国家（或地区）码
         */
        @JsonProperty("nationcode")
        private String nationCode;

        /**
         * 手机号码
         */
        private String mobile;

        /**
         * 实际是否收到短信接收状态，SUCCESS（成功）、FAIL（失败）
         */
        @JsonProperty("report_status")
        private String status;

        /**
         * 用户接收短信状态码错误信息
         */
        @JsonProperty("errmsg")
        private String errCode;

        /**
         * 用户接收短信状态描述
         */
        @JsonProperty("description")
        private String description;

        /**
         * 本次发送标识 ID（与发送接口返回的SerialNo对应）
         */
        @JsonProperty("sid")
        private String serialNo;

        /**
         * 用户的 session 内容（与发送接口的请求参数 SessionContext 一致）
         */
        @JsonProperty("ext")
        private SessionContext sessionContext;

    }

    @VisibleForTesting
    @Data
    static class SessionContext {

        /**
         * 发送短信记录id
         */
        private Long logId;

    }

}
