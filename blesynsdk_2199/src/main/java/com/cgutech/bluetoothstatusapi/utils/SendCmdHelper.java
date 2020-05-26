//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cgutech.bluetoothstatusapi.utils;

import android.util.Log;

import com.cgutech.bluetoothstatusapi.callback.ReceiverFinish;
import com.cgutech.bluetoothstatusapi.callback.Sender;
import com.cgutech.bluetoothstatusapi.controller.BluetoothStateManager;
import com.cgutech.commonBt.log.LogHelperBt;
import com.cgutech.commonBt.util.UtilsBt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SendCmdHelper {
    private static final String TAG = "send";
    private List<byte[]> sendList;
    private List<byte[]> receiveList;
    private byte[] nextData;
    private final int MAXLENGTH = 15;
    private long mDelay = 200L;
    private int currentReceiveLength = 0;
    private boolean isSendSuccess = false;
    private boolean isDataSendFlag = false;
    private Sender sender;
    private ReceiverFinish receiver;

    public SendCmdHelper(ReceiverFinish receiver, Sender sender) {
        this.receiver = receiver;
        this.sender = sender;
        this.resetSendList();
        this.resetReceiveLsit();
    }

    public boolean isSendSuccess() {
        return this.isSendSuccess;
    }

    public void setmDelay(long ms) {
        this.mDelay = ms;
    }

    public void setDataSendMode(boolean flag) {
        this.isDataSendFlag = flag;
    }

    private boolean getDataSednMode() {
        return this.isDataSendFlag;
    }

    private void resetSendList() {
        this.sendList = new ArrayList();
    }

    private void resetReceiveLsit() {
        this.receiveList = new ArrayList();
    }

    public void send(byte[] cmd) {
        int len;
        if (cmd.length % 15 == 0) {
            len = cmd.length / 15;
        } else {
            len = cmd.length / 15 + 1;
        }

        if (len == 0) {
            len = 1;
        }

        this.sendPackage(1, cmd, len);
    }

    private void packageHandle(int num, byte[] data, int len) {
        byte[] ctl = new byte[2];
        if (num > len && len != 1) {
            this.sendAll();
        } else {
            if (num == 1) {
                this.resetSendList();
                ctl = this.getCtlFirst(len);
            } else {
                ctl = this.getCtlNotFirst(num);
            }

            int currentLength;
            if (data.length > 15) {
                currentLength = 15;
            } else {
                currentLength = data.length;
            }

            byte st = (byte) (80 | currentLength);
            byte[] packageData = new byte[4 + currentLength];
            packageData[0] = st;
            System.arraycopy(ctl, 0, packageData, 1, 2);
            System.arraycopy(data, 0, packageData, 3, currentLength);
            packageData[packageData.length - 1] = UtilsBt.getBcc(packageData);
            this.sendList.add(packageData);
            if (len == 1 && num == 1 && num == len) {
                this.sendAll();
            } else {
                this.nextData = new byte[data.length - currentLength];
                System.arraycopy(data, currentLength, this.nextData, 0, data.length - currentLength);
            }
        }

    }

    public void sendPackage(int num, byte[] data, int len) {
        byte[] loopData = data;
        if (num == len && len == 1) {
            this.packageHandle(num, data, len);
        } else {
            for (int i = num; i <= len + 1; ++i) {
                this.packageHandle(i, loopData, len);
                loopData = this.nextData;
            }
        }

    }

    public void receive(byte[] data) {
        if (this.geCurrentPackgeLenth(data[0]) == data.length - 4 && data.length >= 4) {
            if (data[data.length - 1] != UtilsBt.getBcc(data)) {
                LogHelperBt.LogI("receive", "bcc校验错误");
                this.resetReceiveLsit();
            } else if (this.getPackageNum(data[1], data[2]) != this.receiveList.size() + 1) {
                int packageNum = this.getPackageNum(data[1], data[2]);
                if (packageNum == this.receiveList.size()) {
                    LogHelperBt.LogI("receive", "收到重复的包 num:" + this.getPackageNum(data[1], data[2]) + ", list size:" + this.receiveList.size());
                } else {
                    LogHelperBt.LogI("receive", "校验当前包数错误 num:" + this.getPackageNum(data[1], data[2]) + ", list size:" + this.receiveList.size());
                    this.resetReceiveLsit();
                }
            } else {
                if (this.isFirstPackage(data[1])) {
                    this.resetReceiveLsit();
                    this.currentReceiveLength = this.getPackgeSize(data[1], data[2]);
                }

                byte[] dataAdd = new byte[data.length - 4];
                System.arraycopy(data, 3, dataAdd, 0, data.length - 4);
                this.receiveList.add(dataAdd);
                if (this.currentReceiveLength == this.receiveList.size()) {
                    this.receiveAll();
                }
            }
        } else {
            LogHelperBt.LogI("send", "长度不符合：" + UtilsBt.bytesToHexString(data));
            this.resetReceiveLsit();
        }

    }

    private byte[] getCtlNotFirst(int num) {
        byte[] tmp = new byte[]{(byte) (127 & num >> 8), (byte) (-1 & num)};
        return tmp;
    }

    private byte[] getCtlFirst(int len) {
        byte[] tmp = new byte[]{(byte) (len >> 8 | 128), (byte) (-1 & len)};
        return tmp;
    }

    private boolean isFirstPackage(byte st) {
        return (st & 128) >> 7 != 0;
    }

    private int getPackageNum(byte h, byte l) {
        int a1 = (h & 127) << 8;
        return this.isFirstPackage(h) ? 1 : a1 + l;
    }

    private int geCurrentPackgeLenth(byte len) {
        return 15 & len;
    }

    private int getPackgeSize(byte h, byte l) {
        return (h & 127) << 8 | l & 255;
    }

    private void sendAll() {
        LogHelperBt.LogI("send", "正在发送");
        (new Thread() {
            public void run() {
                if (SendCmdHelper.this.sendList == null) {
                    LogHelperBt.LogI("send", "sendList == null");
                } else {
                    while (SendCmdHelper.this.sendList.size() != 0) {
                        if (SendCmdHelper.this.sender != null && SendCmdHelper.this.sender.send((byte[]) SendCmdHelper.this.sendList.get(0))) {
                            SendCmdHelper.this.isSendSuccess = true;
                        } else {
                            LogHelperBt.LogI("send", "发送失败");
                            SendCmdHelper.this.isSendSuccess = false;
                        }

                        if (SendCmdHelper.this.sendList.size() == 0) {
                            break;
                        }

                        SendCmdHelper.this.sendList.remove(0);

                        try {
                            sleep(SendCmdHelper.this.mDelay);
                        } catch (InterruptedException var2) {
                            var2.printStackTrace();
                        }
                    }
                }

            }
        }).start();
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
            } else {
                LogHelperBt.LogI("sendCmdHelper", "收到OBU的消息，Receiver 为空. channel:" + channel + ", data:" + receiveData + ", state:" + BluetoothStateManager.instance().getmBluetoothState().getStateName());
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
            this.resetReceiveLsit();
            if (this.receiver != null) {
                this.receiver.onFinish(channel, receiveData);
            } else {
                LogHelperBt.LogI("sendCmdHelper", "收到OBU的消息，Receiver 为空. channel:" + channel + ", data:" + receiveData + ", state:" + BluetoothStateManager.instance().getmBluetoothState().getStateName());
            }
        }

    }
}
