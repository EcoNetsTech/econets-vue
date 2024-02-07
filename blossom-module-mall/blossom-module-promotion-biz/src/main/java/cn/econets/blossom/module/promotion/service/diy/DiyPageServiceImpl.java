package cn.econets.blossom.module.promotion.service.diy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.module.promotion.controller.admin.diy.vo.page.DiyPageCreateReqVO;
import cn.econets.blossom.module.promotion.controller.admin.diy.vo.page.DiyPagePageReqVO;
import cn.econets.blossom.module.promotion.controller.admin.diy.vo.page.DiyPagePropertyUpdateRequestVO;
import cn.econets.blossom.module.promotion.controller.admin.diy.vo.page.DiyPageUpdateReqVO;
import cn.econets.blossom.module.promotion.convert.diy.DiyPageConvert;
import cn.econets.blossom.module.promotion.dal.dataobject.diy.DiyPageDO;
import cn.econets.blossom.module.promotion.dal.mysql.diy.DiyPageMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static cn.econets.blossom.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.econets.blossom.module.promotion.enums.ErrorCodeConstants.DIY_PAGE_NAME_USED;
import static cn.econets.blossom.module.promotion.enums.ErrorCodeConstants.DIY_PAGE_NOT_EXISTS;

/**
 * 装修页面 Service 实现类
 *
 */
@Service
@Validated
public class DiyPageServiceImpl implements DiyPageService {

    @Resource
    private DiyPageMapper diyPageMapper;

    @Override
    public Long createDiyPage(DiyPageCreateReqVO createReqVO) {
        // 校验名称唯一
        validateNameUnique(null, createReqVO.getTemplateId(), createReqVO.getName());
        // 插入
        DiyPageDO diyPage = DiyPageConvert.INSTANCE.convert(createReqVO);
        diyPage.setProperty("{}");
        diyPageMapper.insert(diyPage);
        return diyPage.getId();
    }

    @Override
    public void updateDiyPage(DiyPageUpdateReqVO updateReqVO) {
        // 校验存在
        validateDiyPageExists(updateReqVO.getId());
        // 校验名称唯一
        validateNameUnique(updateReqVO.getId(), updateReqVO.getTemplateId(), updateReqVO.getName());
        // 更新
        DiyPageDO updateObj = DiyPageConvert.INSTANCE.convert(updateReqVO);
        diyPageMapper.updateById(updateObj);
    }

    /**
     * 校验 Page 页面，在一个 template 模版下的名字是唯一的
     *
     * @param id Page 编号
     * @param templateId 模版编号
     * @param name Page 名字
     */
    void validateNameUnique(Long id, Long templateId, String name) {
        if (templateId != null || StrUtil.isBlank(name)) {
            return;
        }
        DiyPageDO page = diyPageMapper.selectByNameAndTemplateIdIsNull(name);
        if (page == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的页面
        if (id == null) {
            throw exception(DIY_PAGE_NAME_USED, name);
        }
        if (!page.getId().equals(id)) {
            throw exception(DIY_PAGE_NAME_USED, name);
        }
    }

    @Override
    public void deleteDiyPage(Long id) {
        // 校验存在
        validateDiyPageExists(id);
        // 删除
        diyPageMapper.deleteById(id);
    }

    private void validateDiyPageExists(Long id) {
        if (diyPageMapper.selectById(id) == null) {
            throw exception(DIY_PAGE_NOT_EXISTS);
        }
    }

    @Override
    public DiyPageDO getDiyPage(Long id) {
        return diyPageMapper.selectById(id);
    }

    @Override
    public List<DiyPageDO> getDiyPageList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return diyPageMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<DiyPageDO> getDiyPagePage(DiyPagePageReqVO pageReqVO) {
        return diyPageMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DiyPageDO> getDiyPageByTemplateId(Long templateId) {
        return diyPageMapper.selectListByTemplateId(templateId);
    }

    @Override
    public void updateDiyPageProperty(DiyPagePropertyUpdateRequestVO updateReqVO) {
        // 校验存在
        validateDiyPageExists(updateReqVO.getId());
        // 更新
        DiyPageDO updateObj = DiyPageConvert.INSTANCE.convert(updateReqVO);
        diyPageMapper.updateById(updateObj);
    }

}
