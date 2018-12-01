package org.mendora.structor;

import java.util.Optional;

public enum SpecError {
    SUCCESS(0, "success"),

    FAILURE(-1, "ERROR");

    public final int code;
    public final String msg;

    SpecError(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Optional<SpecError> valOf(int code) {
        for (SpecError error : values()) {
            if (error.code == code) {
                return Optional.of(error);
            }
        }
        return Optional.empty();
    }
}
