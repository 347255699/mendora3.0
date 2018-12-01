package org.mendora.structor;

import java.util.Optional;

public enum SpecError3 {
    SUCCESS(0, "success"),

    FAILURE(-1, "ERROR");

    public final int code;
    public final String msg;

    SpecError3(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Optional<SpecError3> valOf(int code) {
        for (SpecError3 error : values()) {
            if (error.code == code) {
                return Optional.of(error);
            }
        }
        return Optional.empty();
    }
}
