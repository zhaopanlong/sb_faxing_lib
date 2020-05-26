//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cgutech.newbluetoothapi;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.cgutech.bluetoothstatusapi.callback.ConnectCallback;
import com.cgutech.bluetoothstatusapi.callback.Receiver;
import com.cgutech.bluetoothstatusapi.callback.ScanCallback;
import com.cgutech.bluetoothstatusapi.constant.HasSe;
import com.cgutech.bluetoothstatusapi.controller.BluetoothStateManager;
import com.cgutech.bluetoothstatusapi.exception.ErrorStateException;
import com.cgutech.bluetoothstatusapi.service.BluetoothStateListener;
import com.cgutech.bluetoothstatusapi.utils.Utils;
import com.cgutech.commonBt.log.LogHelperBt;
import com.cgutech.commonBt.util.UtilsBt;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class BluetoothObuHandler {
    private static final String TAG = "BluetoothHelper";
    private boolean mIsConnect = false;
    private BluetoothObuCallback mCallback;
    private int sendCmdDelay = 10;
    private int reCount = 5;
    private int timeOut = 20000;
    private int frameTimeout = 400;
    private int frameReCount = 5;
    private static BluetoothObuHandler mInstance;
    private ScanCallback scanCallback = new ScanCallback() {
        public void onScan(BluetoothDevice device, int rssi) {
            if (BluetoothObuHandler.this.mCallback != null && !BluetoothObuHandler.this.scanResultMap.containsKey(device.getAddress())) {
                ScanResult scanResult = new ScanResult();
                scanResult.setDevice(device);
                scanResult.setRssi(rssi);
                BluetoothObuHandler.this.scanResultMap.put(device.getName(), scanResult);
            }

        }

        public void onScanTimeout() {
            if (BluetoothObuHandler.this.mCallback != null) {
                Iterator var6 = BluetoothObuHandler.this.scanResultMap.entrySet().iterator();

                while(var6.hasNext()) {
                    Entry<String, ScanResult> entry = (Entry)var6.next();
                    BluetoothObuHandler.this.scanResultList.add(entry.getValue());
                }

                Log.i("测试", "超时");
                BluetoothObuHandler.this.latch.countDown();
            }

        }
    };
    private ConnectCallback connectCallback = new ConnectCallback() {
        public void onConnect() {
            BluetoothObuHandler.this.mIsConnect = true;
            BluetoothObuHandler.this.connectState = BluetoothObuHandler.CONNECTED;
            BluetoothObuHandler.this.latch.countDown();
        }

        public void onDisconnect() {
            BluetoothObuHandler.this.mIsConnect = false;
            BluetoothObuHandler.this.connectState = BluetoothObuHandler.DISCONNECT;
            BluetoothObuHandler.this.latch.countDown();
        }

        public void onTimeout() {
            BluetoothObuHandler.this.connectState = BluetoothObuHandler.TIMEOUT;
            BluetoothObuHandler.this.latch.countDown();
        }

        public void onError(String error) {
            BluetoothObuHandler.this.connectState = BluetoothObuHandler.ERROR;
            BluetoothObuHandler.this.latch.countDown();
        }
    };
    private Receiver receiver = new Receiver() {
        public boolean onRecv(String channel, byte[] data) {
            if (BluetoothObuHandler.this.mCallback != null) {
                ReceiveResult receiveResult = new ReceiveResult();
                if (data != null) {
                    String rData = channel + Utils.bytesToHexString(data);
                    String bcc;
                    if (rData.startsWith("b755")) {
                        int len = Integer.parseInt(rData.substring(4, 8), 16);
                        if (len != 11) {
                            HasSe.hasSe = false;
                            receiveResult.setChannel("B7");
                            receiveResult.setCommand("55000B00FFFFFFFFFFFFFFFF9000CE");
                            BluetoothObuHandler.this.commandQueue.offer(receiveResult);
                        } else {
                            bcc = rData.substring(10, 14);
                            if (!bcc.equals("4101")) {
                                HasSe.hasSe = false;
                                receiveResult.setChannel("B7");
                                receiveResult.setCommand("55000B00FFFFFFFFFFFFFFFF9000CE");
                                BluetoothObuHandler.this.commandQueue.offer(receiveResult);
                            } else {
                                HasSe.hasSe = true;
                                receiveResult.setChannel(channel);
                                receiveResult.setCommand(Utils.bytesToHexString(data));
                                BluetoothObuHandler.this.commandQueue.offer(receiveResult);
                            }
                        }
                    } else if (rData.startsWith("b3")) {
                        if (!HasSe.hasSe && HasSe.hasChange) {
                            HasSe.hasChange = false;
                            String returnCmd = Utils.bytesToHexString(data);
                            returnCmd = "00" + returnCmd.substring(6) + "3A";
                            returnCmd = "3C" + Utils.shortToHexString(returnCmd.length() / 2) + returnCmd;
                            bcc = Utils.getBCC(Utils.hexStringTobytes(returnCmd));
                            returnCmd = returnCmd + bcc;
                            receiveResult.setChannel("b7");
                            receiveResult.setCommand(returnCmd.toLowerCase());
                            BluetoothObuHandler.this.commandQueue.offer(receiveResult);
                        } else {
                            receiveResult.setChannel(channel);
                            receiveResult.setCommand(Utils.bytesToHexString(data));
                            BluetoothObuHandler.this.commandQueue.offer(receiveResult);
                        }
                    } else {
                        receiveResult.setChannel(channel);
                        receiveResult.setCommand(Utils.bytesToHexString(data));
                        BluetoothObuHandler.this.commandQueue.offer(receiveResult);
                    }
                } else {
                    receiveResult.setChannel(channel);
                    receiveResult.setCommand((String)null);
                    BluetoothObuHandler.this.commandQueue.offer(receiveResult);
                }
            }

            return true;
        }

        public boolean onSendTimeout(byte[] data) {
            ReceiveResult receiveResult = new ReceiveResult();
            if (data != null) {
                String sData = Utils.bytesToHexString(data);
                if (sData != null) {
                    String channel = sData.substring(0, 2);
                    String rData = sData.substring(2);
                    receiveResult.setChannel(channel);
                    receiveResult.setCommand(rData);
                    BluetoothObuHandler.this.commandQueue.offer(receiveResult);
                }
            } else {
                receiveResult.setChannel((String)null);
                receiveResult.setCommand((String)null);
                BluetoothObuHandler.this.commandQueue.offer(receiveResult);
            }

            return false;
        }
    };
    private Map<String, ScanResult> scanResultMap = new HashMap();
    private List<ScanResult> scanResultList;
    private CountDownLatch latch = new CountDownLatch(1);
    private int connectState;
    private static int CONNECTED = 0;
    private static int DISCONNECT = 1;
    private static int TIMEOUT = 2;
    private static int ERROR = 3;
    private BlockingQueue<ReceiveResult> commandQueue = new LinkedBlockingQueue(2);

    private BluetoothObuHandler() {
    }

    public static BluetoothObuHandler getInstance() {
        if (mInstance == null) {
            Class var0 = BluetoothObuHandler.class;
            synchronized(BluetoothObuHandler.class) {
                if (mInstance == null) {
                    mInstance = new BluetoothObuHandler();
                }
            }
        }

        return mInstance;
    }

    public int initializeObu(Context context, BluetoothObuCallback callback) {
        this.mCallback = callback;
        Intent intent = new Intent();
        intent.setClass(context, BluetoothStateListener.class);
        context.startService(intent);
        BluetoothStateManager.instance().setContext(context);
        return 0;
    }

    public void setGloableReceiver(final com.cgutech.newbluetoothapi.Receiver gloableReceiver) {
        BluetoothStateManager.instance().setGloableReceiver(new Receiver() {
            public boolean onRecv(String channel, byte[] data) {
                return gloableReceiver.onRecv(channel, data);
            }

            public boolean onSendTimeout(byte[] data) {
                return false;
            }
        });
    }

    public ReceiveResult sendObuCmd(String channel, String command) throws ErrorStateException {
        String cmd = channel + command;
        int timeOut = 20000;
        if (!HasSe.hasSe) {
            cmd = cmd.toUpperCase();
            if (cmd.startsWith("A7C3")) {
                cmd = "A301" + Utils.intToByteString(cmd.length() / 2 - 5) + cmd.substring(8, cmd.length() - 1);
                HasSe.hasChange = true;
            }
        }

        ReceiveResult result = null;
        this.commandQueue = new LinkedBlockingQueue(2);
        BluetoothStateManager.instance().getmBluetoothState().send(Utils.hexStringTobytes(cmd), this.sendCmdDelay, this.reCount, this.frameReCount, timeOut, this.frameTimeout, this.receiver, this.connectCallback);

        try {
            result = (ReceiveResult)this.commandQueue.take();
            return result;
        } catch (InterruptedException var10) {
            var10.printStackTrace();
            return result;
        } finally {
            ;
        }
    }

    public ReceiveResult sendObuCmd(String channel, String command, int reCount, int sendTimeout) throws ErrorStateException {
        String cmd = channel + command;
        if (!HasSe.hasSe) {
            cmd = cmd.toUpperCase();
            if (cmd.startsWith("A7C3")) {
                cmd = "A301" + Utils.intToByteString(cmd.length() / 2 - 5) + cmd.substring(4, cmd.length() - 1);
                HasSe.hasChange = true;
            }
        }

        ReceiveResult result = null;
        this.commandQueue = new LinkedBlockingQueue(2);
        BluetoothStateManager.instance().getmBluetoothState().send(Utils.hexStringTobytes(cmd), this.sendCmdDelay, reCount, this.frameReCount, sendTimeout, this.frameTimeout, this.receiver, this.connectCallback);

        try {
            result = (ReceiveResult)this.commandQueue.take();
            return result;
        } catch (InterruptedException var11) {
            var11.printStackTrace();
            return result;
        } finally {
            ;
        }
    }

    public ReceiveResult sendObuCmd(String channel, String command, int reCount, int sendTimeout, int reFrameCount, int frameTimeout) throws ErrorStateException {
        String cmd = channel + command;
        if (!HasSe.hasSe) {
            cmd = cmd.toUpperCase();
            if (cmd.startsWith("A7C3")) {
                cmd = "A301" + Utils.intToByteString(cmd.length() / 2 - 5) + cmd.substring(4, cmd.length() - 1);
                HasSe.hasChange = true;
            }
        }

        ReceiveResult result = null;
        this.commandQueue = new LinkedBlockingQueue(2);
        BluetoothStateManager.instance().getmBluetoothState().send(Utils.hexStringTobytes(cmd), this.sendCmdDelay, reCount, reFrameCount, sendTimeout, frameTimeout, this.receiver, this.connectCallback);

        try {
            result = (ReceiveResult)this.commandQueue.take();
            return result;
        } catch (InterruptedException var13) {
            var13.printStackTrace();
            return result;
        } finally {
            ;
        }
    }

    public ReceiveResult sendEsamCmd(String command) {
        String cmd = "A401" + Utils.intToByteString(command.length() / 2) + command;
        ReceiveResult result = null;

        try {
            this.commandQueue = new LinkedBlockingQueue(2);
            BluetoothStateManager.instance().getmBluetoothState().send(UtilsBt.hexStringTobytes(cmd), this.sendCmdDelay, this.reCount, this.frameReCount, this.timeOut, this.frameTimeout, this.receiver, this.connectCallback);
            result = (ReceiveResult)this.commandQueue.take();
            return result;
        } catch (ErrorStateException var9) {
            var9.printStackTrace();
            return result;
        } catch (InterruptedException var10) {
            var10.printStackTrace();
            return result;
        } finally {
            ;
        }
    }

    public ReceiveResult sendEncryptEsamCmd(String command) {
        String tempCmd = "C500" + Utils.intToByteString(command.length() / 2) + command;
        String cmd = "A7" + tempCmd + Utils.getBCC(UtilsBt.hexStringTobytes(tempCmd));
        ReceiveResult result = null;

        try {
            this.commandQueue = new LinkedBlockingQueue(2);
            BluetoothStateManager.instance().getmBluetoothState().send(Utils.hexStringTobytes(cmd), this.sendCmdDelay, this.reCount, this.frameReCount, this.timeOut, this.frameTimeout, this.receiver, this.connectCallback);
            result = (ReceiveResult)this.commandQueue.take();
            return result;
        } catch (ErrorStateException var10) {
            var10.printStackTrace();
            return result;
        } catch (InterruptedException var11) {
            var11.printStackTrace();
            return result;
        } finally {
            ;
        }
    }

    public ReceiveResult sendIccCmd(String command) {
        String cmdBcc = "C6" + command;
        String cmd = "A7C6" + command + Utils.getBCC(Utils.hexStringTobytes(cmdBcc));
        ReceiveResult result = null;

        try {
            this.commandQueue = new LinkedBlockingQueue(2);
            BluetoothStateManager.instance().getmBluetoothState().send(UtilsBt.hexStringTobytes(cmd), this.sendCmdDelay, this.reCount, this.frameReCount, this.timeOut, this.frameTimeout, this.receiver, this.connectCallback);
            result = (ReceiveResult)this.commandQueue.take();
            return result;
        } catch (ErrorStateException var10) {
            var10.printStackTrace();
            return result;
        } catch (InterruptedException var11) {
            var11.printStackTrace();
            return result;
        } finally {
            ;
        }
    }

    public ReceiveResult sendEncryptICCCmd(String command) {
        String tempCmd = "C4" + Utils.intToByteStringTemp(command.length() / 2) + command;
        String cmd = "A7C4" + Utils.intToByteStringTemp(command.length() / 2) + command + Utils.getBCC(UtilsBt.hexStringTobytes(tempCmd));
        ReceiveResult result = null;

        try {
            this.commandQueue = new LinkedBlockingQueue(2);
            BluetoothStateManager.instance().getmBluetoothState().send(Utils.hexStringTobytes(cmd), this.sendCmdDelay, this.reCount, this.frameReCount, this.timeOut, this.frameTimeout, this.receiver, this.connectCallback);
            result = (ReceiveResult)this.commandQueue.take();
            return result;
        } catch (ErrorStateException var10) {
            var10.printStackTrace();
            return result;
        } catch (InterruptedException var11) {
            var11.printStackTrace();
            return result;
        } finally {
            ;
        }
    }

    public ReceiveResult TransCommand(String cmd, String reqData) {
        ReceiveResult result = null;
        if (cmd.equalsIgnoreCase("1")) {
            String cmdBcc = "C7" + reqData;
            String cmdTemp = "A7C7" + reqData + Utils.getBCC(Utils.hexStringTobytes(cmdBcc));

            try {
                this.commandQueue = new LinkedBlockingQueue(2);
                BluetoothStateManager.instance().getmBluetoothState().send(UtilsBt.hexStringTobytes(cmdTemp), this.sendCmdDelay, this.reCount, this.frameReCount, this.timeOut, this.frameTimeout, this.receiver, this.connectCallback);
                result = (ReceiveResult)this.commandQueue.take();
            } catch (ErrorStateException var7) {
                var7.printStackTrace();
            } catch (InterruptedException var8) {
                var8.printStackTrace();
            }
        } else if (cmd.equalsIgnoreCase("0")) {
            result = this.sendIccCmd(reqData);
        }

        return result;
    }

    public ReceiveResult MiwenCommand(String cmd, String reqData) {
        ReceiveResult result = null;
        if (cmd.equalsIgnoreCase("1")) {
            String cmdBcc = "C5" + Utils.intToByteStringTemp(reqData.length() / 2) + reqData;
            String cmdTemp = "A7C5" + Utils.intToByteStringTemp(reqData.length() / 2) + reqData + Utils.getBCC(UtilsBt.hexStringTobytes(cmdBcc));

            try {
                this.commandQueue = new LinkedBlockingQueue(2);
                BluetoothStateManager.instance().getmBluetoothState().send(UtilsBt.hexStringTobytes(cmdTemp), this.sendCmdDelay, this.reCount, this.frameReCount, this.timeOut, this.frameTimeout, this.receiver, this.connectCallback);
                result = (ReceiveResult)this.commandQueue.take();
            } catch (ErrorStateException var7) {
                var7.printStackTrace();
            } catch (InterruptedException var8) {
                var8.printStackTrace();
            }
        } else if (cmd.equalsIgnoreCase("0")) {
            result = this.sendEncryptICCCmd(reqData);
        }

        return result;
    }

    public boolean startScan(List<ScanResult> resultList, int outTime) throws ErrorStateException {
        try {
            this.scanResultList = resultList;
            this.latch = new CountDownLatch(1);
            BluetoothStateManager.instance().getmBluetoothState().startScan(outTime, this.scanCallback);
            this.latch.await();
            return true;
        } catch (InterruptedException var4) {
            var4.printStackTrace();
            return false;
        }
    }

    public void stopScan() throws ErrorStateException {
        if (BluetoothStateManager.instance().getmBluetoothState().getStateType() == 6) {
            BluetoothStateManager.instance().getmBluetoothState().stopScan();
        }

    }

    public int connectToObu(BluetoothDevice device, int timeOut) throws ErrorStateException {
        try {
            this.connectState = -1;
            this.latch = new CountDownLatch(1);
            BluetoothStateManager.instance().getmBluetoothState().connect(device, timeOut, this.connectCallback);
            this.latch.await();
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return this.connectState;
    }

    public synchronized boolean isObuConnected() {
        return this.mIsConnect;
    }

    public synchronized void disconnectObu() throws ErrorStateException {
        if (BluetoothStateManager.instance().getmBluetoothState().getStateType() != 4 && BluetoothStateManager.instance().getmBluetoothState().getStateType() != 0 && BluetoothStateManager.instance().getmBluetoothState().getStateType() != 1) {
            try {
                this.connectState = -1;
                this.latch = new CountDownLatch(1);
                BluetoothStateManager.instance().getmBluetoothState().disconnect(this.connectCallback);
                this.latch.await();
            } catch (InterruptedException var2) {
                var2.printStackTrace();
            }
        }

    }

    public void setSendCmdDelay(int sendCmdDelay) {
        this.sendCmdDelay = sendCmdDelay;
    }

    public void setNeedReply(boolean needReply) {
        BluetoothStateManager.instance().setNeedFrameReply(needReply);
    }

    public void setReCount(int reCount) {
        this.reCount = reCount;
    }

    public void setFrameReCount(int frameReCount) {
        this.frameReCount = frameReCount;
    }

    public void setmCallback(BluetoothObuCallback mCallback) {
        LogHelperBt.LogI("BluetoothHelper", "setCallback:" + mCallback.getClass().getSimpleName());
        this.mCallback = mCallback;
    }

    public boolean isBluetoothOpen() {
        boolean result = false;
        if (BluetoothStateManager.instance().getmBluetoothAdapter() != null && BluetoothStateManager.instance().getmBluetoothAdapter().isEnabled()) {
            result = true;
        }

        return result;
    }

    public void setmIsConnect(boolean mIsConnect) {
        this.mIsConnect = mIsConnect;
    }
}
