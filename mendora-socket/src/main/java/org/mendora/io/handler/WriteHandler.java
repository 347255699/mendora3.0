package org.mendora.io.handler;

import java.nio.channels.SelectionKey;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/21
 * desc:
 */
@FunctionalInterface
public interface WriteHandler {
    void handle(SelectionKey selectionKey) throws Exception;
}
