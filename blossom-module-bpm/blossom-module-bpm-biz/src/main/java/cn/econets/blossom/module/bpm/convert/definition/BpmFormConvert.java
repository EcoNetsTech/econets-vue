package cn.econets.blossom.module.bpm.convert.definition;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.module.bpm.controller.admin.definition.vo.form.BpmFormCreateReqVO;
import cn.econets.blossom.module.bpm.controller.admin.definition.vo.form.BpmFormRespVO;
import cn.econets.blossom.module.bpm.controller.admin.definition.vo.form.BpmFormSimpleRespVO;
import cn.econets.blossom.module.bpm.controller.admin.definition.vo.form.BpmFormUpdateReqVO;
import cn.econets.blossom.module.bpm.dal.dataobject.definition.BpmFormDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 动态表单 Convert
 *
 */
@Mapper
public interface BpmFormConvert {

    BpmFormConvert INSTANCE = Mappers.getMapper(BpmFormConvert.class);

    BpmFormDO convert(BpmFormCreateReqVO bean);

    BpmFormDO convert(BpmFormUpdateReqVO bean);

    BpmFormRespVO convert(BpmFormDO bean);

    List<BpmFormSimpleRespVO> convertList2(List<BpmFormDO> list);

    PageResult<BpmFormRespVO> convertPage(PageResult<BpmFormDO> page);

}
