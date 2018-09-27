package org.mendora.generate.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/26
 * desc: 数据库配置信息
 */
@Slf4j
@Data
public class DbConfig {
    /**
     * 账户
     */
    private String user;
    /**
     * 密码
     */
    private String password;
    /**
     * 连接
     */
    private String url;
    /**
     * 驱动名称
     */
    private String driverClass;
}
