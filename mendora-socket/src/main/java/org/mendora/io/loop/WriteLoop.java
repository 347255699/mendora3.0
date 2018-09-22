package org.mendora.io.loop;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/21
 * desc:
 */
public class WriteLoop extends AbstractLoop {
    public WriteLoop(Selector selector) {
        super(selector);
    }

    @Override
    public void execute(Selector selector) {

    }

    @Override
    public void handle(SelectionKey selectionKey) {

    }
}
