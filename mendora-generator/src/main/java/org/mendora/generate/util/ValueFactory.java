package org.mendora.generate.util;

import java.sql.SQLException;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/26
 * desc:
 */
@FunctionalInterface
public interface ValueFactory {
    /**
     * 产生数值
     *
     * @param field
     * @return
     */
    Object val(String field) throws SQLException;
}
