package cn.econets.blossom.module.infrastructure.service.demo.demo01;

import cn.econets.blossom.framework.common.pojo.PageResult;
import cn.econets.blossom.module.infrastructure.controller.admin.demo.demo01.vo.Demo01ContactPageReqVO;
import cn.econets.blossom.module.infrastructure.controller.admin.demo.demo01.vo.Demo01ContactSaveReqVO;
import cn.econets.blossom.module.infrastructure.dal.dataobject.demo.demo01.Demo01ContactDO;

import javax.validation.Valid;

/**
 * 示例联系人 Service 接口
 *
 *
 */
public interface Demo01ContactService {

    /**
     * 创建示例联系人
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDemo01Contact(@Valid Demo01ContactSaveReqVO createReqVO);

    /**
     * 更新示例联系人
     *
     * @param updateReqVO 更新信息
     */
    void updateDemo01Contact(@Valid Demo01ContactSaveReqVO updateReqVO);

    /**
     * 删除示例联系人
     *
     * @param id 编号
     */
    void deleteDemo01Contact(Long id);

    /**
     * 获得示例联系人
     *
     * @param id 编号
     * @return 示例联系人
     */
    Demo01ContactDO getDemo01Contact(Long id);

    /**
     * 获得示例联系人分页
     *
     * @param pageReqVO 分页查询
     * @return 示例联系人分页
     */
    PageResult<Demo01ContactDO> getDemo01ContactPage(Demo01ContactPageReqVO pageReqVO);

}
