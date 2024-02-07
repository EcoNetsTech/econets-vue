package cn.econets.blossom.module.crm.service.product;

import cn.econets.blossom.module.crm.controller.admin.product.vo.category.CrmProductCategoryCreateReqVO;
import cn.econets.blossom.module.crm.controller.admin.product.vo.category.CrmProductCategoryListReqVO;
import cn.econets.blossom.module.crm.dal.dataobject.product.CrmProductCategoryDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * CRM 产品分类 Service 接口
 *
 */
public interface CrmProductCategoryService {

    /**
     * 创建产品分类
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProductCategory(@Valid CrmProductCategoryCreateReqVO createReqVO);

    /**
     * 更新产品分类
     *
     * @param updateReqVO 更新信息
     */
    void updateProductCategory(@Valid CrmProductCategoryCreateReqVO updateReqVO);

    /**
     * 删除产品分类
     *
     * @param id 编号
     */
    void deleteProductCategory(Long id);

    /**
     * 获得产品分类
     *
     * @param id 编号
     * @return 产品分类
     */
    CrmProductCategoryDO getProductCategory(Long id);

    /**
     * 获得产品分类列表
     *
     * @param listReqVO 列表请求
     * @return 产品分类列表
     */
    List<CrmProductCategoryDO> getProductCategoryList(CrmProductCategoryListReqVO listReqVO);

    /**
     * 获得产品分类列表
     *
     * @param ids 编号数组
     * @return 产品分类列表
     */
    List<CrmProductCategoryDO> getProductCategoryList(Collection<Long> ids);

}
