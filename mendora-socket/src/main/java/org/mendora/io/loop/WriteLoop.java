package org.mendora.io.loop;

import lombok.extern.slf4j.Slf4j;
import org.mendora.io.handler.InterRWCAHandler;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/21
 * desc: 可写事件循环器
 */
@Slf4j
public class WriteLoop extends AbstractLoop {
    private InterRWCAHandler interWriteHandler;

    private WriteLoop(Selector selector, InterRWCAHandler interWriteHandler) {
        super(selector);
        this.interWriteHandler = interWriteHandler;
    }

    static WriteLoop newWriteLoop(Selector selector, InterRWCAHandler interWriteHandler) {
        return new WriteLoop(selector, interWriteHandler);
    }

    @Override
    public void execute(Selector selector) {

    }

    @Override
    public void handle(SelectionKey selectionKey) {
        if (selectionKey.isValid() && selectionKey.isWritable()) {
            try {
                interWriteHandler.handle(selectionKey);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
