package org.mendora.io.decoder;

import org.mendora.io.loop.SelectionKeyContext;


/**
 * @author menfre
 * date: 2018/9/22
 * version: 1.0
 * desc:
 */
@FunctionalInterface
public interface ReadDecoder {
    /**
     * decode from buffer, if decode successfully return true, else return false;
     *
     * @param ctx context for selection key.
     * @return boolean value
     */
    boolean decode(SelectionKeyContext ctx);
}
