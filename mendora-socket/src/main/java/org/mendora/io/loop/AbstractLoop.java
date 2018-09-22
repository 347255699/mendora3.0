package org.mendora.io.loop;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/21
 * desc:
 */
@Slf4j
public abstract class AbstractLoop extends Thread {
    private Selector selector;

    protected abstract void execute(Selector selector);

    protected abstract void handle(SelectionKey selectionKey);

    protected AbstractLoop(Selector selector) {
        this.selector = selector;
    }

    private void eventLoop(Set<SelectionKey> selectionKeys) {
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey selectionKey = iterator.next();
            iterator.remove();
            handle(selectionKey);
        }
    }

    protected void select() {
        while (selector.isOpen()) {
            try {
                int selected = selector.select();
                execute(selector);
                if (selected == 0) {
                    continue;
                }
                if (selector.isOpen()) {
                    eventLoop(selector.selectedKeys());
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
