package cn.econets.blossom.framework.desensitize.handler;

import cn.econets.blossom.framework.desensitize.DesensitizeTest;
import cn.econets.blossom.framework.desensitize.annotation.Address;
import cn.econets.blossom.framework.desensitize.core.base.handler.DesensitizationHandler;

/**
 * {@link Address} 的脱敏处理器
 *
 * 用于 {@link DesensitizeTest} 测试使用
 */
public class AddressHandler implements DesensitizationHandler<Address> {

    @Override
    public String desensitize(String origin, Address annotation) {
        return origin + annotation.replacer();
    }

}
