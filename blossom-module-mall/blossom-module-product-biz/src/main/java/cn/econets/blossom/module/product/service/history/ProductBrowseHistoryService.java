package cn.econets.blossom.module.product.service.history;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.module.product.controller.admin.history.vo.ProductBrowseHistoryPageReqVO;
import cn.econets.blossom.module.product.dal.dataobject.history.ProductBrowseHistoryDO;
import org.springframework.scheduling.annotation.Async;

import java.util.Collection;

/**
 * 商品浏览记录 Service 接口
 *
 */
public interface ProductBrowseHistoryService {

    /**
     * 创建商品浏览记录
     *
     * @param userId 用户编号
     * @param spuId  SPU 编号
     */
    @Async
    void createBrowseHistory(Long userId, Long spuId);

    /**
     * 隐藏用户商品浏览记录
     *
     * @param userId 用户编号
     * @param spuId  SPU 编号
     */
    void hideUserBrowseHistory(Long userId, Collection<Long> spuId);

    /**
     * 获得商品浏览记录分页
     *
     * @param pageReqVO 分页查询
     * @return 商品浏览记录分页
     */
    PageResult<ProductBrowseHistoryDO> getBrowseHistoryPage(ProductBrowseHistoryPageReqVO pageReqVO);

}