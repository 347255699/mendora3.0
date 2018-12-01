package org.mendora.jdbc;

import org.mendora.jdbc.condition.Condition;

import java.util.HashSet;
import java.util.Set;

/**
 * 查询语句构建器
 */
public class SelectBuilder {
    private final StringBuilder sqlBuilder = new StringBuilder();
    private Set<Condition> set = new HashSet<>();

    public SelectBuilder(String table) {
        sqlBuilder
                .append("SELECT * FROM ")
                .append(table)
                .append(" WHERE ");
    }

    public SelectBuilder where(Condition condition) {
        set.add(condition);
        return this;
    }

    public String build(){
        final FixedCapacityStack<Condition> stack = FixedCapacityStack.wrap(set);
        final Condition first = stack.pop();
        sqlBuilder.append(composeCondition(first));
        stack.forEach(condition ->
           sqlBuilder
                   .append("and")
                   .append(composeCondition(condition))
        );
        return sqlBuilder.toString();
    }

    private StringBuilder composeCondition(Condition condition){
        return new StringBuilder()
                .append(condition.getField())
                .append(" ")
                .append(condition.getSymbol().symbol)
                .append(" ")
                .append(String.valueOf(condition.getVal()));
    }
}
