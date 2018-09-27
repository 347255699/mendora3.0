package org.mendora.generate.jdbc;

import lombok.Data;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/26
 * desc:
 */
@Data
public class TableDesc {
    private String field;
    private String type;
    private String isNull;
    private String key;
    private String defaultVal;
    private String extra;
}
