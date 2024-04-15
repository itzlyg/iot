package cn.sinozg.applet.turbo.engine.exception;

import cn.sinozg.applet.turbo.engine.common.ErrorEnum;

public class ParamException extends TurboException {

    public ParamException(int errNo, String errMsg) {
        super(errNo, errMsg);
    }

    public ParamException(ErrorEnum errorEnum) {
        super(errorEnum);
    }
}

