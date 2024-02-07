package cn.econets.blossom.module.crm.enums.product;

import cn.econets.blossom.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * CRM 商品状态
 *
 * @since 2023-11-30 21:53
 */
@Getter
@AllArgsConstructor
public enum CrmProductStatusEnum implements IntArrayValuable {

    DISABLE(0, "下架"),
    ENABLE(1, "上架");

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(CrmProductStatusEnum::getStatus).toArray();

    /**
     * 状态
     */
    private final Integer status;
    /**
     * 状态名
     */
    private final String name;

    @Override
    public int[] array() {
        return ARRAYS;
    }

}
