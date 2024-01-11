package cn.econets.blossom.module.infrastructure.dal.mysql.file;

import cn.econets.blossom.module.infrastructure.dal.dataobject.file.FileContentDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileContentMapper extends BaseMapper<FileContentDO> {
}
