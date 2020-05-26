package com.anshibo.faxing_lib.bean;

import java.io.Serializable;

/**
 * Created by gcy on 2016/12/10.
 * 卡账户信息查询
 */

public class CardBalanceQueryBean implements Serializable{


    /**
     * operationStatus : 0 //是否是补圈
     * payType : 1//卡类型（1储存卡2记账卡）
     * clientType : 1//客户类型（1个人2集团）
     * cardAccountBalance : 498卡余额
     * clientName : 王甜
     * cardStatus : 600202101
     * carLicense : 浙F9G235
     * fillAmonut补圈金额
     */

    private int operationStatus;
    private String payType;
    private String clientType;
    private int cardAccountBalance;
    private String clientName;
    private String cardStatus;
    private String carLicense;
    private int fillAmout;
    private int card_balance;

    public int getCard_balance() {
        return card_balance;
    }

    public void setCard_balance(int card_balance) {
        this.card_balance = card_balance;
    }

    public int getFillAmout() {
        return fillAmout;
    }

    public void setFillAmout(int fillAmout) {
        this.fillAmout = fillAmout;
    }

    public int getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(int operationStatus) {
        this.operationStatus = operationStatus;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public int getCardAccountBalance() {
        return cardAccountBalance;
    }

    public void setCardAccountBalance(int cardAccountBalance) {
        this.cardAccountBalance = cardAccountBalance;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getCarLicense() {
        return carLicense;
    }

    public void setCarLicense(String carLicense) {
        this.carLicense = carLicense;
    }
}
