package org.mendora.io.selection;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mendora.io.Config.SocketConfig;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/10/3
 * desc:
 */
@RequiredArgsConstructor
public class SelectionEventContext {
    @Getter
    /**
     * 可读缓冲区
     */
    private final ByteBuffer readBuf = ByteBuffer.allocate(SocketConfig.READ_BUFFER_SIZE);
    @NonNull
    @Getter
    /**
     * 远端地址
     */
    private InetSocketAddress remoteAddress;
    @Getter
    /**
     * 待写出队列
     */
    private ConcurrentLinkedQueue<ByteBuffer> writeQueue = new ConcurrentLinkedQueue<>();

    @Getter
    /**
     * 是否需要保活(长连接)
     */
    private boolean keepAlive = false;

    /**
     * 写入数据
     *
     * @param writeBuf
     */
    public void write(ByteBuffer writeBuf) {
        writeQueue.add(writeBuf);
    }

    /**
     * 关闭连接
     */
    public void close() {
        if (isKeepAlive()) {
            keepAlive = false;
        }
    }

    /**
     * 保持为长连接
     */
    public void keepAlive() {
        keepAlive = true;
    }
}
