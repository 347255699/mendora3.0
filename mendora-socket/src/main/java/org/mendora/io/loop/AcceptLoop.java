package org.mendora.io.loop;

import lombok.extern.slf4j.Slf4j;
import org.mendora.io.selection.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/10/3
 * desc:
 */
@Slf4j
public class AcceptLoop extends AbstractLoop {
    private boolean isReaderOpen = false;
    private SelectionAcceptHandler acceptHandler;
    private SelectionReadHandler readHandler;
    private LoopSelectorProvider selectorProvider;

    public static AcceptLoop newAcceptLoop(LoopSelectorProvider selectorProvider, SelectionAcceptHandler acceptHandler, SelectionReadHandler readHandler) {
        return new AcceptLoop(selectorProvider, acceptHandler, readHandler);
    }

    protected AcceptLoop(LoopSelectorProvider selectorProvider, SelectionAcceptHandler acceptHandler, SelectionReadHandler readHandler) {
        super(selectorProvider.acceptor());
        this.acceptHandler = acceptHandler;
        this.readHandler = readHandler;
        this.selectorProvider = selectorProvider;
    }

    @Override
    protected void handle(SelectionKey selectionKey) {
        if (selectionKey.isValid() && selectionKey.isAcceptable()) {
            ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
            try {
                SocketChannel acceptChannel = ssc.accept();
                acceptChannel.configureBlocking(false);
                InetSocketAddress remoteAddress = (InetSocketAddress) acceptChannel.getRemoteAddress();
                SelectionEventContext skc = new SelectionEventContext(remoteAddress);
                acceptChannel.register(selectorProvider.reader(), SelectionKey.OP_READ, skc);
                acceptHandler.handle(skc);
                if (!isReaderOpen) {
                    ReadLoop.newReadLoop(selectorProvider, readHandler).start();
                    isReaderOpen = true;
                } else {
                    selectorProvider.reader().wakeup();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void run() {
        select();
    }

}
