package cn.econets.blossom.module.infrastructure.convert.file;

import cn.econets.blossom.module.infrastructure.controller.admin.file.vo.config.FileConfigSaveReqVO;
import cn.econets.blossom.module.infrastructure.dal.dataobject.file.FileConfigDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * 文件配置 Convert
 *]
 */
@Mapper
public interface FileConfigConvert {

    FileConfigConvert INSTANCE = Mappers.getMapper(FileConfigConvert.class);

    @Mapping(target = "config", ignore = true)
    FileConfigDO convert(FileConfigSaveReqVO bean);

}
