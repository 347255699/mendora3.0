package org.mendora.generate.jdbc;

import org.mendora.generate.director.DirectorFactory;

/**
 * @author menfre
 * date: 2018/10/10
 * version: 1.0
 * desc:
 */
public class JsonDbSourcesProvider implements DbSourcesProvider {

    static DbSourcesProvider provider() {
        return new JsonDbSourcesProvider();
    }

    @Override
    public DbSources provide() {
        return DirectorFactory.director().getDbSources();
    }
}
