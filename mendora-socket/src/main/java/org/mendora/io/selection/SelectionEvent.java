package org.mendora.io.loop;

import lombok.Builder;
import lombok.ToString;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/10/3
 * desc:
 */
@Builder
@ToString
public class SelectionEvent {

    private InetSocketAddress remoteAddress;
    private ByteBuffer readBuf;
    private SelectionEventType selectionEventType;
}
