package org.mendora.generate.jdbc;

import lombok.Data;

/**
 * @author menfre
 * date: 2018/10/10
 * version: 1.0
 * desc:
 */
public interface DbSourcesProvider {

    /**
     * 数据源载体
     */
    @Data
    class DbSources {
        /**
         * 账户
         */
        private String user;
        /**
         * 密码
         */
        private String password;
        /**
         * 链接
         */
        private String url;
        /**
         * 驱动类
         */
        private String driverClass;
    }

    /**
     * 提供数据源
     *
     * @return 数据源载体
     */
    DbSources provide();
}
