package org.mendora.structor;

public class DemoService {

    public CallValue<Demo, SpecError> doSomething() {
        return CallValue.withError(SpecError.SUCCESS);
    }

    public CallValue<Demo, SpecError2> doSomething2() {
        final CallValue<Demo, SpecError> callValue = doSomething();
        SpecError error = callValue.getError();
        if (callValue.hasError()) {
            return CallValue.withError(error);
        } else {
            return CallValue.withOk(callValue.getData());
        }
    }

    public CallValue<Demo, SpecError3> doSomething3() {
        final CallValue<Demo, SpecError2> callValue = doSomething2();
        if (callValue.hasError()) {
            return CallValue.withError(callValue.getError());
        } else {
            return CallValue.withOk(callValue.getData());
        }
    }

    public void doSomething4() {
        CallValue<Demo, SpecError3> demoSpecError3CallValue = doSomething3();
        if (demoSpecError3CallValue.hasError()) {
            System.out.println(demoSpecError3CallValue.getError());
        }
    }

    public static void main(String[] args) {
        new DemoService().doSomething4();
    }
}
