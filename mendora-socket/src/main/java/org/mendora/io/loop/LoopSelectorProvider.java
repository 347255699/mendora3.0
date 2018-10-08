package org.mendora.io.loop;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;

/**
 * @author menfre
 * @version 1.0
 * date: 2018/10/3
 * desc: 监听器所需绑定选择器提供者
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

    /**
     * 连接接入
     * @return
     */
    public Selector acceptor() {
        return selectors[0];
    }

    /**
     * 可读
     * @return
     */
    public Selector reader() {
        return selectors[1];
    }

    /**
     * 可写
     * @return
     */
    public Selector writer() {
        return selectors[1];
    }
}
