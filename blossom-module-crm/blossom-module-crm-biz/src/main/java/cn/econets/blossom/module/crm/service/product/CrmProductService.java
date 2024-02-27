package cn.econets.blossom.module.crm.service.product;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.module.crm.controller.admin.product.vo.product.CrmProductPageReqVO;
import cn.econets.blossom.module.crm.controller.admin.product.vo.product.CrmProductSaveReqVO;
import cn.econets.blossom.module.crm.dal.dataobject.product.CrmProductDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * CRM 产品 Service 接口
 *
 */
public interface CrmProductService {

    /**
     * 创建产品
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProduct(@Valid CrmProductSaveReqVO createReqVO);

    /**
     * 更新产品
     *
     * @param updateReqVO 更新信息
     */
    void updateProduct(@Valid CrmProductSaveReqVO updateReqVO);

    /**
     * 删除产品
     *
     * @param id 编号
     */
    void deleteProduct(Long id);

    /**
     * 获得产品
     *
     * @param id 编号
     * @return 产品
     */
    CrmProductDO getProduct(Long id);

    /**
     * 获得产品列表
     *
     * @param ids 编号
     * @return 产品列表
     */
    List<CrmProductDO> getProductList(Collection<Long> ids);

    /**
     * 获得产品分页
     *
     * @param pageReqVO 分页查询
     * @return 产品分页
     */
    PageResult<CrmProductDO> getProductPage(CrmProductPageReqVO pageReqVO, Long userId);

    /**
     * 获得产品
     *
     * @param categoryId 分类编号
     * @return 产品
     */
    CrmProductDO getProductByCategoryId(Long categoryId);

    /**
     * 获得产品列表
     *
     * @param ids 产品编号
     * @return 产品列表
     */
    List<CrmProductDO> getProductListByIds(Collection<Long> ids);

}
