package cn.econets.blossom.module.pay.service.wallet;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.module.pay.controller.admin.wallet.vo.transaction.PayWalletTransactionPageReqVO;
import cn.econets.blossom.module.pay.controller.app.wallet.vo.transaction.AppPayWalletTransactionPageReqVO;
import cn.econets.blossom.module.pay.controller.app.wallet.vo.transaction.AppPayWalletTransactionSummaryRespVO;
import cn.econets.blossom.module.pay.convert.wallet.PayWalletTransactionConvert;
import cn.econets.blossom.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.econets.blossom.module.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import cn.econets.blossom.module.pay.dal.mysql.wallet.PayWalletTransactionMapper;
import cn.econets.blossom.module.pay.dal.redis.no.PayNoRedisDAO;
import cn.econets.blossom.module.pay.enums.wallet.PayWalletBizTypeEnum;
import cn.econets.blossom.module.pay.service.wallet.bo.WalletTransactionCreateReqBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import java.time.LocalDateTime;

import static cn.econets.blossom.module.pay.controller.app.wallet.vo.transaction.AppPayWalletTransactionPageReqVO.TYPE_EXPENSE;
import static cn.econets.blossom.module.pay.controller.app.wallet.vo.transaction.AppPayWalletTransactionPageReqVO.TYPE_INCOME;

/**
 * 钱包流水 Service 实现类
 *
 *
 */
@Service
@Slf4j
@Validated
public class PayWalletTransactionServiceImpl implements PayWalletTransactionService {

    /**
     * 钱包流水的 no 前缀
     */
    private static final String WALLET_NO_PREFIX = "W";

    @Resource
    private PayWalletService payWalletService;
    @Resource
    private PayWalletTransactionMapper payWalletTransactionMapper;
    @Resource
    private PayNoRedisDAO noRedisDAO;

    @Override
    public PageResult<PayWalletTransactionDO> getWalletTransactionPage(Long userId, Integer userType,
                                                                       AppPayWalletTransactionPageReqVO pageVO) {
        PayWalletDO wallet = payWalletService.getOrCreateWallet(userId, userType);
        return payWalletTransactionMapper.selectPage(wallet.getId(), pageVO.getType(), pageVO, pageVO.getCreateTime());
    }

    @Override
    public PageResult<PayWalletTransactionDO> getWalletTransactionPage(PayWalletTransactionPageReqVO pageVO) {
        return payWalletTransactionMapper.selectPage(pageVO.getWalletId(), null, pageVO, null);
    }

    @Override
    public PayWalletTransactionDO createWalletTransaction(WalletTransactionCreateReqBO bo) {
        PayWalletTransactionDO transaction = PayWalletTransactionConvert.INSTANCE.convert(bo)
                .setNo(noRedisDAO.generate(WALLET_NO_PREFIX));
        payWalletTransactionMapper.insert(transaction);
        return transaction;
    }

    @Override
    public PayWalletTransactionDO getWalletTransactionByNo(String no) {
        return payWalletTransactionMapper.selectByNo(no);
    }

    @Override
    public PayWalletTransactionDO getWalletTransaction(String bizId, PayWalletBizTypeEnum type) {
        return payWalletTransactionMapper.selectByBiz(bizId, type.getType());
    }

    @Override
    public AppPayWalletTransactionSummaryRespVO getWalletTransactionSummary(Long userId, Integer userType, LocalDateTime[] createTime) {
        PayWalletDO wallet = payWalletService.getOrCreateWallet(userId, userType);
        AppPayWalletTransactionSummaryRespVO summary = new AppPayWalletTransactionSummaryRespVO()
                .setTotalExpense(1).setTotalIncome(100);
        return new AppPayWalletTransactionSummaryRespVO()
                .setTotalExpense(payWalletTransactionMapper.selectPriceSum(wallet.getId(), TYPE_EXPENSE, createTime))
                .setTotalIncome(payWalletTransactionMapper.selectPriceSum(wallet.getId(), TYPE_INCOME, createTime));
    }

}
