package org.mendora.io.loop;

import lombok.extern.slf4j.Slf4j;
import org.mendora.io.selection.SelectionEventContext;
import org.mendora.io.selection.SelectionReadHandler;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
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
    private boolean isWriterOpen = false;
    private LoopSelectorProvider selectorProvider;

    public static ReadLoop newReadLoop(LoopSelectorProvider selectorProvider, SelectionReadHandler readHandler) {
        return new ReadLoop(selectorProvider, readHandler);
    }

    protected ReadLoop(LoopSelectorProvider selectorProvider, SelectionReadHandler readHandler) {
        super(selectorProvider.reader());
        this.readHandler = readHandler;
        this.selectorProvider = selectorProvider;
    }

    /**
     * 是否有写事件需要啊处理
     *
     * @param sk 选择键
     * @throws Exception
     */
    private void hasWriteEvent(SelectionKey sk) {
        SelectionEventContext ctx = (SelectionEventContext) sk.attachment();
        // 写消息队列不为空
        if (!ctx.getWriteQueue().isEmpty()) {
            final boolean isInterestedInWrite = sk.isValid() && (sk.interestOps() & SelectionKey.OP_WRITE) != 0;
            // 是否已经关注写事件
            if (!isInterestedInWrite) {
                sk.interestOps(sk.interestOps() | SelectionKey.OP_WRITE);
            }
            // 写循环器是否已经开启
            if (!isWriterOpen) {
                WriteLoop.newWriteLoop(selectorProvider.writer()).start();
                isWriterOpen = true;
            } else {
                // 唤醒写选择器
                selectorProvider.writer().wakeup();
            }
        }
        // 是否为长链接
        if (!ctx.isKeepAlive()) {
            cancel(sk);
        }

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
                    readBuf.flip();
                    readBuf.mark();
                    if (!readHandler.handle(skc)) {
                        readBuf.reset();
                    }
                    readBuf.compact();
                    hasWriteEvent(selectionKey);
                } else if (byteNum == -1) {
                    cancel(selectionKey);
                }
            } catch (Exception e) {
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
