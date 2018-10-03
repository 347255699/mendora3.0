package org.mendora.io.loop;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mendora.io.Config.Config;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/10/3
 * desc:
 */
@RequiredArgsConstructor
public class SelectionEventContext {
    @Getter
    private final ByteBuffer readBuf = ByteBuffer.allocate(Config.READ_BUFFER_SIZE);
    @NonNull
    @Getter
    private InetSocketAddress remoteAddress;
}
