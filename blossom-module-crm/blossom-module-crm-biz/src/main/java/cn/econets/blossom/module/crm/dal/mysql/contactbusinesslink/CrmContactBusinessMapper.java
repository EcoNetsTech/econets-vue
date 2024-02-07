package cn.econets.blossom.module.crm.dal.mysql.contactbusinesslink;

import cn.econets.blossom.framework.mybatis.core.mapper.BaseMapperX;
import cn.econets.blossom.module.crm.dal.dataobject.contact.CrmContactBusinessDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * CRM 联系人与商机的关联 Mapper
 *
 */
@Mapper
public interface CrmContactBusinessMapper extends BaseMapperX<CrmContactBusinessDO> {

    default CrmContactBusinessDO selectByContactIdAndBusinessId(Long contactId, Long businessId) {
        return selectOne(CrmContactBusinessDO::getContactId, contactId,
                CrmContactBusinessDO::getBusinessId, businessId);
    }

    default void deleteByContactIdAndBusinessId(Long contactId, Collection<Long> businessIds) {
        delete(new LambdaQueryWrapper<CrmContactBusinessDO>()
                .eq(CrmContactBusinessDO::getContactId, contactId)
                .in(CrmContactBusinessDO::getBusinessId, businessIds));
    }

    default List<CrmContactBusinessDO> selectListByContactId(Long contactId) {
        return selectList(CrmContactBusinessDO::getContactId, contactId);
    }

}
