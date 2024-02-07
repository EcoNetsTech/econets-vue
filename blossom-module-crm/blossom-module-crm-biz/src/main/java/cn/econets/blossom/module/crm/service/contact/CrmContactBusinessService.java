package cn.econets.blossom.module.crm.service.contact;

import cn.econets.blossom.module.crm.controller.admin.contact.vo.CrmContactBusinessReqVO;
import cn.econets.blossom.module.crm.dal.dataobject.contact.CrmContactBusinessDO;

import javax.validation.Valid;
import java.util.List;

/**
 * CRM 联系人与商机的关联 Service 接口
 *
 */
public interface CrmContactBusinessService {

    /**
     * 创建联系人与商机的关联
     *
     * @param createReqVO 创建信息
     */
    void createContactBusinessList(@Valid CrmContactBusinessReqVO createReqVO);

    /**
     * 删除联系人与商机的关联
     *
     * @param deleteReqVO 删除信息
     */
    void deleteContactBusinessList(@Valid CrmContactBusinessReqVO deleteReqVO);

    /**
     * 删除联系人与商机的关联，基于联系人编号
     *
     * @param contactId 联系人编号
     */
    void deleteContactBusinessByContactId(Long contactId);

    /**
     * 获得联系人与商机的关联列表，基于联系人编号
     *
     * @param contactId 联系人编号
     * @return 联系人商机关联
     */
    List<CrmContactBusinessDO> getContactBusinessListByContactId(Long contactId);

}
