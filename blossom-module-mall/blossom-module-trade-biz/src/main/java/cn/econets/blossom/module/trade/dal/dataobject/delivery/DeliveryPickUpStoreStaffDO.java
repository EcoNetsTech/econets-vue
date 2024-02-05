package cn.econets.blossom.module.trade.dal.dataobject.delivery;

import cn.econets.blossom.framework.common.enums.CommonStatusEnum;
import cn.econets.blossom.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

// TODO ：后续再详细 review 一轮
// TODO ：可能改成 DeliveryPickUpStoreUserDO
/**
 * 自提门店店员 DO
 *
 */
@TableName(value ="trade_delivery_pick_up_store_staff")
@KeySequence("trade_delivery_pick_up_store_staff_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
public class DeliveryPickUpStoreStaffDO extends BaseDO {

    /**
     * 编号，自增
     */
    @TableId
    private Long id;

    /**
     * 自提门店编号
     *
     * 关联 {@link DeliveryPickUpStoreDO#getId()}
     */
    private Long storeId;

    /**
     * 管理员用户id
     *
     * 关联 {AdminUserDO#getId()}
     */
    private Long adminUserId;

    /**
     * 状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

}
