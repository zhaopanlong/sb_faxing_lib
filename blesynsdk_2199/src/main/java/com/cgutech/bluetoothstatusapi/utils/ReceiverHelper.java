//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cgutech.bluetoothstatusapi.utils;

import android.util.Log;

import com.cgutech.bluetoothstatusapi.callback.ReceiverFinish;
import com.cgutech.commonBt.log.LogHelperBt;
import com.cgutech.commonBt.util.UtilsBt;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReceiverHelper {
    private List<byte[]> receiveList;
    private int currentLength;
    private ReceiverFinish receiver;

    public ReceiverHelper(ReceiverFinish receiver) {
        this.resetReceiveLsit();
        this.receiver = receiver;
    }

    public void receive(byte[] data) {
        if (this.geCurrentPackgeLenth(data[0]) == data.length - 4 && data.length >= 4) {
            if (data[data.length - 1] != UtilsBt.getBcc(data)) {
                LogHelperBt.LogI("receive", "bcc校验错误");
                this.resetReceiveLsit();
            } else if (this.getPackageNum(data) != this.receiveList.size() + 1) {
                int packageNum = this.getPackageNum(data);
                if (packageNum == this.receiveList.size()) {
                    LogHelperBt.LogI("receive", "收到重复的包 num:" + this.getPackageNum(data) + ", list size:" + this.receiveList.size());
                    this.resetReceiveLsit();
                } else {
                    LogHelperBt.LogI("receive", "校验当前包数顺序错误 num:" + this.getPackageNum(data) + ", list size:" + this.receiveList.size());
                    this.resetReceiveLsit();
                }
            } else {
                if (this.isFirstPackage(data)) {
                    this.resetReceiveLsit();
                    this.currentLength = this.getPackgeSize(data[1], data[2]);
                }

                byte[] dataAdd = new byte[data.length - 4];
                System.arraycopy(data, 3, dataAdd, 0, data.length - 4);
                this.receiveList.add(dataAdd);
                if (this.currentLength == this.receiveList.size()) {
                    this.receiveAll();
                }
            }
        } else {
            LogHelperBt.LogI("send", "长度不符合：" + UtilsBt.bytesToHexString(data));
            this.resetReceiveLsit();
        }

    }

    private void receiveAll() {
        Log.d("send", "全部接收");
        int packageLen = this.receiveList.size();
        String channel;
        byte[] data;
        byte[] receiveData;
        if (packageLen == 1) {
            data = (byte[]) this.receiveList.get(0);
            this.resetReceiveLsit();
            receiveData = new byte[data.length - 1];
            receiveData = new byte[]{data[0]};
            channel = UtilsBt.bytesToHexString(receiveData);
            System.arraycopy(data, 1, receiveData, 0, data.length - 1);
            if (this.receiver != null) {
                this.receiver.onFinish(channel, receiveData);
            }
        } else {
            int len = 0;

            for (Iterator pos = this.receiveList.iterator(); pos.hasNext(); len += receiveData.length) {
                receiveData = (byte[]) ((byte[]) pos.next());
            }

            data = new byte[len];
            int pos1 = 0;

            byte[] tmp;
            for (Iterator receiveData1 = this.receiveList.iterator(); receiveData1.hasNext(); pos1 += tmp.length) {
                tmp = (byte[]) ((byte[]) receiveData1.next());
                System.arraycopy(tmp, 0, data, pos1, tmp.length);
            }

            receiveData = new byte[len - 1];
            System.arraycopy(data, 1, receiveData, 0, len - 1);
            byte[] tmpChannel = new byte[]{data[0]};
            channel = UtilsBt.bytesToHexString(tmpChannel);
            if (this.receiver != null) {
                this.receiver.onFinish(channel, receiveData);
            }
        }

        this.resetReceiveLsit();
    }

    private int geCurrentPackgeLenth(byte len) {
        return 15 & len;
    }

    private int getPackgeSize(byte h, byte l) {
        byte[] buf = new byte[]{(byte) (h & 127), l, 0, 0};
        return ByteBuffer.wrap(buf).getShort();
    }

    private boolean isFirstPackage(byte[] data) {
        return (data[1] & '耀') != 0;
    }

    private int getPackageNum(byte[] data) {
        return this.isFirstPackage(data) ? 1 : ByteBuffer.wrap(data, 1, 2).getShort();
    }

    public void resetReceiveLsit() {
        this.receiveList = new ArrayList();
    }
}
