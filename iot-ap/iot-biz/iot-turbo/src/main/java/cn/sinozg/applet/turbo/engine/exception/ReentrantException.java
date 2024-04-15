package cn.sinozg.applet.turbo.engine.exception;

import cn.sinozg.applet.turbo.engine.common.ErrorEnum;

public class ReentrantException extends ProcessException {

    public ReentrantException(int errNo, String errMsg) {
        super(errNo, errMsg);
    }

    public ReentrantException(ErrorEnum errorEnum) {
        super(errorEnum);
    }
}
