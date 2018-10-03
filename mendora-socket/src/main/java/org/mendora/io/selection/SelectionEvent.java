package org.mendora.io.selection;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/10/3
 * desc:
 */
@Data
@AllArgsConstructor
public class SelectionEvent {
    private InetSocketAddress remoteAddress;
    private ByteBuffer readBuf;
    private SelectionEventType selectionEventType;
}
