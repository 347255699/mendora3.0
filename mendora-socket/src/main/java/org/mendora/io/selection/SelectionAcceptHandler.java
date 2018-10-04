package org.mendora.io.selection;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/10/3
 * desc: 连接接入事件，外部处理器
 */
@FunctionalInterface
public interface SelectionAcceptHandler {
    void handle(SelectionEventContext selectionEventContext);
}
