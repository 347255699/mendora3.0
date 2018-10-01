package org.mendora.io;

import lombok.extern.slf4j.Slf4j;
import org.mendora.io.loop.AcceptLoop;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;

/**
 * @author menfre
 * date: 2018/9/30
 * version: 1.0
 * desc:
 */
@Slf4j
public class ServerReactor {

    private ServerSocketChannel ssc;
    private Selector selector;

    private ServerReactor(int port) {
        try {
            ssc = ServerSocketChannel.open();
            ssc.socket().bind(new InetSocketAddress(port));
            ssc.configureBlocking(false);
            selector = SelectorProvider.provider().openSelector();
            ssc.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static ServerReactor newServerReactor(int port) {
        return new ServerReactor(port);
    }

    public void start() {
        AcceptLoop.newAcceptLoop(selector).start();
    }

}
