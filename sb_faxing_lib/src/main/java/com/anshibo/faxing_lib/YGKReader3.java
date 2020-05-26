package com.anshibo.faxing_lib;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.text.TextUtils;

import com.ygk.obu.OBUManager;
import com.ygk.obu.ServiceStatus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhaopanlong
 * @createtime：2020/4/23 下午4:56
 */
public class YGKReader3 implements IReader {
    private static YGKReader3 instance;
    public OBUManager obuMan;

    public static YGKReader3 getInstance(Context context) {
        if (instance == null) {
            instance = new YGKReader3(context);
        }
        return instance;
    }

    private YGKReader3(Context context) {
        obuMan = OBUManager.getInstance(context);
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
            String serviceInfo = deivceSE.getServiceInfo();
            if (TextUtils.isEmpty(serviceInfo)){
                serviceInfo = "FFFFFFFFFFFFFFFF";
            }
            readerResult.setSuccess(true);
            readerResult.setResult(serviceInfo);
            return readerResult;
        }
        readerResult.setSuccess(false);
        return readerResult;
    }

    @Override
    public ReaderResult mingWen(String cmd) {
        ReaderResult readerResult = new ReaderResult();
        ServiceStatus serStatus = obuMan.transCommand("0",cmd);
        if (serStatus.getServiceCode() == 0) {
            LogUtils.i("返回内容 :" + serStatus.getServiceInfo());
            String serviceInfo = serStatus.getServiceInfo().replaceAll(" ", "").trim();
            serviceInfo = replaceBlank(serviceInfo).trim();//去除空格和换行
            int start = serviceInfo.indexOf("明文");
            int end = serviceInfo.indexOf("签名内容");
            String ming = serviceInfo;
            String qianming = "";
            if (start != -1 && end != -1) {
                ming = serviceInfo.substring(start + 2, end);
                qianming = serviceInfo.substring(end + 4);
            }

            if (ming.trim().endsWith("9000")) {
                readerResult.setSuccess(true);
                readerResult.setResult(ming);
                return readerResult;
            }
        }
        readerResult.setSuccess(false);
        return readerResult;
    }

    @Override
    public ReaderResult esamMingWen(String cmd) {
        ReaderResult readerResult = new ReaderResult();
        ServiceStatus serStatus = obuMan.transCommand("1", cmd);
        if (serStatus.getServiceCode() == 0) {
            LogUtils.i("返回内容 :" + serStatus.getServiceInfo());
            String serviceInfo = serStatus.getServiceInfo().replaceAll(" ", "").trim();
            serviceInfo = replaceBlank(serviceInfo).trim();//去除空格和换行
            int start = serviceInfo.indexOf("明文");
            int end = serviceInfo.indexOf("签名内容");
            String ming = serviceInfo;
            String qianming = "";
            if (start != -1 && end != -1) {
                ming = serviceInfo.substring(start + 2, end);
                qianming = serviceInfo.substring(end + 4);
            }

            if (ming.trim().endsWith("9000")) {
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
        return null;
    }

    @Override
    public ReaderResult esamMiWen(String cmd) {
        return null;
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
