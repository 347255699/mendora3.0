package org.mendora.jdbc;

import org.mendora.jdbc.condition.Condition;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询语句构建器
 *
 * @author menfre
 */
public class SelectBuilder {
    private final StringBuilder sqlBuilder = new StringBuilder();
    private List<Condition> conditions = new ArrayList<>();

    public SelectBuilder(String table) {
        sqlBuilder
                .append("SELECT * FROM ")
                .append(table);
    }

    public SelectBuilder where(Condition condition) {
        conditions.add(condition);
        return this;
    }

    public String build() {
        if (conditions.size() > 0) {
            final FixedCapacityStack<Condition> stack = FixedCapacityStack.of(conditions);
            final Condition first = stack.pop();
            sqlBuilder.append(" WHERE ");
            sqlBuilder.append(buildCondition(first));
            stack.forEach(condition ->
                    sqlBuilder
                            .append(" AND ")
                            .append(buildCondition(condition))
            );
        }
        return sqlBuilder.toString();
    }

    private StringBuilder buildCondition(Condition condition) {
        return new StringBuilder()
                .append(condition.getField())
                .append(" ")
                .append(condition.getOperator().val)
                .append(" ")
                .append(String.valueOf(condition.getVal()));
    }
}
