package cn.econets.blossom.module.crm.service.clue;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.module.crm.controller.admin.clue.vo.CrmCluePageReqVO;
import cn.econets.blossom.module.crm.controller.admin.clue.vo.CrmClueSaveReqVO;
import cn.econets.blossom.module.crm.controller.admin.clue.vo.CrmClueTransferReqVO;
import cn.econets.blossom.module.crm.controller.admin.clue.vo.CrmClueTransformReqVO;
import cn.econets.blossom.module.crm.dal.dataobject.clue.CrmClueDO;
import cn.econets.blossom.module.crm.service.clue.bo.CrmClueUpdateFollowUpReqBO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 线索 Service 接口
 *
 */
public interface CrmClueService {

    /**
     * 创建线索
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createClue(@Valid CrmClueSaveReqVO createReqVO);

    /**
     * 更新线索
     *
     * @param updateReqVO 更新信息
     */
    void updateClue(@Valid CrmClueSaveReqVO updateReqVO);

    /**
     * 更新线索相关的跟进信息
     *
     * @param clueUpdateFollowUpReqBO 信息
     */
    void updateClueFollowUp(CrmClueUpdateFollowUpReqBO clueUpdateFollowUpReqBO);

    /**
     * 删除线索
     *
     * @param id 编号
     */
    void deleteClue(Long id);

    /**
     * 获得线索
     *
     * @param id 编号
     * @return 线索
     */
    CrmClueDO getClue(Long id);

    /**
     * 获得线索列表
     *
     * @param ids 编号
     * @return 线索列表
     */
    List<CrmClueDO> getClueList(Collection<Long> ids, Long userId);

    /**
     * 获得线索分页
     *
     * @param pageReqVO 分页查询
     * @param userId    用户编号
     * @return 线索分页
     */
    PageResult<CrmClueDO> getCluePage(CrmCluePageReqVO pageReqVO, Long userId);

    /**
     * 线索转移
     *
     * @param reqVO  请求
     * @param userId 用户编号
     */
    void transferClue(CrmClueTransferReqVO reqVO, Long userId);

    /**
     * 线索转化为客户
     *
     * @param reqVO  线索编号
     * @param userId 用户编号
     */
    void translateCustomer(CrmClueTransformReqVO reqVO, Long userId);

}
