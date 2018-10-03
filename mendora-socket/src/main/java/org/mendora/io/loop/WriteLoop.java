package org.mendora.io.loop;

import org.mendora.io.selection.SelectionEventContext;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/10/3
 * desc:
 */
public class WriteLoop extends AbstractLoop {
    protected WriteLoop(Selector selector) {
        super(selector);
    }

    @Override
    protected void handle(SelectionKey selectionKey) {
        if (selectionKey.isValid() && selectionKey.isWritable()) {
            SelectionEventContext skc = (SelectionEventContext) selectionKey.attachment();
            if (skc.getWriteQueue().isEmpty()) {
                if (!skc.isKeepLive()) {
                    cancel(selectionKey);
                }
            }
        }
    }
}
