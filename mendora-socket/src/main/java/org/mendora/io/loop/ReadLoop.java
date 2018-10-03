package org.mendora.io.loop;

import lombok.extern.slf4j.Slf4j;
import org.mendora.io.selection.SelectionEvent;
import org.mendora.io.selection.SelectionEventContext;
import org.mendora.io.selection.SelectionReadHandler;
import org.mendora.io.selection.SelectionEventType;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/10/3
 * desc:
 */
@Slf4j
public class ReadLoop extends AbstractLoop {
    private SelectionReadHandler readHandler;

    public static ReadLoop newReadLoop(Selector reader, SelectionReadHandler readHandler) {
        return new ReadLoop(reader, readHandler);
    }

    protected ReadLoop(Selector reader, SelectionReadHandler readHandler) {
        super(reader);
        this.readHandler = readHandler;
    }

    @Override
    protected void handle(SelectionKey selectionKey) {
        if (selectionKey.isValid() && selectionKey.isReadable()) {
            final SocketChannel clientChannel = (SocketChannel) selectionKey.channel();
            final SelectionEventContext skc = (SelectionEventContext) selectionKey.attachment();
            final ByteBuffer readBuf = skc.getReadBuf();
            try {
                int byteNum = clientChannel.read(readBuf);
                if (byteNum > 0) {
                    SelectionEventType.valOf(SelectionKey.OP_READ).ifPresent(selectionEventType -> {
                        readBuf.flip();
                        readBuf.mark();
                        if (!readHandler.handle(new SelectionEvent(skc.getRemoteAddress(), readBuf, selectionEventType))) {
                            readBuf.reset();
                        }
                    });
                    readBuf.compact();
                } else if (byteNum == -1) {
                    cancel(selectionKey);
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                cancel(selectionKey);
            }
        }
    }

    @Override
    public void run() {
        select();
    }
}
