package cn.sinozg.applet.turbo.engine.exception;


import cn.sinozg.applet.turbo.engine.common.ErrorEnum;

public class SuspendException extends ProcessException {

    public SuspendException(int errNo, String errMsg) {
        super(errNo, errMsg);
    }

    public SuspendException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public SuspendException(ErrorEnum errorEnum, String detailMsg) {
        super(errorEnum, detailMsg);
    }
}
