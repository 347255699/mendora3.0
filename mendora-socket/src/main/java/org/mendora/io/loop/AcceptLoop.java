package org.mendora.io.loop;

import lombok.extern.slf4j.Slf4j;
import org.mendora.io.selection.SelectionEventContext;

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

    public static AcceptLoop newAcceptLoop(Selector acceptor, Selector reader) {
        return new AcceptLoop(acceptor, reader);
    }

    protected AcceptLoop(Selector acceptor, Selector reader) {
        super(acceptor);
        this.reader = reader;
    }

    @Override
    protected void handle(SelectionKey selectionKey) {
        if (selectionKey.isValid() && selectionKey.isAcceptable()) {
            ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
            try {
                SocketChannel acceptChannel = ssc.accept();
                acceptChannel.configureBlocking(false);
                InetSocketAddress remoteAddress = (InetSocketAddress) acceptChannel.getRemoteAddress();
                log.info("channel created. from: {}", remoteAddress);
                SelectionEventContext skc = new SelectionEventContext(remoteAddress);
                acceptChannel.register(reader, SelectionKey.OP_READ, skc);
                if (!isReaderOpen) {
                    ReadLoop.newReadLoop(reader).start();
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
