package com.anshibo.faxing_lib.bean;

import java.io.Serializable;

/**
 * 创建日期：2018/10/13 on 13:48
 * 描述:
 * 作者:王甜
 */
public class QcCardSuccessBean implements Serializable{

    /**
     * msg : 圈存完成成功
     * completeDateTime : 2018-10-13T13:37:52.177
     * code : 1
     * cardAccountBalance : 13.73
     * etcUserName : 河南蒙牛****司
     * orderId : 2018101313375280000001
     * etcCarLicense : 豫A8**8N
     * cardBalance : 4.15
     * etcCardIdView : 4101 1649 2201 0202 2928
     * cardNo : 41011649220102022928
     */

    private String msg;
    private String completeDateTime;
    private int code;
    private String cardAccountBalance;
    private String etcUserName;
    private String orderId;
    private String etcCarLicense;
    private String cardBalance;
    private String etcCardIdView;
    private String cardNo;
    private String businessNo;//业务流水号

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCompleteDateTime() {
        return completeDateTime;
    }

    public void setCompleteDateTime(String completeDateTime) {
        this.completeDateTime = completeDateTime;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCardAccountBalance() {
        return cardAccountBalance;
    }

    public void setCardAccountBalance(String cardAccountBalance) {
        this.cardAccountBalance = cardAccountBalance;
    }

    public String getEtcUserName() {
        return etcUserName;
    }

    public void setEtcUserName(String etcUserName) {
        this.etcUserName = etcUserName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getEtcCarLicense() {
        return etcCarLicense;
    }

    public void setEtcCarLicense(String etcCarLicense) {
        this.etcCarLicense = etcCarLicense;
    }

    public String getCardBalance() {
        return cardBalance;
    }

    public void setCardBalance(String cardBalance) {
        this.cardBalance = cardBalance;
    }

    public String getEtcCardIdView() {
        return etcCardIdView;
    }

    public void setEtcCardIdView(String etcCardIdView) {
        this.etcCardIdView = etcCardIdView;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }
}
