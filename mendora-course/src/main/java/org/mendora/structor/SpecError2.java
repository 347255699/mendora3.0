package org.mendora.structor;

import java.util.Optional;

public enum SpecError2 {
    SUCCESS(0, "success"),

    FAILURE(-1, "ERROR");

    public final int code;
    public final String msg;

    SpecError2(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Optional<SpecError2> valOf(int code) {
        for (SpecError2 error : values()) {
            if (error.code == code) {
                return Optional.of(error);
            }
        }
        return Optional.empty();
    }
}
