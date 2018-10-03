package org.mendora.io.selection;

import java.nio.channels.SelectionKey;
import java.util.Optional;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/10/3
 * desc: 选择事件类型
 */
public enum SelectionEventType {
    ACCEPTED(SelectionKey.OP_ACCEPT, "接入成功"),
    CONNECTED(SelectionKey.OP_CONNECT, "连接成功"),
    READABLE(SelectionKey.OP_READ, "可读"),
    WRITEABLE(SelectionKey.OP_ACCEPT, "可写");

    private int val;
    private String msg;

    SelectionEventType(int val, String msg) {
        this.val = val;
        this.msg = msg;
    }

    public static Optional<SelectionEventType> valOf(int val) {
        SelectionEventType set = null;
        for (SelectionEventType _set : values()) {
            if (_set.val == val) {
                set = _set;
            }
        }
        return Optional.ofNullable(set);
    }
}
