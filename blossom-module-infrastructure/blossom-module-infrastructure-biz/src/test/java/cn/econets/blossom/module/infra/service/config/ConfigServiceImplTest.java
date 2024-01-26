package cn.econets.blossom.module.infra.service.config;

import cn.econets.blossom.framework.common.util.collection.ArrayUtils;
import cn.econets.blossom.framework.test.core.ut.BaseDbUnitTest;
import cn.econets.blossom.framework.test.core.util.AssertUtils;
import cn.econets.blossom.framework.test.core.util.RandomUtils;
import cn.econets.blossom.module.infrastructure.controller.admin.config.vo.ConfigSaveReqVO;
import cn.econets.blossom.module.infrastructure.dal.dataobject.config.ConfigDO;
import cn.econets.blossom.module.infrastructure.dal.mysql.config.ConfigMapper;
import cn.econets.blossom.module.infrastructure.enums.ErrorCodeConstants;
import cn.econets.blossom.module.infrastructure.enums.config.ConfigTypeEnum;
import cn.econets.blossom.module.infrastructure.service.config.ConfigServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;

import java.util.function.Consumer;

import static cn.hutool.core.util.RandomUtil.randomEle;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 */
@Import(ConfigServiceImplTest.class)
public class ConfigServiceImplTest extends BaseDbUnitTest {
    @Resource
    private ConfigServiceImpl configService;

    @Resource
    private ConfigMapper configMapper;

    @Test
    public void testCreateConfig_success() {
        // 准备参数
        ConfigSaveReqVO reqVO = RandomUtils.randomPojo(ConfigSaveReqVO.class)
                .setId(null); // 防止 id 被赋值，导致唯一性校验失败

        // 调用
        Long configId = configService.createConfig(reqVO);
        // 断言
        assertNotNull(configId);
        // 校验记录的属性是否正确
        ConfigDO config = configMapper.selectById(configId);
        AssertUtils.assertPojoEquals(reqVO, config, "id");
        assertEquals(ConfigTypeEnum.CUSTOM.getType(), config.getType());
    }

    @Test
    public void testUpdateConfig_success() {
        // mock 数据
        ConfigDO dbConfig = randomConfigDO();
        configMapper.insert(dbConfig);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ConfigSaveReqVO reqVO = RandomUtils.randomPojo(ConfigSaveReqVO.class, o -> {
            o.setId(dbConfig.getId()); // 设置更新的 ID
        });

        // 调用
        configService.updateConfig(reqVO);
        // 校验是否更新正确
        ConfigDO config = configMapper.selectById(reqVO.getId()); // 获取最新的
        AssertUtils.assertPojoEquals(reqVO, config);
    }

    @Test
    public void testDeleteConfig_success() {
        // mock 数据
        ConfigDO dbConfig = randomConfigDO(o -> {
            o.setType(ConfigTypeEnum.CUSTOM.getType()); // 只能删除 CUSTOM 类型
        });
        configMapper.insert(dbConfig);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbConfig.getId();

        // 调用
        configService.deleteConfig(id);
        // 校验数据不存在了
        assertNull(configMapper.selectById(id));
    }

    @Test
    public void testDeleteConfig_canNotDeleteSystemType() {
        // mock 数据
        ConfigDO dbConfig = randomConfigDO(o -> {
            o.setType(ConfigTypeEnum.SYSTEM.getType()); // SYSTEM 不允许删除
        });
        configMapper.insert(dbConfig);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbConfig.getId();

        // 调用, 并断言异常
        AssertUtils.assertServiceException(() -> configService.deleteConfig(id), ErrorCodeConstants.CONFIG_CAN_NOT_DELETE_SYSTEM_TYPE);
    }

    // ========== 随机对象 ==========

    @SafeVarargs
    private static ConfigDO randomConfigDO(Consumer<ConfigDO>... consumers) {
        Consumer<ConfigDO> consumer = (o) -> {
            o.setType(randomEle(ConfigTypeEnum.values()).getType()); // 保证 key 的范围
        };
        return RandomUtils.randomPojo(ConfigDO.class, ArrayUtils.append(consumer, consumers));
    }
}
