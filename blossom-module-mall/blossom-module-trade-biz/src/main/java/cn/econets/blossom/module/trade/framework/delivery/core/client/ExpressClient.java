package cn.econets.blossom.module.trade.framework.delivery.core.client;

import cn.econets.blossom.module.trade.framework.delivery.core.client.dto.ExpressTrackQueryReqDTO;
import cn.econets.blossom.module.trade.framework.delivery.core.client.dto.ExpressTrackRespDTO;

import java.util.List;

/**
 * 快递客户端接口
 *
 */
public interface ExpressClient {

    /**
     * 快递实时查询
     *
     * @param reqDTO 查询请求参数
     */
    // TODO ：返回字段可以参考 https://doc.youzanyun.com/detail/API/0/5 响应的 data
    List<ExpressTrackRespDTO> getExpressTrackList(ExpressTrackQueryReqDTO reqDTO);

}
