package org.mendora.io.loop;

import lombok.extern.slf4j.Slf4j;
import org.mendora.io.selection.SelectionEventContext;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Queue;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/10/3
 * desc:
 */
@Slf4j
public class WriteLoop extends AbstractLoop {

    public static WriteLoop newWriteLoop(Selector writer) {
        return new WriteLoop(writer);
    }

    protected WriteLoop(Selector writer) {
        super(writer);
    }

    @Override
    protected void handle(SelectionKey selectionKey) {
        if (selectionKey.isValid() && selectionKey.isWritable()) {
            final SelectionEventContext ctx = (SelectionEventContext) selectionKey.attachment();
            final Queue<ByteBuffer> queue = ctx.getWriteQueue();
            final SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            // 待写出消息队列是否为空
            while (!queue.isEmpty()) {
                final ByteBuffer buf = queue.peek();
                // 切换为读模式
                buf.flip();
                try {
                    // 写出消息
                    socketChannel.write(buf);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                    cancel(selectionKey);
                }
                // 是否还有剩余数据
                if (buf.hasRemaining()) {
                    break;
                } else {
                    // 移出队列
                    queue.poll();
                }
            }
            // 队列为空，暂时无消息需要写出
            if (queue.isEmpty()) {
                // 取消写注册
                selectionKey.interestOps(selectionKey.interestOps() & ~SelectionKey.OP_WRITE);
                // 是否为长链接
                if (!ctx.isKeepAlive()) {
                    cancel(selectionKey);
                }
            }
        }
    }

    @Override
    public void run() {
        select();
    }
}
