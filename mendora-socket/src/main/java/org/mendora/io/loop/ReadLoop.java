package org.mendora.io.loop;

import lombok.extern.slf4j.Slf4j;
import org.mendora.io.handler.ReadHandler;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/21
 * desc:
 */
@Slf4j
public class ReadLoop extends AbstractLoop {
    private ReadHandler readHandler;

    private ReadLoop(Selector selector, ReadHandler readHandler) {
        super(selector);
        this.readHandler = readHandler;
    }

    static ReadLoop newReadLoop(Selector selector, ReadHandler readHandler) {
        return new ReadLoop(selector, readHandler);
    }

    @Override
    protected void execute(Selector selector) {

    }

    @Override
    protected void handle(SelectionKey selectionKey) {
        if (selectionKey.isValid() && selectionKey.isReadable()) {
            try {
                readHandler.handle(selectionKey);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
