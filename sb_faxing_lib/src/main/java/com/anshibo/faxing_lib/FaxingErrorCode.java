package com.anshibo.faxing_lib;

import static com.anshibo.faxing_lib.ReaderConst.QC_STEP_PIN_VERIFY;
import static com.anshibo.faxing_lib.ReaderConst.QC_STEP_READ_ACCOUNT_BALANCE;
import static com.anshibo.faxing_lib.ReaderConst.QC_STEP_READ_FILE;
import static com.anshibo.faxing_lib.ReaderConst.QC_STEP_READ_QC;
import static com.anshibo.faxing_lib.ReaderConst.QC_STEP_READ_QCINIT_BEFORE;
import static com.anshibo.faxing_lib.ReaderConst.QC_STEP_READ_SE;

/**
 * @author zhaopanlong
 * @createtime：2020/5/7 下午2:32
 */
public enum FaxingErrorCode {
    QC_READ_SE_ERR(QC_STEP_READ_SE, "获取SE失败，请重试！"),
    QC_READ_FILE_ERR(QC_STEP_READ_FILE, "卡号读取失败，请确保卡片和设备放好后重新尝试！"),
    QC_QCINIT_BEFORE_ERR(QC_STEP_READ_QCINIT_BEFORE, "圈存初始化失败，请重试！"),
    QC_PIN_VERIFY_ERR(QC_STEP_PIN_VERIFY, "校验PIN失败，请重试！"),
    QC_READ_QC_ERR(QC_STEP_READ_QC, "圈存异常，请再次圈存进行补圈操作！"),
    QC_READ_ACCOUNT_BALANCE_ERR(QC_STEP_READ_ACCOUNT_BALANCE, "圈存异常...请再次圈存核对金额！"),
    ERR(000,"未定义异常");

    FaxingErrorCode(int errorCode, String errorDes) {
        this.errorCode = errorCode;
        this.errorDes = errorDes;
    }

    private int errorCode;
    private String errorDes;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDes() {
        return errorDes;
    }

    public void setErrorDes(String errorDes) {
        this.errorDes = errorDes;
    }
}
