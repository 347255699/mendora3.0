package org.mendora.io.loop;

import lombok.extern.slf4j.Slf4j;
import org.mendora.io.handler.AcceptOrConnectHandler;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/21
 * desc:
 */
@Slf4j
public class AcceptLoop extends AbstractLoop {
    private AcceptOrConnectHandler acceptHandler;

    private AcceptLoop(Selector selector, AcceptOrConnectHandler acceptHandler) {
        super(selector);
        this.acceptHandler = acceptHandler;
    }

    static AcceptLoop newAcceptLoop(Selector selector, AcceptOrConnectHandler acceptHandler) {
        return new AcceptLoop(selector, acceptHandler);
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
            ServerSocketChannel subServerSocketChannel = (ServerSocketChannel) selectionKey.channel();
            try {
                SocketChannel socketChannel = subServerSocketChannel.accept();
                acceptHandler.handle(socketChannel);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
