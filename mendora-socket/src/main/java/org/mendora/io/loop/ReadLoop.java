package org.mendora.io.loop;

import lombok.extern.slf4j.Slf4j;
import org.mendora.io.handler.InterRWAHandler;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/21
 * desc: 可读事件循环器
 */
@Slf4j
public class ReadLoop extends AbstractLoop {
    private InterRWAHandler interReadHandler;

    private ReadLoop(Selector selector, InterRWAHandler interReadHandler) {
        super(selector);
        this.interReadHandler = interReadHandler;
    }

    static ReadLoop newReadLoop(Selector selector, InterRWAHandler interReadHandler) {
        log.info("read loop start up!");
        return new ReadLoop(selector, interReadHandler);
    }

    @Override
    protected void execute(Selector selector) {

    }

    @Override
    protected void handle(SelectionKey selectionKey) {
        if (selectionKey.isValid() && selectionKey.isReadable()) {
            try {
                interReadHandler.handle(selectionKey);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
