package com.anshibo.faxing_lib.api;

import java.io.Serializable;

/**
 * 创建日期：2018/9/21 on 15:48
 * 描述:
 * 作者:王甜
 */
public class BaseReaderResponse implements Serializable {

    /**
     * success : true
     * message : null
     * errorCode : null
     * status : true
     * errorMsg : 卡账查询成功
     */

    private boolean success;
    private String message;
    private String errorCode;
    private String errorMsg;
    private String flag;
    private String code;
    private String codeMsg;
    private String msg;
    private boolean status;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCodeMsg() {
        return codeMsg;
    }

    public void setCodeMsg(String codeMsg) {
        this.codeMsg = codeMsg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
