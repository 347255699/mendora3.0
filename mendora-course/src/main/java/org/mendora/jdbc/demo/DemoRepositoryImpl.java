package org.mendora.jdbc.demo;

import org.mendora.jdbc.BaseRepository;
import org.mendora.jdbc.condition.Conditions;
import org.mendora.jdbc.demo.vo.Demo;

/**
 * 实现用例
 *
 * @author menfre
 */
public class DemoRepositoryImpl implements BaseRepository<String, Demo> {

    public String findById() {
        return select()
                .where(Conditions.greater("age", 21))
                .where(Conditions.greater("age", 23))
                .build();
    }

    @Override
    public String tableName() {
        return "demo";
    }

    public static void main(String[] args) {
        System.out.println(new DemoRepositoryImpl().findById());
    }
}
