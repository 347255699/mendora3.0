package org.mendora.io.loop;

import lombok.extern.slf4j.Slf4j;
import org.mendora.io.handler.InterRWCAHandler;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * @author menfre
 * date: 2018/9/23
 * version: 1.0
 * desc: 链接建立事件循环器
 */
@Slf4j
public class ConnectLoop extends AbstractLoop {
    private InterRWCAHandler interConnectHandler;

    private ConnectLoop(Selector selector, InterRWCAHandler interConnectHandler) {
        super(selector);
        this.interConnectHandler = interConnectHandler;
    }

    static ConnectLoop newConnectLoop(Selector selector, InterRWCAHandler interConnectHandler) {
        return new ConnectLoop(selector, interConnectHandler);
    }


    @Override
    protected void execute(Selector selector) {

    }

    @Override
    protected void handle(SelectionKey selectionKey) {
        if (selectionKey.isValid() && selectionKey.isConnectable()) {
            try {
                interConnectHandler.handle(selectionKey);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
