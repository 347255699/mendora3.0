package org.mendora.structor;


import lombok.Getter;

public class CallValue<T, E extends Enum> {
    @Getter
    private T data;
    @Getter
    private E error;

    private CallValue(T data) {
        this.data = data;
    }

    private CallValue(E error) {
        this.error = error;
    }

    public boolean hasError() {
        return this.error != null;
    }

    public static <T> CallValue withOk(T data) {
        return new CallValue<>(data);
    }

    public static <E extends Enum> CallValue withError(E error) {
        return new CallValue<>(error);
    }
}
