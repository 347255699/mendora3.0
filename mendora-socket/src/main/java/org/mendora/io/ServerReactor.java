package org.mendora.io;

import lombok.extern.slf4j.Slf4j;
import org.mendora.io.loop.AcceptLoop;
import org.mendora.io.loop.LoopSelectorProvider;
import org.mendora.io.selection.SelectionAcceptHandler;
import org.mendora.io.selection.SelectionReadHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/10/2
 * desc:
 */
@Slf4j
public class ServerReactor {
    /**
     * 监听端口号
     */
    private ServerSocketChannel ssc;
    private LoopSelectorProvider selectorProvider;
    private ServerReactor(int port) {
        try {
            ssc = ServerSocketChannel.open();
            ssc.socket().bind(new InetSocketAddress(port));
            ssc.configureBlocking(false);
            selectorProvider = LoopSelectorProvider.newLoopSelectorProvider();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static ServerReactor newServerReactor(int port) {
        return new ServerReactor(port);
    }

    public void open(SelectionAcceptHandler acceptHandler, SelectionReadHandler readHandler) throws Exception {
        ssc.register(selectorProvider.acceptor(), SelectionKey.OP_ACCEPT);
        AcceptLoop.newAcceptLoop(selectorProvider, acceptHandler, readHandler).start();
    }

}
