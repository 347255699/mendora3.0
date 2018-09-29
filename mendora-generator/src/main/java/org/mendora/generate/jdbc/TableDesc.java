package org.mendora.generate.jdbc;

import lombok.Data;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/26
 * desc: 表结构描述
 */
@Data
public class TableDesc {
    private String field;
    private String type;
    private String collation;
    private String isNull;
    private String key;
    private String defaultVal;
    private String extra;
    private String privileges;
    private String comment;
}
