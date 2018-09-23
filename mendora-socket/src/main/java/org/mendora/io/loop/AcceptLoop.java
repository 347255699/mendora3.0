package org.mendora.io.loop;

import lombok.extern.slf4j.Slf4j;
import org.mendora.io.handler.InterRWCAHandler;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/21
 * desc: 链接接入事件循环器
 */
@Slf4j
public class AcceptLoop extends AbstractLoop {
    private InterRWCAHandler interAcceptHandler;

    private AcceptLoop(Selector selector, InterRWCAHandler interAcceptHandler) {
        super(selector);
        this.interAcceptHandler = interAcceptHandler;
    }

    static AcceptLoop newAcceptLoop(Selector selector, InterRWCAHandler interAcceptHandler) {
        return new AcceptLoop(selector, interAcceptHandler);
    }

    @Override
    protected void execute(Selector selector) {

    }

    @Override
    public void run() {
        select();
    }

    @Override
    protected void handle(SelectionKey selectionKey) {
        if (selectionKey.isValid() && selectionKey.isAcceptable()) {
            try {
                interAcceptHandler.handle(selectionKey);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
