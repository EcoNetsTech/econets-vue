package cn.econets.blossom.module.bpm.convert.task;

import cn.econets.blossom.framework.common.util.date.DateUtils;
import cn.econets.blossom.module.bpm.controller.admin.task.vo.activity.BpmActivityRespVO;
import org.flowable.engine.history.HistoricActivityInstance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * BPM 活动 Convert
 *
 */
@Mapper(uses = DateUtils.class)
public interface BpmActivityConvert {

    BpmActivityConvert INSTANCE = Mappers.getMapper(BpmActivityConvert.class);

    List<BpmActivityRespVO> convertList(List<HistoricActivityInstance> list);

    @Mappings({
            @Mapping(source = "activityId", target = "key"),
            @Mapping(source = "activityType", target = "type")
    })
    BpmActivityRespVO convert(HistoricActivityInstance bean);
}
