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
    private Selector reader;
    private boolean isReaderOpen = false;
    private SelectionAcceptHandler acceptHandler;
    private SelectionReadHandler readHandler;

    public static AcceptLoop newAcceptLoop(Selector acceptor, Selector reader, SelectionAcceptHandler acceptHandler, SelectionReadHandler readHandler) {
        return new AcceptLoop(acceptor, reader, acceptHandler, readHandler);
    }

    protected AcceptLoop(Selector acceptor, Selector reader, SelectionAcceptHandler acceptHandler, SelectionReadHandler readHandler) {
        super(acceptor);
        this.reader = reader;
        this.acceptHandler = acceptHandler;
        this.readHandler = readHandler;
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
                acceptChannel.register(reader, SelectionKey.OP_READ, skc);
                acceptHandler.handle(skc);
                if (!isReaderOpen) {
                    ReadLoop.newReadLoop(reader, readHandler).start();
                    isReaderOpen = true;
                } else {
                    reader.wakeup();
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
