package org.mendora.jdbc.condition;

import org.mendora.jdbc.Operator;

/**
 * 条件构造工具
 *
 * @author menfre
 */
public class Conditions {
    /**
     * 大于
     *
     * @param field 字段
     * @param val   字段值
     * @return 条件载体
     */
    public static Condition greater(String field, Object val) {
        return Condition.builder()
                .field(field)
                .val(val)
                .operator(Operator.GREATER)
                .build();
    }
}
