package org.mendora.jdbc.condition;

import lombok.Builder;
import lombok.Data;
import org.mendora.jdbc.Operator;

/**
 * 条件载体
 *
 * @author menfre
 */
@Builder
@Data
public class Condition {
    /**
     * 条件符号
     */
    private Operator operator;
    /**
     * 字段
     */
    private String field;
    /**
     * 字段值
     */
    private Object val;
}
