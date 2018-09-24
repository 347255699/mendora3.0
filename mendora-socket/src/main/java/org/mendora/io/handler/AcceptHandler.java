package org.mendora.io.handler;

import org.mendora.io.loop.SelectionKeyContext;

/**
 * @author menfre
 * date: 2018/9/23
 * version: 1.0
 * desc:
 */
@FunctionalInterface
public interface AcceptHandler {
    void handle(SelectionKeyContext ctx);
}
