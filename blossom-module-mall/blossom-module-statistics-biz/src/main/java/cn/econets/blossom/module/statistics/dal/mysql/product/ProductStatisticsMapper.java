package cn.econets.blossom.module.statistics.dal.mysql.product;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.framework.common.pojo.SortablePageParam;
import cn.econets.blossom.framework.mybatis.core.mapper.BaseMapperX;
import cn.econets.blossom.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.econets.blossom.framework.mybatis.core.query.MPJLambdaWrapperX;
import cn.econets.blossom.module.statistics.controller.admin.product.vo.ProductStatisticsReqVO;
import cn.econets.blossom.module.statistics.controller.admin.product.vo.ProductStatisticsRespVO;
import cn.econets.blossom.module.statistics.dal.dataobject.product.ProductStatisticsDO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品统计 Mapper
 *
 */
@Mapper
public interface ProductStatisticsMapper extends BaseMapperX<ProductStatisticsDO> {

    default PageResult<ProductStatisticsDO> selectPageGroupBySpuId(ProductStatisticsReqVO reqVO, SortablePageParam pageParam) {
        return selectPage(pageParam, buildWrapper(reqVO)
                .groupBy(ProductStatisticsDO::getSpuId)
                .select(ProductStatisticsDO::getSpuId)
        );
    }

    default List<ProductStatisticsDO> selectListByTimeBetween(ProductStatisticsReqVO reqVO) {
        return selectList(buildWrapper(reqVO)
                .groupBy(ProductStatisticsDO::getTime)
                .select(ProductStatisticsDO::getTime));
    }

    default ProductStatisticsRespVO selectVoByTimeBetween(ProductStatisticsReqVO reqVO) {
        return selectJoinOne(ProductStatisticsRespVO.class, buildWrapper(reqVO));
    }

    /**
     * 构建 LambdaWrapper
     *
     * @param reqVO 查询参数
     * @return LambdaWrapper
     */
    static MPJLambdaWrapperX<ProductStatisticsDO> buildWrapper(ProductStatisticsReqVO reqVO) {
        return new MPJLambdaWrapperX<ProductStatisticsDO>()
                .betweenIfPresent(ProductStatisticsDO::getTime, reqVO.getTimes())
                .selectSum(ProductStatisticsDO::getBrowseCount)
                .selectSum(ProductStatisticsDO::getBrowseUserCount)
                .selectSum(ProductStatisticsDO::getFavoriteCount)
                .selectSum(ProductStatisticsDO::getCartCount)
                .selectSum(ProductStatisticsDO::getOrderCount)
                .selectSum(ProductStatisticsDO::getOrderPayCount)
                .selectSum(ProductStatisticsDO::getOrderPayPrice)
                .selectSum(ProductStatisticsDO::getAfterSaleCount)
                .selectSum(ProductStatisticsDO::getAfterSaleRefundPrice)
                .selectAvg(ProductStatisticsDO::getBrowseConvertPercent);
    }

    /**
     * 根据时间范围统计商品信息
     *
     * @param page      分页参数
     * @param beginTime 起始时间
     * @param endTime   截止时间
     * @return 统计
     */
    IPage<ProductStatisticsDO> selectStatisticsResultPageByTimeBetween(IPage<ProductStatisticsDO> page,
                                                                       @Param("beginTime") LocalDateTime beginTime,
                                                                       @Param("endTime") LocalDateTime endTime);

    default Long selectCountByTimeBetween(LocalDateTime beginTime, LocalDateTime endTime) {
        return selectCount(new LambdaQueryWrapperX<ProductStatisticsDO>().between(ProductStatisticsDO::getTime, beginTime, endTime));
    }

}