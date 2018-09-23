package org.mendora.io.loop;

import lombok.extern.slf4j.Slf4j;
import org.mendora.io.handler.AcceptOrConnectHandler;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author menfre
 * date: 2018/9/23
 * version: 1.0
 * desc:
 */
@Slf4j
public class ConnectLoop extends AbstractLoop {
    private AcceptOrConnectHandler connectHandler;

    private ConnectLoop(Selector selector, AcceptOrConnectHandler connectHandler) {
        super(selector);
        this.connectHandler = connectHandler;
    }

    static ConnectLoop newConnectLoop(Selector selector, AcceptOrConnectHandler connectHandler) {
        return new ConnectLoop(selector, connectHandler);
    }


    @Override
    protected void execute(Selector selector) {

    }

    @Override
    protected void handle(SelectionKey selectionKey) {
        if (selectionKey.isValid() && selectionKey.isConnectable()) {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            try {
                connectHandler.handle(socketChannel);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
