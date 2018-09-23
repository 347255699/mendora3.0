package org.mendora.io.loop;


import lombok.extern.slf4j.Slf4j;
import org.mendora.io.handler.ConnectOrAcceptHandler;
import org.mendora.io.handler.ReadHandler;
import org.mendora.io.handler.InterRWCAHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.Queue;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/9/21
 * desc: 循环器提供者
 */
@Slf4j
public class LoopProvider {
    /**
     * default selector number.
     */
    private static final int SELECTOR_NUM = 3;

    private ReadHandler readHandler;

    private ConnectOrAcceptHandler connectedHandler;

    private ConnectOrAcceptHandler acceptedHandler;

    private InterRWCAHandler interAcceptHandler;

    private InterRWCAHandler interConnectHandler;

    private InterRWCAHandler interReadHandler;

    private InterRWCAHandler interWriteHandler;

    /**
     * selector array.
     */
    private Selector[] selectors = new Selector[SELECTOR_NUM];

    /**
     * 是否有写事件需要啊处理
     *
     * @param sk  选择键
     * @param ctx 选择键上下文
     * @throws Exception
     */
    private void hasWriteEvent(SelectionKey sk, SelectionKeyContext ctx) throws Exception {
        // 写消息队列不为空
        if (!ctx.getWriteQueue().isEmpty()) {
            final int interestSet = sk.interestOps();
            final boolean isInterestedInWrite = (interestSet & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE;
            // 是否已经关注写事件
            if (sk.isValid() && !isInterestedInWrite) {
                sk.interestOps(interestSet | SelectionKey.OP_WRITE);
            }
            // 写循环器是否已经开启
            if (!writeSelector().isOpen()) {
                WriteLoop.newWriteLoop(writeSelector(), interWriteHandler).start();
            } else {
                // 唤醒写选择器
                writeSelector().wakeup();
            }
        } else {
            // 是否为长链接
            if (!ctx.iskeepLive()) {
                sk.channel().close();
                sk.cancel();
            }
        }
    }

    private LoopProvider() {
        for (int i = 0; i < SELECTOR_NUM; i++) {
            try {
                // 打开一组选择器
                selectors[i] = SelectorProvider.provider().openSelector();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        /**
         * 内部链接接入处理器
         * @param sk 选择键
         */
        interAcceptHandler = sk -> {
            final SelectionKeyContext ctx = new SelectionKeyContext();
            final ServerSocketChannel serverSocketChannel = (ServerSocketChannel) sk.channel();
            final SocketChannel sc = serverSocketChannel.accept();
            sc.configureBlocking(false);
            sc.register(readSelector(), SelectionKey.OP_READ, ctx);
            if (!readSelector().isOpen()) {
                // 启动可读循环器
                ReadLoop.newReadLoop(readSelector(), interReadHandler).start();
            }
            // 外部链接接入处理器
            acceptedHandler.handle(ctx);
            // 是否有写事件需要处理
            hasWriteEvent(sk, ctx);
        };

        /**
         * 内部链接成功处理器
         * @param sk 选择键
         */
        interConnectHandler = sk -> {
            final SelectionKeyContext ctx = new SelectionKeyContext();
            final SocketChannel sc = (SocketChannel) sk.channel();
            sc.register(readSelector(), SelectionKey.OP_READ, ctx);
            if (!readSelector().isOpen()) {
                // 启动可读循环器
                ReadLoop.newReadLoop(readSelector(), interReadHandler).start();
            }
            // 外部链接成功处理器
            connectedHandler.handle(ctx);
            // 是否有写事件需要处理
            hasWriteEvent(sk, ctx);
        };

        /**
         * 内部可读处理器
         * @param sk 选择键
         */
        interReadHandler = sk -> {
            final SelectionKeyContext ctx = (SelectionKeyContext) sk.attachment();
            final ByteBuffer readBuf = ctx.getReadBuf();
            final SocketChannel channel = (SocketChannel) sk.channel();
            int read = channel.read(readBuf);
            // 读取字节数大于0，有数据可读
            if (read > 0) {
                // 切换为读模式
                readBuf.flip();
                // 是否有剩余数据未处理(预防粘包)
                while (readBuf.hasRemaining()) {
                    // 记住当前buffer的position位置
                    readBuf.mark();
                    // 外部可读处理器处理是否成功
                    if (readHandler.handle(ctx)) {
                        // 是否有写事件需要处理
                        hasWriteEvent(sk, ctx);
                    } else {
                        // 处理器失败，恢复buffer的position位置(预防断包)
                        readBuf.reset();
                        break;
                    }
                }
                // 若有剩余字节，移动至头部并切换为写模式
                readBuf.compact();
            }
            // 客户端链接关闭
            if (read == -1) {
                log.warn("客户端(ip:{})链接已经关闭！", channel.getRemoteAddress());
                sk.cancel();
                channel.close();
            }
        };

        /**
         * 内部可写处理器
         * @param sk 选择键
         */
        interWriteHandler = sk -> {
            final SelectionKeyContext ctx = (SelectionKeyContext) sk.attachment();
            final Queue<ByteBuffer> queue = ctx.getWriteQueue();
            final SocketChannel socketChannel = (SocketChannel) sk.channel();
            // d待写出消息队列是否为空
            while (!queue.isEmpty()) {
                final ByteBuffer buf = queue.peek();
                // 切换为读模式
                buf.flip();
                // 写出消息
                socketChannel.write(buf);
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
                sk.interestOps(sk.interestOps() & ~SelectionKey.OP_WRITE);
                // 是否为长链接
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
     * 取得接入或链接选择器
     *
     * @return 接入或链接选择器
     */
    private Selector acceptOrConnectSelector() {
        return selectors[0];
    }

    /**
     * 取得读选择器
     *
     * @return 读选择器
     */
    private Selector readSelector() {
        return selectors[1];
    }


    /**
     * 取得写选择器
     *
     * @return 选择器
     */
    private Selector writeSelector() {
        return selectors[2];
    }

    /**
     * 执行循环器，用户客户端
     *
     * @param socketChannel    套接字通道
     * @param connectedHandler 链接触发器
     * @param readHandler      可读触发器
     * @throws Exception
     */
    public void execute(SocketChannel socketChannel, ConnectOrAcceptHandler connectedHandler, ReadHandler readHandler) throws Exception {
        this.connectedHandler = connectedHandler;
        this.readHandler = readHandler;
        // 注册链接事件
        socketChannel.register(acceptOrConnectSelector(), SelectionKey.OP_CONNECT);
        // 启动链接循环器
        ConnectLoop.newConnectLoop(acceptOrConnectSelector(), interConnectHandler).start();
    }

    /**
     * 启动循环器，用于服务端
     *
     * @param serverSocketChannel
     * @param acceptedHandler     接入触发器
     * @param readHandler         可读触发器
     * @throws Exception
     */
    public void execute(ServerSocketChannel serverSocketChannel, ConnectOrAcceptHandler acceptedHandler, ReadHandler readHandler) throws Exception {
        this.acceptedHandler = acceptedHandler;
        this.readHandler = readHandler;
        // 注册接入事件
        serverSocketChannel.register(acceptOrConnectSelector(), SelectionKey.OP_ACCEPT);
        // 启动接入循环器
        AcceptLoop.newAcceptLoop(acceptOrConnectSelector(), interAcceptHandler).start();
    }
}
