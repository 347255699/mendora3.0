package org.mendora.io.loop;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/10/3
 * desc:
 */
@Slf4j
public class LoopSelectorProvider {
    private Selector[] selectors = new Selector[2];

    private LoopSelectorProvider() {
        for (int i = 0; i < 2; i++) {
            try {
                selectors[i] = SelectorProvider.provider().openSelector();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public static LoopSelectorProvider newLoopSelectorProvider() {
        return new LoopSelectorProvider();
    }

    public Selector acceptor() {
        return selectors[0];
    }

    public Selector reader() {
        return selectors[1];
    }

    public Selector writer() {
        return selectors[1];
    }
}
