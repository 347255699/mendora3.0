package org.mendora.io.loop;

import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author menfre
 * date: 2018/9/22
 * version: 1.0
 * desc:
 */
public class SelectionKeyContext {
    private static final int DEFAULT_BUFFER_CAPACITY = 1024;
    @Getter
    private ConcurrentLinkedQueue<ByteBuffer> writeQueue = new ConcurrentLinkedQueue<>();
    @Getter
    private ByteBuffer readBuf = ByteBuffer.allocate(DEFAULT_BUFFER_CAPACITY);
    @Getter
    @Setter
    private SelectionKey selectionKey;
}
