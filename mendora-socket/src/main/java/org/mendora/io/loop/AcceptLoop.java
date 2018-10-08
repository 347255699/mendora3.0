package org.mendora.io.loop;

import lombok.extern.slf4j.Slf4j;
import org.mendora.io.selection.SelectionAcceptHandler;
import org.mendora.io.selection.SelectionEventContext;
import org.mendora.io.selection.SelectionReadHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/10/3
 * desc: 链接接入监听器
 */
@Slf4j
public class AcceptLoop extends AbstractLoop {
    /**
     * 是否开启可读监听器
     */
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
                // 接入的连接通道
                SocketChannel acceptChannel = ssc.accept();
                // 非阻塞
                acceptChannel.configureBlocking(false);
                InetSocketAddress remoteAddress = (InetSocketAddress) acceptChannel.getRemoteAddress();
                // 新增选择事件上下文
                SelectionEventContext skc = new SelectionEventContext(remoteAddress);
                // 注册可读事件
                acceptChannel.register(selectorProvider.reader(), SelectionKey.OP_READ, skc);
                // 触发外部读处理器
                acceptHandler.handle(skc);
                if (!isReaderOpen) {
                    // 开启可读监听器
                    ReadLoop.newReadLoop(selectorProvider, readHandler).start();
                    isReaderOpen = true;
                } else {
                    // 唤醒监听器
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
