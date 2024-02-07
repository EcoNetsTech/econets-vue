package cn.econets.blossom.module.promotion.api.bargain;

import cn.econets.blossom.module.promotion.service.bargain.BargainActivityService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * 砍价活动 Api 接口实现类
 *
 */
@Service
@Validated
public class BargainActivityApiImpl implements BargainActivityApi {

    @Resource
    private BargainActivityService bargainActivityService;

    @Override
    public void updateBargainActivityStock(Long id, Integer count) {
        bargainActivityService.updateBargainActivityStock(id, count);
    }

}
