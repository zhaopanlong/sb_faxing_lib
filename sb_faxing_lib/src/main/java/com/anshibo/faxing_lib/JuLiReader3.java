package com.anshibo.faxing_lib;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import etc.obu.service.OBUManager;
import etc.obu.service.ServiceStatus;

public class JuLiReader3 implements IReader {
    private OBUManager obuMan;
    private static JuLiReader3 instance;
    private Context mContext;

    public static JuLiReader3 getInstance(Context context) {
        if (instance == null) {
            instance = new JuLiReader3(context);
        }
        return instance;
    }

    private JuLiReader3(Context context) {
        mContext = context;
        obuMan = new OBUManager(context);
        LogUtils.i("obuMan::" + obuMan);
    }

    @Override
    public ReaderResult connect(ReaderDevice readerDevice) {
        BluetoothDevice dev = readerDevice.getBluetoothDevice();
        ReaderResult readerResult = new ReaderResult();
        ServiceStatus result = null;
        // 连接
        obuMan.setTimeOutSecond(30000);
        result = obuMan.connectDevice(dev.getName(), dev.getAddress());
        if (result.getServiceCode() == 0) {
            readerResult.setSuccess(true);
            readerResult.setResult(dev.getName());
            return readerResult;
        }
        readerResult.setSuccess(false);
        readerResult.setError(result.getServiceInfo());
        return readerResult;
    }

    @Override
    public void disConnect() {
        ServiceStatus result = obuMan.disconnectDevice();
        if (result.getServiceCode() != 0) {
            LogUtils.e("聚利：：断开失败" + result.getServiceInfo());
            return;
        }
        LogUtils.i("聚利：：断开成功" + result.getServiceInfo());
    }

    @Override
    public ReaderResult getSE() {
        ReaderResult readerResult = new ReaderResult();
        ServiceStatus serStatus = null;
        serStatus = obuMan.getDeviceSE();
        Log.e("获取设备序列号返回码", "code " + serStatus.getServiceCode());
        if (serStatus.getServiceCode() == 0) {
            readerResult.setSuccess(true);
            readerResult.setResult(serStatus.getServiceInfo());
            return readerResult;
        }

        readerResult.setSuccess(false);
        readerResult.setError(serStatus.getServiceInfo());
        return readerResult;

    }

    @Override
    public ReaderResult mingWen(String cmd) {
        ReaderResult readerResult = new ReaderResult();
        ServiceStatus serStatus = null;
        String len2 = HexBytes.desToHex(cmd.length() / 2, 2);
        serStatus = obuMan.TransMingwenCommand("A3", "01" + len2 + cmd, cmd.length() / 2);
        if (serStatus.getServiceCode() == 0) {
            LogUtils.i("返回内容 :" + serStatus.getServiceInfo());
            String serviceInfo = serStatus.getServiceInfo().replaceAll(" ", "").trim();
            serviceInfo = replaceBlank(serviceInfo).trim();//去除空格和换行
            int start = serviceInfo.indexOf("明文");
            int end = serviceInfo.indexOf("签名内容");
            String ming = serviceInfo.substring(start + 2, end);
            String qianming = serviceInfo.substring(end + 4);
            if (ming.endsWith("9000")) {
                LogUtils.i("ming::" + ming);
                readerResult.setSuccess(true);
                readerResult.setResult(ming + "00" + qianming);
                return readerResult;
            }

        }
        readerResult.setSuccess(false);
        readerResult.setError(serStatus.getServiceInfo());
        return readerResult;
    }

    @Override
    public ReaderResult esamMingWen(String cmd) {
        ReaderResult readerResult = new ReaderResult();
        ServiceStatus serStatus = null;
        String len2 = HexBytes.desToHex(cmd.length() / 2, 2);
        serStatus = obuMan.transESAMMingwenCommand("01" + len2 + cmd);
        if (serStatus.getServiceCode() == 0) {
            LogUtils.i("返回 " + serStatus.getServiceInfo());
            String serviceInfo = serStatus.getServiceInfo().replaceAll(" ", "").trim();
            serviceInfo = replaceBlank(serviceInfo).trim();//去除空格和换行
            int start = serviceInfo.indexOf("明文");
            int end = serviceInfo.indexOf("签名内容");
            String ming = serviceInfo.substring(start + 2, end);
            if (ming.trim().endsWith("9000")) {
                readerResult.setSuccess(true);
                readerResult.setResult(ming.trim().substring(0, ming.trim().length() - 4));
                return readerResult;
            }
            readerResult.setSuccess(false);
            readerResult.setError(ming);
            return readerResult;
        }
        readerResult.setSuccess(false);
        readerResult.setError("请重新尝试");
        return readerResult;
    }

    @Override
    public ReaderResult miWen(String cmds, boolean noNew) {
        ReaderResult readerResult = new ReaderResult();
        ServiceStatus serStatus = null;
        String cmd2 = getCmd2(cmds);
        serStatus = obuMan.transMiwenCommand(cmd2.length() / 2, "03" + cmd2);
        if (serStatus.getServiceCode() == 0) {
            LogUtils.i("返回内容 :" + serStatus.getServiceInfo());
            String serviceInfo = serStatus.getServiceInfo().replaceAll(" ", "").trim();
            serviceInfo = replaceBlank(serviceInfo).trim();//去除空格和换行
            int start = serviceInfo.indexOf("明文");
            int end = serviceInfo.indexOf("签名内容");
            String ming = serviceInfo.substring(start + 2, end);
            if (ming.trim().endsWith("9000")) {
                readerResult.setSuccess(true);
                readerResult.setResult(ming);
                return readerResult;
            }
        }
        readerResult.setSuccess(false);
        readerResult.setError(serStatus.getServiceInfo());
        return readerResult;
    }

    @Override
    public ReaderResult esamMiWen(String cmd) {
        ReaderResult readerResult = new ReaderResult();
        String cmd2 = getCmd2(cmd);
        ServiceStatus serStatus = null;
        serStatus = obuMan.transESAMMiwenCommand("03" + cmd2);
        if (serStatus.getServiceCode() == 0) {
            LogUtils.i("写标签返回 " + serStatus.getServiceInfo());
            String serviceInfo = serStatus.getServiceInfo().replaceAll(" ", "").trim();
            serviceInfo = replaceBlank(serviceInfo).trim();//去除空格和换行
            int start = serviceInfo.indexOf("明文");
            int end = serviceInfo.indexOf("签名内容");
            String ming = serviceInfo.substring(start + 2, end);
            if (ming.trim().endsWith("9000")) {
                readerResult.setSuccess(true);
                readerResult.setResult(ming);
                return readerResult;
            }
        }
        readerResult.setSuccess(false);
        readerResult.setError(serStatus.getServiceInfo());
        return readerResult;

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

    private String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
