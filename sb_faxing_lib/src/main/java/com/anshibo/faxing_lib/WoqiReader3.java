package com.anshibo.faxing_lib;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Base64;

import com.watchdata.obusdkhenan.inf.SdkInterface;
import com.watchdata.obusdkhenan.inf.bean.ServiceStatus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhaopanlong
 * @createtime：2020/4/23 下午4:29
 */
public class WoqiReader3 implements IReader {
    private static final String TAG = "握奇设备";
    private static WoqiReader3 instance;
    private SdkInterface obuMan;

    public static WoqiReader3 getInstance(Context context) {
        if (instance == null) {
            instance = new WoqiReader3(context);
        }
        return instance;
    }

    public WoqiReader3(Context context) {
        obuMan = new SdkInterface(context);
        obuMan.setTimeOutSecond("5000");//超时时间
    }

    @Override
    public ReaderResult connect(ReaderDevice device) {
        ReaderResult readerResult = new ReaderResult();
        ServiceStatus serviceStatus = obuMan.connectDevice(device.getBluetoothDevice());
        if (serviceStatus.getServiceCode() == 0) {
            readerResult.setSuccess(true);
            return readerResult;
        }
        readerResult.setSuccess(false);
        return readerResult;
    }

    @Override
    public void disConnect() {
        obuMan.disconnectDevice();
    }

    @Override
    public ReaderResult getSE() {
        ReaderResult readerResult = new ReaderResult();
        ServiceStatus deivceSE = obuMan.getDeivceSE();
        if (deivceSE.getServiceCode() == 0) {
            readerResult.setSuccess(true);
            readerResult.setResult(deivceSE.getServiceInfo());
            return readerResult;
        }
        readerResult.setSuccess(false);
        return readerResult;
    }

    @Override
    public ReaderResult mingWen(String cmd) {
        ReaderResult readerResult = new ReaderResult();
        ServiceStatus serviceStatus;
        if (cmd.startsWith("805000020B01")) {
            //圈存初始化命令
            serviceStatus = obuMan.transA1Command(0, cmd);
            if (serviceStatus.getServiceCode() == 0){
                serviceStatus.setServiceInfo("********"+serviceStatus.getServiceInfo());
            }
        } else {
            String len2 = HexBytes.desToHex(cmd.length() / 2, 2);
            String len3 = HexBytes.desToHex(("01" + len2 + cmd).length() / 2, 2);
            LogUtils.i("发送的命令::" + "80" + len3 + "01" + len2 + cmd);
            serviceStatus = obuMan.transA0Command(1, "80" + len3 + "01" + len2 + cmd);
        }


        if (serviceStatus.getServiceCode() == 0) {
            String serviceInfo = serviceStatus.getServiceInfo().trim();
            LogUtils.i(TAG + ":明文指令返回结果：" + serviceInfo);
            if (serviceInfo != null && serviceInfo.contains("9000")) {
                readerResult.setSuccess(true);
                serviceInfo = serviceInfo.substring(8);
                String[] split = serviceInfo.split("9000");
                String result = split[0] + "9000";
                if (split.length > 1) {
                    result += ("00" + split[1]);
                }
                readerResult.setResult(result);
                return readerResult;
            }
        }
        readerResult.setSuccess(false);
        return readerResult;
    }

    @Override
    public ReaderResult esamMingWen(String cmd) {
        ReaderResult readerResult = new ReaderResult();
        ServiceStatus status = obuMan.transA2Command(0, cmd);
        LogUtils.e("ServiceCode:::;" + status.getServiceCode());
        if (status.getServiceCode() == 0) {
            LogUtils.i("返回内容 :" + status.getServiceInfo());
            String serviceInfo = status.getServiceInfo().replaceAll(" ", "").trim();
            if (serviceInfo.contains("9000")) {
                serviceInfo = replaceBlank(serviceInfo).trim();//去除空格和换行
                String[] splitResult = serviceInfo.split("9000");
                String ming = "";
                if (splitResult.length > 0) {
                    ming = splitResult[0];
                }
                readerResult.setSuccess(true);
                readerResult.setResult(ming);
                return readerResult;
            }
        }
        readerResult.setSuccess(false);
        return readerResult;
    }

    @Override
    public ReaderResult miWen(String cmds, boolean noNew) {
        ReaderResult readerResult = new ReaderResult();
        String cmd2 = getCmd2(cmds);
        ServiceStatus serviceStatus = obuMan.transA1Command(1, cmd2);
        if (0 == serviceStatus.getServiceCode()) {//写卡成功
            String serviceInfo = serviceStatus.getServiceInfo();
            if (serviceInfo != null && serviceInfo.substring(0, 12).contains("9000")) {
                String ming = serviceInfo.substring(0, 12);
                readerResult.setSuccess(true);
                readerResult.setResult(ming + "00" + serviceInfo.substring(12));
                return readerResult;
            }
        }
        readerResult.setSuccess(false);
        return readerResult;
    }

    @Override
    public ReaderResult esamMiWen(String cmd) {
        ReaderResult readerResult = new ReaderResult();
        String cmds2 = getCmd2(cmd);
        ServiceStatus status = obuMan.transA2Command(1, cmds2);
        if (status.getServiceCode() == 0) {
            LogUtils.i("返回内容 :" + status.getServiceInfo());
            String serviceInfo = status.getServiceInfo().replaceAll(" ", "").trim();
            if (serviceInfo.contains("9000")) {
                serviceInfo = replaceBlank(serviceInfo).trim();//去除空格和换行
                String[] splitResult = serviceInfo.split("9000");
                String ming = splitResult[0];
                LogUtils.i("ming::" + ming);
                readerResult.setSuccess(true);
                readerResult.setResult(ming);
                return readerResult;
            }
        }
        readerResult.setSuccess(false);
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
}
