package org.mendora.io.handler;

import org.mendora.io.loop.SelectionEvent;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/10/3
 * desc:
 */
@FunctionalInterface
public interface SelectionEventHandler {
    void handle(SelectionEvent se);
}
