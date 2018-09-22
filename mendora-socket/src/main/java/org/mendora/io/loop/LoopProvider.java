package org.mendora.io.loop;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.mendora.io.handler.AcceptHandler;
import org.mendora.io.handler.ReadHandler;
import org.mendora.io.handler.WriteHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/21
 * desc: event loop provider.
 */
@Slf4j
public class LoopProvider {
    /**
     * default selector number.
     */
    private static final int SELECTOR_NUM = 2;
    private static final int DEFAULT_BUFFER_CAPACITY = 1024;

    private AcceptHandler acceptHandler;

    private ReadHandler readHandler;

    private WriteHandler writeHandler;

    private static class SelectionKeyContext {
        @Getter
        private ConcurrentLinkedQueue<ByteBuffer> writeQueue = new ConcurrentLinkedQueue<>();
        @Getter
        private ByteBuffer readBuf = ByteBuffer.allocate(DEFAULT_BUFFER_CAPACITY);
    }

    /**
     * selector array.
     */
    private Selector[] selectors = new Selector[SELECTOR_NUM];

    private LoopProvider() {
        for (int i = 0; i < SELECTOR_NUM; i++) {
            try {
                // product selector.
                selectors[i] = SelectorProvider.provider().openSelector();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        acceptHandler = sc -> {
            sc.register(readSelector(), SelectionKey.OP_READ, new SelectionKeyContext());
            if (!readSelector().isOpen()) {
                ReadLoop.newReadLoop(readSelector(), readHandler).start();
            }
        };

        readHandler = sk -> {
            SelectionKeyContext ctx = (SelectionKeyContext) sk.attachment();
            ByteBuffer readBuf = ctx.getReadBuf();
            SocketChannel channel = (SocketChannel) sk.channel();
            int read = channel.read(readBuf);
            if (read > 0) {
                // todo need a Buffer Decoder
            }
            if (read == -1) {
                log.warn("client channel({}) was closed!", channel.getRemoteAddress());
                channel.close();
                sk.cancel();
            }
        };

        writeHandler = sc -> {

        };

    }

    public static LoopProvider newLoopProvider() {
        return new LoopProvider();
    }

    /**
     * provide selector for accept event.
     *
     * @return selector of accept
     */
    private Selector acceptSelector() {
        return selectors[0];
    }

    /**
     * provide selector for read event.
     *
     * @return selector of read
     */
    private Selector readSelector() {
        return selectors[1];
    }

    /**
     * provide selector for write event.
     *
     * @return selector of write
     */
    private Selector writeSelector() {
        return selectors[1];
    }

    public void execute(ServerSocketChannel serverSocketChannel) throws Exception {
        serverSocketChannel.register(acceptSelector(), SelectionKey.OP_ACCEPT);
        AcceptLoop.newAcceptLoop(acceptSelector(), acceptHandler).start();
    }

}
