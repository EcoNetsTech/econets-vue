package cn.econets.blossom.module.statistics.dal.mysql.infra;

import cn.econets.blossom.framework.mybatis.core.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

// TODO api 访问日志，现在会清理，可能要单独有个偏业务的访问表；
/**
 * API 访问日志的统计 Mapper
 *
 */
@Mapper
@SuppressWarnings("rawtypes")
public interface ApiAccessLogStatisticsMapper extends BaseMapperX {

    // TODO 已经 review
    Integer selectIpCountByUserTypeAndCreateTimeBetween(@Param("userType") Integer userType,
                                                        @Param("beginTime") LocalDateTime beginTime,
                                                        @Param("endTime") LocalDateTime endTime);

    // TODO 已经 review
    Integer selectUserCountByUserTypeAndCreateTimeBetween(@Param("userType") Integer userType,
                                                          @Param("beginTime") LocalDateTime beginTime,
                                                          @Param("endTime") LocalDateTime endTime);

}
