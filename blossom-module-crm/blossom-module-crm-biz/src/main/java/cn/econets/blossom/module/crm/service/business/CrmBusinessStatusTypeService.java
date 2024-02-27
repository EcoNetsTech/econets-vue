package cn.econets.blossom.module.crm.service.business;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.module.crm.controller.admin.business.vo.type.CrmBusinessStatusTypePageReqVO;
import cn.econets.blossom.module.crm.controller.admin.business.vo.type.CrmBusinessStatusTypeQueryVO;
import cn.econets.blossom.module.crm.controller.admin.business.vo.type.CrmBusinessStatusTypeSaveReqVO;
import cn.econets.blossom.module.crm.dal.dataobject.business.CrmBusinessStatusTypeDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 商机状态类型 Service 接口
 *
 */
public interface CrmBusinessStatusTypeService {

    /**
     * 创建商机状态类型
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createBusinessStatusType(@Valid CrmBusinessStatusTypeSaveReqVO createReqVO);

    /**
     * 更新商机状态类型
     *
     * @param updateReqVO 更新信息
     */
    void updateBusinessStatusType(@Valid CrmBusinessStatusTypeSaveReqVO updateReqVO);

    /**
     * 删除商机状态类型
     *
     * @param id 编号
     */
    void deleteBusinessStatusType(Long id);

    /**
     * 获得商机状态类型
     *
     * @param id 编号
     * @return 商机状态类型
     */
    CrmBusinessStatusTypeDO getBusinessStatusType(Long id);

    /**
     * 获得商机状态类型分页
     *
     * @param pageReqVO 分页查询
     * @return 商机状态类型分页
     */
    PageResult<CrmBusinessStatusTypeDO> getBusinessStatusTypePage(CrmBusinessStatusTypePageReqVO pageReqVO);

    // TODO @ljlleo 常用的 ids 之类的查询，可以封装单独的方法，不用走类似 QueryVO，用起来更方便。
    /**
     * 获得商机状态类型列表
     *
     * @param queryVO 查询参数
     * @return 商机状态类型列表
     */
    List<CrmBusinessStatusTypeDO> selectList(CrmBusinessStatusTypeQueryVO queryVO);

    /**
     * 获得商机状态类型列表
     *
     * @param ids 编号数组
     * @return 商机状态类型列表
     */
    List<CrmBusinessStatusTypeDO> getBusinessStatusTypeList(Collection<Long> ids);

}
