package cn.econets.blossom.module.promotion.enums.common;

import cn.econets.blossom.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

// TODO 芋艿：弱化这个状态
/**
 * 促销活动的状态枚举
 *
 */
@AllArgsConstructor
@Getter
public enum PromotionActivityStatusEnum implements IntArrayValuable {

    WAIT(10, "未开始"),
    RUN(20, "进行中"),
    END(30, "已结束"),
    CLOSE(40, "已关闭");

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(PromotionActivityStatusEnum::getStatus).toArray();

    /**
     * 状态值
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
