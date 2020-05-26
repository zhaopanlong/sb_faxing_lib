package com.anshibo.faxing_lib;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.genvict.obusdk.data.ServiceStatus;
import com.genvict.obusdk.user.interfaces.OBUManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JinYiReader3 implements IReader{
    private static JinYiReader3 reader3;
    private OBUManager obuMan;

    public static JinYiReader3 getInstance(Context context) {
        if (reader3 == null) {
            reader3 = new JinYiReader3(context);
        }
        return reader3;
    }

    private JinYiReader3(Context context) {
        obuMan = OBUManager.getInstance();
        obuMan.initialize(context);
    }

    @Override
    public ReaderResult connect(ReaderDevice device) {
        ReaderResult readerResult = new ReaderResult();
        ServiceStatus serviceStatus = obuMan.connectDevice(device.getBluetoothDevice().getAddress());
        if (serviceStatus.getStatus() == 0) {
            readerResult.setSuccess(true);
            return readerResult;
        }
        readerResult.setSuccess(false);
        readerResult.setError("连接失败");
        return readerResult;
    }

    @Override
    public void disConnect() {
        obuMan.disconnectDevice();
    }

    @Override
    public ReaderResult getSE() {
        ReaderResult readerResult = new ReaderResult();
        ServiceStatus deviceSE = obuMan.getDeviceSE();
        Log.e("获取设备序列号返回码", "code " + deviceSE.getStatus());
        if (deviceSE.getStatus() == 0) {
            String SE = (String) deviceSE.getData();
            LogUtils.i("获取设备序列号返回内容 " + SE);
            readerResult.setSuccess(true);
            readerResult.setResult(SE);
            return readerResult;
        }
        readerResult.setSuccess(false);
        readerResult.setError("获取SE失败");
        return readerResult;
    }

    @Override
    public ReaderResult mingWen(String cmd) {
        LogUtils.i("明文卡指令：" + cmd);
        ReaderResult result = new ReaderResult();
        ServiceStatus serStatus = obuMan.cardCommand(cmd);
        Log.i("读卡返回", "status " + serStatus.getStatus() + "data::");
        if (serStatus.getStatus() == 0) {
            String serviceInfo = ((String) serStatus.getData()).replaceAll(" ", "").trim();
            LogUtils.i("返回内容 :" + serStatus.getData());
            serviceInfo = replaceBlank(serviceInfo).trim();//去除空格和换行
            String[] split = serviceInfo.split("&");
            String ming = split[0];
            LogUtils.i("ming::" + ming);
            if (ming.endsWith("9000")) {
                result.setSuccess(true);
                if (split.length>1){
                    result.setResult(ming+"00"+split[1]);
                }else {
                    result.setResult(ming);
                }

                return result;
            }
            result.setSuccess(false);
            result.setError((String) serStatus.getData());
            return result;
        }
        result.setSuccess(false);
        result.setError(serStatus.getMessage());
        return result;
    }

    @Override
    public ReaderResult esamMingWen(String cmd) {
        ReaderResult readerResult = new ReaderResult();
        LogUtils.i("明文标签指令：" + cmd);
        ServiceStatus serStatus = null;
        byte b = 0x2;
        serStatus = obuMan.cosCommand(b, cmd);
        if (serStatus.getStatus() == 0) {
            //进入3f00文件成功
            LogUtils.e("有关标签指令指令返回信息：：" + serStatus.getData());
            String result = (String) serStatus.getData();
            if (result.endsWith("9000")) {
                readerResult.setSuccess(true);
                readerResult.setResult(result.substring(0, result.length() - 4));
                return readerResult;
            }
            readerResult.setSuccess(false);
            readerResult.setError(result);
            return readerResult;
        }
        readerResult.setSuccess(false);
        readerResult.setError(serStatus.getMessage());
        return readerResult;

    }

    @Override
    public ReaderResult miWen(String cmds, boolean noNew) {
        ReaderResult readerResult = new ReaderResult();
        LogUtils.i("密文写卡指令：" + cmds);
        String cmds2 = getCmd2(cmds);
        ServiceStatus serStatus = null;
        LogUtils.i(":::reqData::" + cmds2);
        if (noNew) {
            serStatus = obuMan.recentTransA1(cmds2);
        } else {
            serStatus = obuMan.newTransA1(cmds2);
        }
        Log.i("新的newtransA1Thread返回", "code " + serStatus.getStatus());
        if (serStatus.getStatus() == 0) {
            LogUtils.i("返回内容 :" + serStatus.getData());
            String serviceInfo = ((String) serStatus.getData()).replaceAll(" ", "").trim();
            String[] split = serviceInfo.split("&");
            String ming = split[0];
            LogUtils.i("ming::" + ming);
            if (ming.endsWith("9000")) {
                String qianming = split[1];
                LogUtils.i("ming::" + ming + ":::qianming::" + qianming + "----签名的长度：：：" + qianming.length());
                readerResult.setSuccess(true);
                readerResult.setResult(ming + "00" + qianming);
                return readerResult;
            }
        }
        readerResult.setSuccess(false);
        readerResult.setError(serStatus.getMessage());
        return readerResult;
    }

    @Override
    public ReaderResult esamMiWen(String cmd) {
        LogUtils.i("密文标签指令：" + cmd);
        ReaderResult readerResult = new ReaderResult();
        String cmd2 = getCmd2(cmd);
        ServiceStatus serStatus = null;
        serStatus = obuMan.esamActive(cmd2);
        if (serStatus.getStatus() == 0) {
            LogUtils.i("修改拆卸状态返回 " + serStatus.getStatus());
            String data = (String) serStatus.getData();
            LogUtils.i("密文通道返回：：： " + (String) serStatus.getData());
            if (data.contains("9000")) {
                readerResult.setSuccess(true);
                readerResult.setResult(serStatus.getMessage());
                return readerResult;
            }
        }
        readerResult.setSuccess(false);
        readerResult.setError(serStatus.getMessage());
        return readerResult;
    }


    private String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    private String getCmd2(String cmd1) {
        //密文写卡
        String[] split = cmd1.split(":");
        String cmds2 = "";
        String no = "";
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            LogUtils.i("获得的加密数据::" + s);
            byte[] bytes = Base64.decode(s.getBytes(), Base64.DEFAULT);
            String jieMa = HexBytes.bytes2Hex(bytes, bytes.length);
            String lv = HexBytes.desToHex(jieMa.length() / 2, 2);
            LogUtils.i("揭秘数据::" + jieMa);
            LogUtils.i("解密数据的长度16进制" + lv);
            no = HexBytes.intToHex2(i + 1);
            cmds2 += (no + lv + jieMa);
        }

        LogUtils.e("cmds2：：：" + cmds2);
        return cmds2;
    }
}
