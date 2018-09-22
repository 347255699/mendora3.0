package org.mendora.io.loop;

import lombok.extern.slf4j.Slf4j;
import org.mendora.io.handler.WriteHandler;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/21
 * desc:
 */
@Slf4j
public class WriteLoop extends AbstractLoop {
    private WriteHandler writeHandler;

    public WriteLoop(Selector selector) {
        super(selector);
    }

    private WriteLoop(Selector selector, WriteHandler writeHandler) {
        super(selector);
        this.writeHandler = writeHandler;
    }

    static WriteLoop newWriteLoop(Selector selector, WriteHandler writeHandler) {
        return new WriteLoop(selector, writeHandler);
    }

    @Override
    public void execute(Selector selector) {

    }

    @Override
    public void handle(SelectionKey selectionKey) {
        if (selectionKey.isValid() && selectionKey.isWritable()) {
            try {
                writeHandler.handle(selectionKey);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
