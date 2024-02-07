package cn.econets.blossom.module.crm.service.receivable;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.module.crm.controller.admin.receivable.vo.receivable.CrmReceivableCreateReqVO;
import cn.econets.blossom.module.crm.controller.admin.receivable.vo.receivable.CrmReceivablePageReqVO;
import cn.econets.blossom.module.crm.controller.admin.receivable.vo.receivable.CrmReceivableUpdateReqVO;
import cn.econets.blossom.module.crm.dal.dataobject.customer.CrmCustomerDO;
import cn.econets.blossom.module.crm.dal.dataobject.receivable.CrmReceivableDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * CRM 回款 Service 接口
 *
 *
 */
public interface CrmReceivableService {

    /**
     * 创建回款
     *
     * @param createReqVO 创建信息
     * @param userId      用户编号
     * @return 编号
     */
    Long createReceivable(@Valid CrmReceivableCreateReqVO createReqVO, Long userId);

    /**
     * 更新回款
     *
     * @param updateReqVO 更新信息
     */
    void updateReceivable(@Valid CrmReceivableUpdateReqVO updateReqVO);

    /**
     * 删除回款
     *
     * @param id 编号
     */
    void deleteReceivable(Long id);

    /**
     * 获得回款
     *
     * @param id 编号
     * @return 回款
     */
    CrmReceivableDO getReceivable(Long id);

    /**
     * 获得回款列表
     *
     * @param ids 编号
     * @return 回款列表
     */
    List<CrmReceivableDO> getReceivableList(Collection<Long> ids);

    /**
     * 获得回款分页
     *
     * 数据权限：基于 {@link CrmReceivableDO} 读取
     *
     * @param pageReqVO 分页查询
     * @param userId    用户编号
     * @return 回款分页
     */
    PageResult<CrmReceivableDO> getReceivablePage(CrmReceivablePageReqVO pageReqVO, Long userId);

    /**
     * 获得回款分页，基于指定客户
     *
     * 数据权限：基于 {@link CrmCustomerDO} 读取
     *
     * @param pageReqVO 分页查询
     * @return 回款分页
     */
    PageResult<CrmReceivableDO> getReceivablePageByCustomerId(CrmReceivablePageReqVO pageReqVO);

}
