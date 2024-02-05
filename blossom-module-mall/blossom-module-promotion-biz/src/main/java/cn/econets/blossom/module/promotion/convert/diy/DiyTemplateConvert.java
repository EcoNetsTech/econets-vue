package cn.econets.blossom.module.promotion.convert.diy;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.module.promotion.controller.admin.diy.vo.template.*;
import cn.econets.blossom.module.promotion.controller.app.diy.vo.AppDiyTemplatePropertyRespVO;
import cn.econets.blossom.module.promotion.dal.dataobject.diy.DiyPageDO;
import cn.econets.blossom.module.promotion.dal.dataobject.diy.DiyTemplateDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 装修模板 Convert
 *
 */
@Mapper
public interface DiyTemplateConvert {

    DiyTemplateConvert INSTANCE = Mappers.getMapper(DiyTemplateConvert.class);

    DiyTemplateDO convert(DiyTemplateCreateReqVO bean);

    DiyTemplateDO convert(DiyTemplateUpdateReqVO bean);

    DiyTemplateRespVO convert(DiyTemplateDO bean);

    List<DiyTemplateRespVO> convertList(List<DiyTemplateDO> list);

    PageResult<DiyTemplateRespVO> convertPage(PageResult<DiyTemplateDO> page);

    DiyTemplatePropertyRespVO convertPropertyVo(DiyTemplateDO diyTemplate, List<DiyPageDO> pages);

    AppDiyTemplatePropertyRespVO convertPropertyVo2(DiyTemplateDO diyTemplate, String home, String user);

    DiyTemplateDO convert(DiyTemplatePropertyUpdateRequestVO updateReqVO);

}
