package org.mendora.io.loop;

import lombok.Getter;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author menfre
 * date: 2018/9/22
 * version: 1.0
 * desc: 选择键上下文
 */
public class SelectionKeyContext {
    // 默认可读缓冲区开辟大小
    private static final int DEFAULT_BUFFER_CAPACITY = 1024;
    /**
     * 写消息队列
     */
    @Getter
    private ConcurrentLinkedQueue<ByteBuffer> writeQueue = new ConcurrentLinkedQueue<>();
    /**
     * 可读缓冲器
     */
    @Getter
    private ByteBuffer readBuf = ByteBuffer.allocate(DEFAULT_BUFFER_CAPACITY);

    /**
     * 通道状态(0 : 长链接, -1 : 短连接)
     */
    private int channelStatus = 0;
    private volatile Object attachment = null;

    /**
     * 关闭通道或链接(置换为短连接)
     */
    public void close() {
        channelStatus = -1;
    }

    /**
     * 是否需要保活(长链接)
     *
     * @return 布尔
     */
    public boolean iskeepLive() {
        return channelStatus == 0;
    }

    /**
     * 写出数据
     *
     * @param buf
     */
    public void write(ByteBuffer buf) {
        writeQueue.add(buf);
    }

    /**
     * 取得自定义的上下文
     *
     * @return
     */
    public final Object attachment() {
        return attachment;
    }

    /**
     * 设置自定义上下文
     *
     * @param attachment
     */
    public final void setAttachment(Object attachment) {
        this.attachment = attachment;
    }
}
