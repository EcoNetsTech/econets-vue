package cn.econets.blossom.framework.mybatis.core.enums;

import com.baomidou.mybatisplus.annotation.DbType;

/**
 * SQL相关常量类
 *
 */
public class SqlConstants {

    /**
     * 数据库的类型
     */
    public static DbType DB_TYPE;

    public static void init(DbType dbType) {
        DB_TYPE = dbType;
    }

}
