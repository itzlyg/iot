package cn.sinozg.applet.turbo.engine.exception;

import cn.sinozg.applet.turbo.engine.common.ErrorEnum;

public class ProcessException extends TurboException {

    public ProcessException(int errNo, String errMsg) {
        super(errNo, errMsg);
    }

    public ProcessException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public ProcessException(ErrorEnum errorEnum, String detailMsg) {
        super(errorEnum, detailMsg);
    }
}
