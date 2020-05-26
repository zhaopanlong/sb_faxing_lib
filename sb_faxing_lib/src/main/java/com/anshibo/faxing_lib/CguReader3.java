package com.anshibo.faxing_lib;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import com.cgutech.bluetoothstatusapi.exception.ErrorStateException;
import com.cgutech.newbluetoothapi.BluetoothObuCallback;
import com.cgutech.newbluetoothapi.BluetoothObuHandler;
import com.cgutech.newbluetoothapi.ReceiveResult;

/**
 * @author zhaopanlong
 * @createtime：2020/4/16 上午9:40
 */
public class CguReader3 implements IReader, BluetoothObuCallback {
    private static final String TAG = "成谷设备";
    private static CguReader3 cguReader3;
    protected BluetoothObuHandler bluetoothObuHandler;

    public static CguReader3 getInstance(Context context) {
        if (cguReader3 == null) {
            cguReader3 = new CguReader3(context);
        }
        return cguReader3;
    }

    private CguReader3(Context context) {
        bluetoothObuHandler = BluetoothObuHandler.getInstance();
        bluetoothObuHandler.initializeObu(context, this);
        bluetoothObuHandler.setReCount(5);
    }

    @Override
    public ReaderResult connect(ReaderDevice device) {
        ReaderResult readerResult = new ReaderResult();
        try {
            int result = bluetoothObuHandler.connectToObu(device.getBluetoothDevice(), 30000);
            if (result != -1) {
                readerResult.setSuccess(true);
                return readerResult;
            }
        } catch (ErrorStateException e) {
            e.printStackTrace();
        }
        readerResult.setSuccess(false);
        return readerResult;
    }

    @Override
    public void disConnect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bluetoothObuHandler.disconnectObu();
                } catch (ErrorStateException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public ReaderResult getSE() {
        ReaderResult readerResult = new ReaderResult();
        try {
            //成谷获取SE防休眠指令
            bluetoothObuHandler.sendObuCmd("d1", "30");
            //获取se的指令 55000A010000000000000000005E
            ReceiveResult serStatus = bluetoothObuHandler.sendObuCmd("a7", "55000A010000000000000000005E", 3, 10000);
            if (!TextUtils.isEmpty(serStatus.getCommand()) && serStatus.getCommand().substring(0, serStatus.getCommand().length() - 2).endsWith("9000")) {//返回成功
                readerResult.setResult(serStatus.getCommand().substring(8, serStatus.getCommand().length() - 6));
                readerResult.setSuccess(true);
                return readerResult;
            }
        } catch (ErrorStateException e) {
            e.printStackTrace();
        }
        readerResult.setSuccess(false);
        return readerResult;
    }

    @Override
    public ReaderResult mingWen(String cmd) {
        ReaderResult readerResult = new ReaderResult();
        //先发防休眠
        String len2 = HexBytes.desToHex(cmd.length() / 2, 2);
        ReceiveResult receiveResult = null;
        try {
            receiveResult = bluetoothObuHandler.sendObuCmd("a3", "01000" + len2 + cmd);
            //receiveResult = bluetoothObuHandler.sendIccCmd(cmd);
            LogUtils.i(TAG + ":明文指令：" + cmd + ":返回结果：channel：" + receiveResult.getChannel() + ":command:" + receiveResult.getCommand());
            String command = receiveResult.getCommand();
            if (command.contains("9000")) {
                readerResult.setSuccess(true);
                readerResult.setResult(command.substring(6));
                return readerResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        readerResult.setSuccess(false);
        return readerResult;
    }

    @Override
    public ReaderResult esamMingWen(String cmd) {
        ReaderResult readerResult = new ReaderResult();
        ReceiveResult receiveResult = bluetoothObuHandler.sendEsamCmd(cmd);
        if (receiveResult != null) {
            String command = receiveResult.getCommand();
            if (!TextUtils.isEmpty(command) && command.endsWith("9000")) {
                readerResult.setSuccess(true);
                readerResult.setResult(command.substring(6));
                return readerResult;
            }
        }
        readerResult.setSuccess(false);
        return readerResult;
    }

    @Override
    public ReaderResult miWen(String cmds, boolean noNew) {
        ReaderResult readerResult = new ReaderResult();
        try {
            Thread.sleep(500);
            String cmd2 = getCmd2(cmds);
            String len = HexBytes.desToHex(cmd2.length() / 2, 4);
            String comdsNew = getComds("c4" + len + cmd2);//按位异或数据
            ReceiveResult receiveResult = bluetoothObuHandler.sendObuCmd("a7", "c4" + len + cmd2 + comdsNew);
            if (receiveResult != null) {
                String command = receiveResult.getCommand();
                if (!TextUtils.isEmpty(command) && command.contains("9000")) {
                    readerResult.setSuccess(true);
                    readerResult.setResult(command.substring(6));
                    return readerResult;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        readerResult.setSuccess(false);
        return readerResult;
    }

    @Override
    public ReaderResult esamMiWen(String cmd) {
        return null;
    }

    @Override
    public void onConnect() {

    }

    @Override
    public void onConnectTimeout() {

    }

    @Override
    public void onConnectError(String s) {

    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public void onReceiveObuCmd(String s, String s1) {

    }

    @Override
    public void onScanSuccess(BluetoothDevice bluetoothDevice, int i) {

    }

    @Override
    public void onScanTimeout() {

    }

    @Override
    public void onSendTimeout(String s, String s1) {

    }

    @Override
    public void onSendError(String s) {

    }

    private String getCmd2(String cmds) {
        LogUtils.e("密文写卡");
        String[] split = cmds.split(":");
        String cmds2 = "";
        for (String s : split) {
            LogUtils.e("获得的加密数据::" + s);
            byte[] bytes = Base64.decode(s.getBytes(), Base64.DEFAULT);
            String jieMa = HexBytes.bytes2Hex(bytes, bytes.length);
            String lv = HexBytes.desToHex(jieMa.length() / 2, 2);
            LogUtils.e("揭秘数据::" + jieMa);
            LogUtils.e("解密数据的长度16进制" + lv);
            cmds2 += (lv + jieMa);
        }
        return cmds2;
    }

    /**
     * 获取按位异或的数据
     *
     * @param comds
     */
    private String getComds(String comds) {
        String[] dateArr = new String[comds.length() / 2];
        try {
            for (int i = 0; i < comds.length() / 2; i++) {
                dateArr[i] = comds.substring(i * 2, i * 2 + 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String code = "";
        for (int i = 0; i < dateArr.length - 1; i++) {
            if (i == 0) {
                code = xor(dateArr[i], dateArr[i + 1]);
            } else {
                code = xor(code, dateArr[i + 1]);
            }
        }
        return code;
    }

    private static String xor(String strHex_X, String strHex_Y) {

//        MyLogUtil.logMsg("参与异或运算的liange两个参数::"+strHex_X+":::"+strHex_Y);    //将x、y转成二进制形式
        String anotherBinary = Integer.toBinaryString(Integer.valueOf(strHex_X, 16));
        String thisBinary = Integer.toBinaryString(Integer.valueOf(strHex_Y, 16));
        String result = "";
        //判断是否为8位二进制，否则左补零
        if (anotherBinary.length() != 8) {
            for (int i = anotherBinary.length(); i < 8; i++) {
                anotherBinary = "0" + anotherBinary;
            }
        }
        if (thisBinary.length() != 8) {
            for (int i = thisBinary.length(); i < 8; i++) {
                thisBinary = "0" + thisBinary;
            }
        }
        //异或运算
        for (int i = 0; i < anotherBinary.length(); i++) {
            //如果相同位置数相同，则补0，否则补1
            if (thisBinary.charAt(i) == anotherBinary.charAt(i)) {
                result += "0";
            } else {
                result += "1";
            }
        }
//        Log.e("code", result+"::16::进制数::"+Integer.toHexString(Integer.parseInt(result, 2)));
        return HexBytes.desToHex(Integer.parseInt(result, 2), 2);
    }
}