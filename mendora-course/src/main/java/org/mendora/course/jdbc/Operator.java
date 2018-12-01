package org.mendora.course.jdbc;

import java.util.Optional;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/11/30
 * desc: 运算符
 */
public enum Operator {
    /**
     * 运算符
     */
    EQUAL("="),
    NO_EQUAL("!="),
    GREATER(">"),
    LESS("<"),
    GREATER_THAN_EQUAL(">="),
    LESS_THAN_EQUAL("<="),
    BETWEEN("BETWEEN"),
    LIKE("LIKE"),
    IN("IN"),
    AND("AND"),
    OR("OR"),
    NO("NO");

    public final String val;

    Operator(String val) {
        this.val = val;
    }

    public static Optional<Operator> valOf(String val) {
        for (Operator operator : values()) {
            if (operator.val.equals(val)) {
                return Optional.of(operator);
            }
        }
        return Optional.empty();
    }
}
