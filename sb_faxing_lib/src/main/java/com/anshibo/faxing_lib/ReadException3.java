package com.anshibo.faxing_lib;

public class ReadException3 extends Exception {
    private FaxingErrorCode errCode;
    private String errMsg;

    public ReadException3(FaxingErrorCode errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public ReadException3(FaxingErrorCode errCode) {
        this.errCode = errCode;
    }

    public ReadException3(String errMsg) {
        this.errMsg = errMsg;
        this.errCode = FaxingErrorCode.ERR;
    }

    public FaxingErrorCode getErrCode() {
        return errCode;
    }

    public void setErrCode(FaxingErrorCode errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
