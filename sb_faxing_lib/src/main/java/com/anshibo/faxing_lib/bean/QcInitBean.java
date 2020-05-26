package com.anshibo.faxing_lib.bean;

import java.io.Serializable;

/**
 * 创建日期：2018/9/25 on 13:19
 * 描述:
 * 作者:王甜
 */
public class QcInitBean implements Serializable{

    /**
     * msg : 获取圈存指令成功
     * code : 1
     * tradeNum : 34101019999901022018092511251001
     * shsmk : WDA74UACeDq0YlzgBQUpzhx4RI9JjbSxE+tYsjv5zfs=:yjQVmgAnr1jitEcGuUjMkTds3IenGUn+/g3zggPQxLztyS2hN5HaGmOlzXiotH7uAeMZbfpk3oOR71iIkqna/H31KBTWl6Gq5WWxRcuGJvfkhU8HAVAYilK1MsZmR94APrJEuUf4p74mg14bT6vNhw==:9mVaHc+cq5Q=
     */

    private String msg;
    private int code;
    private String tradeNum;
    private String shsmk;
    private boolean isEncry;

    public boolean isEncry() {
        return isEncry;
    }

    public void setEncry(boolean encry) {
        isEncry = encry;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTradeNum() {
        return tradeNum;
    }

    public void setTradeNum(String tradeNum) {
        this.tradeNum = tradeNum;
    }

    public String getShsmk() {
        return shsmk;
    }

    public void setShsmk(String shsmk) {
        this.shsmk = shsmk;
    }
}
