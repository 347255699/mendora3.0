package org.mendora.jdbc;

import org.mendora.jdbc.condition.Conditions;
import org.mendora.jdbc.vo.Demo;

public class DemoRepository extends BaseRepository<String, Demo> {
    @Override
    protected String table() {
        return "demo";
    }

    public String findById(){
        return select().where(Conditions.greater("age", 21)).build();
    }

    public static void main(String[] args) {
        System.out.println(new DemoRepository().findById());
    }
}
