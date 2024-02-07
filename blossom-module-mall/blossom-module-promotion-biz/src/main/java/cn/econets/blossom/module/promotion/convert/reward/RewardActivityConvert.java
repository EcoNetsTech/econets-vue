package cn.econets.blossom.module.promotion.convert.reward;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.module.promotion.controller.admin.reward.vo.RewardActivityCreateReqVO;
import cn.econets.blossom.module.promotion.controller.admin.reward.vo.RewardActivityRespVO;
import cn.econets.blossom.module.promotion.controller.admin.reward.vo.RewardActivityUpdateReqVO;
import cn.econets.blossom.module.promotion.dal.dataobject.reward.RewardActivityDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 满减送活动 Convert
 *
 */
@Mapper
public interface RewardActivityConvert {

    RewardActivityConvert INSTANCE = Mappers.getMapper(RewardActivityConvert.class);

    RewardActivityDO convert(RewardActivityCreateReqVO bean);

    RewardActivityDO convert(RewardActivityUpdateReqVO bean);

    RewardActivityRespVO convert(RewardActivityDO bean);

    PageResult<RewardActivityRespVO> convertPage(PageResult<RewardActivityDO> page);

}
