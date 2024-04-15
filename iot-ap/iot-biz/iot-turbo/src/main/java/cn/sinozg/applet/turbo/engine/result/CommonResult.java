package cn.sinozg.applet.turbo.engine.result;

import cn.sinozg.applet.turbo.engine.common.ErrorEnum;

public class CommonResult {

    private int errCode;
    private String errMsg;

    public CommonResult() {
        super();
    }

    public CommonResult(ErrorEnum errorEnum) {
        this.errCode = errorEnum.getErrNo();
        this.errMsg = errorEnum.getErrMsg();
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

}
