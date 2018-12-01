package org.mendora.jdbc.condition;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

/**
 * 条件载体
 */
@Builder
@Data
public class Condition {
    /**
     * 条件符号
     */
    private Symbol symbol;
    /**
     * 字段
     */
    private String field;
    /**
     * 字段值
     */
    private Object val;

    /**
     * 条件符号枚举
     */
    public enum Symbol {
        GREATER(">");

        public final String val;

        Symbol(String val) {
            this.val = val;
        }

        /**
         * 名称映射
         *
         * @param val 条件符号名称
         * @return 条件符号枚举
         */
        public static Optional<Symbol> valOf(String val) {
            for (Symbol symbol : values()) {
                if (symbol.val.equals(val)) {
                    return Optional.of(symbol);
                }
            }
            return Optional.empty();
        }
    }
}
