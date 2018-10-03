package org.mendora.io.selection;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/10/3
 * desc:
 */
@FunctionalInterface
public interface SelectionReadHandler {
    boolean handle(SelectionEventContext selectionEventContext);
}
