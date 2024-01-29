package cn.econets.blossom.module.pay.service.demo;

import cn.hutool.core.util.ObjectUtil;
import cn.econets.blossom.framework.common.pojo.PageParam;
import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.module.pay.controller.admin.demo.vo.transfer.PayDemoTransferCreateReqVO;
import cn.econets.blossom.module.pay.convert.demo.PayDemoTransferConvert;
import cn.econets.blossom.module.pay.dal.dataobject.demo.PayDemoTransferDO;
import cn.econets.blossom.module.pay.dal.dataobject.transfer.PayTransferDO;
import cn.econets.blossom.module.pay.dal.mysql.demo.PayDemoTransferMapper;
import cn.econets.blossom.module.pay.enums.transfer.PayTransferStatusEnum;
import cn.econets.blossom.module.pay.service.transfer.PayTransferService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.Objects;

import static cn.econets.blossom.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.econets.blossom.module.pay.enums.ErrorCodeConstants.*;
import static cn.econets.blossom.module.pay.enums.transfer.PayTransferStatusEnum.WAITING;

/**
 * 示例转账业务 Service 实现类
 *
 *
 */
@Service
@Validated
public class PayDemoTransferServiceImpl implements PayDemoTransferService {

    /**
     * 接入的实力应用编号

     * 从 [支付管理 -> 应用信息] 里添加
     */
    private static final Long TRANSFER_APP_ID = 8L;
    @Resource
    private PayDemoTransferMapper demoTransferMapper;
    @Resource
    private PayTransferService payTransferService;
    @Resource
    private Validator validator;

    @Override
    public Long createDemoTransfer(@Valid PayDemoTransferCreateReqVO vo) {
        // 1 校验参数
        vo.validate(validator);
        // 2 保存示例转账业务表
        PayDemoTransferDO demoTransfer = PayDemoTransferConvert.INSTANCE.convert(vo)
                .setAppId(TRANSFER_APP_ID).setTransferStatus(WAITING.getStatus());
        demoTransferMapper.insert(demoTransfer);
        return demoTransfer.getId();
    }

    @Override
    public PageResult<PayDemoTransferDO> getDemoTransferPage(PageParam pageVO) {
        return demoTransferMapper.selectPage(pageVO);
    }

    @Override
    public void updateDemoTransferStatus(Long id, Long payTransferId) {
        PayTransferDO payTransfer = validateDemoTransferStatusCanUpdate(id, payTransferId);
        // 更新示例订单状态
        if (payTransfer != null) {
            demoTransferMapper.updateById(new PayDemoTransferDO().setId(id)
                    .setPayTransferId(payTransferId)
                    .setPayChannelCode(payTransfer.getChannelCode())
                    .setTransferStatus(payTransfer.getStatus())
                    .setTransferTime(payTransfer.getSuccessTime()));
        }
    }

    private PayTransferDO validateDemoTransferStatusCanUpdate(Long id, Long payTransferId) {
        PayDemoTransferDO demoTransfer = demoTransferMapper.selectById(id);
        if (demoTransfer == null) {
            throw exception(DEMO_TRANSFER_NOT_FOUND);
        }
        if (PayTransferStatusEnum.isSuccess(demoTransfer.getTransferStatus())
                || PayTransferStatusEnum.isClosed(demoTransfer.getTransferStatus())) {
            // 无需更新返回 null
            return null;
        }
        PayTransferDO transfer = payTransferService.getTransfer(payTransferId);
        if (transfer == null) {
            throw exception(PAY_TRANSFER_NOT_FOUND);
        }
        if (!Objects.equals(demoTransfer.getPrice(), transfer.getPrice())) {
            throw exception(DEMO_TRANSFER_FAIL_PRICE_NOT_MATCH);
        }
        if (ObjectUtil.notEqual(transfer.getMerchantTransferId(), id.toString())) {
            throw exception(DEMO_TRANSFER_FAIL_TRANSFER_ID_ERROR);
        }
        // TODO 校验账号
        return transfer;
    }
}
