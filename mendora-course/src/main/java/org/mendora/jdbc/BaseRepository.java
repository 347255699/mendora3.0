package org.mendora.jdbc;

public abstract class BaseRepository<K, T> {

    protected abstract String table();

    public SelectBuilder select() {
        return new SelectBuilder(table());
    }
}
