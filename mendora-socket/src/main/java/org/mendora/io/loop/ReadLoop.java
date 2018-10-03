package org.mendora.io.loop;

import lombok.extern.slf4j.Slf4j;
import org.mendora.io.selection.SelectionEventContext;

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

    public static ReadLoop newReadLoop(Selector reader) {
        return new ReadLoop(reader);
    }

    protected ReadLoop(Selector reader) {
        super(reader);
    }

    @Override
    protected void handle(SelectionKey selectionKey) {
        if (selectionKey.isValid() && selectionKey.isReadable()) {
            SocketChannel clientChannel = (SocketChannel) selectionKey.channel();
            SelectionEventContext skc = (SelectionEventContext) selectionKey.attachment();
            ByteBuffer readBuf = skc.getReadBuf();
            try {
                int byteNum = clientChannel.read(readBuf);
                if (byteNum > 0) {
                    readBuf.flip();
                    readBuf.mark();
                    while (readBuf.hasRemaining()) {
                        final byte[] bytes = new byte[1024];
                        readBuf.get(bytes, 0, readBuf.limit());
                        log.info(new String(bytes));
                    }
                    readBuf.compact();
                }else if (byteNum == -1) {
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
