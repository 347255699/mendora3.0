package org.mendora.io.loop;


import lombok.extern.slf4j.Slf4j;
import org.mendora.io.decoder.ReadDecoder;
import org.mendora.io.handler.AcceptOrConnectHandler;
import org.mendora.io.handler.ReadHandler;
import org.mendora.io.handler.WriteHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Queue;

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

    private AcceptOrConnectHandler acceptOrConnectHandler;

    private ReadHandler readHandler;

    private WriteHandler writeHandler;

    private ReadDecoder readDecoder;

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

        acceptOrConnectHandler = sc -> {
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
                readBuf.flip();
                readBuf.mark();
                if (readDecoder.decode(ctx)) {
                    readBuf.compact();
                    if (!ctx.getWriteQueue().isEmpty()) {
                        if (sk.isValid() && !sk.isWritable()) {
                            sk.interestOps(sk.interestOps() | SelectionKey.OP_WRITE);
                        }
                        if (!writeSelector().isOpen()) {
                            WriteLoop.newWriteLoop(writeSelector(), writeHandler).start();
                        } else {
                            writeSelector().wakeup();
                        }
                    } else {
                        if (!ctx.iskeepLive()) {
                            sk.cancel();
                            channel.close();
                        }
                    }
                } else {
                    readBuf.reset();
                }
            }
            if (read == -1) {
                log.warn("client channel({}) was closed!", channel.getRemoteAddress());
                channel.close();
                sk.cancel();
            }
        };

        writeHandler = sk -> {
            final SelectionKeyContext ctx = (SelectionKeyContext) sk.attachment();
            final Queue<ByteBuffer> queue = ctx.getWriteQueue();
            final SocketChannel socketChannel = (SocketChannel) sk.channel();
            while (!queue.isEmpty()) {
                final ByteBuffer buf = queue.peek();
                // switch read mode
                buf.flip();
                socketChannel.write(buf);
                if (buf.hasRemaining()) {
                    break;
                } else {
                    queue.poll();
                }
            }
            if (queue.isEmpty()) {
                // 取消写注册
                sk.interestOps(sk.interestOps() & ~SelectionKey.OP_WRITE);
                if (!ctx.iskeepLive()) {
                    sk.cancel();
                    socketChannel.close();
                }
            }
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
     * provide selector for connect event.
     *
     * @return selector of connect
     */
    private Selector connectSelector() {
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

    public void execute(SocketChannel socketChannel, ReadDecoder readDecoder) throws Exception {
        this.readDecoder = readDecoder;
        socketChannel.register(acceptSelector(), SelectionKey.OP_CONNECT);
        ConnectLoop.newConnectLoop(connectSelector(), acceptOrConnectHandler).start();
    }

    public void execute(ServerSocketChannel serverSocketChannel, ReadDecoder readDecoder) throws Exception {
        this.readDecoder = readDecoder;
        serverSocketChannel.register(acceptSelector(), SelectionKey.OP_ACCEPT);
        AcceptLoop.newAcceptLoop(acceptSelector(), acceptOrConnectHandler).start();
    }
}
