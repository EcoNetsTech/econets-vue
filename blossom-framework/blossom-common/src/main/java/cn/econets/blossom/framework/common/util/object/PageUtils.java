package cn.econets.blossom.framework.common.util.object;


import cn.econets.blossom.framework.common.pojo.PageParam;

/**
 * {@link PageParam} 工具类
 *
 */
public class PageUtils {

    public static int getStart(PageParam pageParam) {
        return (pageParam.getPageNo() - 1) * pageParam.getPageSize();
    }

}
