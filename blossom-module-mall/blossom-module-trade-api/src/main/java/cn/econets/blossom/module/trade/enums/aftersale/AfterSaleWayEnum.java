package cn.econets.blossom.module.trade.enums.aftersale;

import cn.econets.blossom.framework.common.core.IntArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 交易售后 - 方式
 *
 */
@RequiredArgsConstructor
@Getter
public enum AfterSaleWayEnum implements IntArrayValuable {

    REFUND(10, "仅退款"),
    RETURN_AND_REFUND(20, "退货退款");

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(AfterSaleWayEnum::getWay).toArray();

    /**
     * 方式
     */
    private final Integer way;
    /**
     * 方式名
     */
    private final String name;

    @Override
    public int[] array() {
        return ARRAYS;
    }

}
