package cn.econets.blossom.module.crm.dal.mysql.contract;


import cn.econets.blossom.framework.mybatis.core.mapper.BaseMapperX;
import cn.econets.blossom.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.econets.blossom.module.crm.dal.dataobject.contract.CrmContractProductDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 合同产品 Mapper
 *
 */
@Mapper
public interface CrmContractProductMapper extends BaseMapperX<CrmContractProductDO> {

    // TODO @puhui999：用不到的方法，看看是不是删除哈
    default void deleteByContractId(Long contractId) { // TODO @lzxhqs：第一个方法，和类之间最好空一行；
        delete(CrmContractProductDO::getContractId, contractId);
    }

    default CrmContractProductDO selectByContractId(Long contractId) {
        return selectOne(CrmContractProductDO::getContractId, contractId);
    }

    default List<CrmContractProductDO> selectListByContractId(Long contractId) {
        return selectList(new LambdaQueryWrapperX<CrmContractProductDO>().eq(CrmContractProductDO::getContractId, contractId));
    }

}
