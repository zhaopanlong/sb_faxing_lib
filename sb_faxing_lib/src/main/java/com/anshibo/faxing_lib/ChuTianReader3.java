package com.anshibo.faxing_lib;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.bluetoothobu.obusdk.OBUManager;
import com.bluetoothobu.obusdk.ServiceStatus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhaopanlong
 * @createtime：2020/5/8 下午8:30
 */
public class ChuTianReader3 implements IReader {
    private static ChuTianReader3 instance;
    private Context mContext;
    public OBUManager obuMan;

    public static ChuTianReader3 getInstance(Context context) {
        if (instance == null) {
            instance = new ChuTianReader3(context);
        }
        return instance;
    }

    private ChuTianReader3(Context context) {
        mContext = context;
        obuMan = new OBUManager(context);
    }

    @Override
    public ReaderResult connect(ReaderDevice device) {
        ReaderResult readerResult = new ReaderResult();
        ServiceStatus serviceStatus = obuMan.connectDevice(device.getBluetoothDevice());
        if (serviceStatus.getServiceCode() == 0) {
            readerResult.setSuccess(true);
            return readerResult;
        }
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
        if (deivceSE.getServiceCode() == 0){
            readerResult.setSuccess(true);
            readerResult.setResult("FFFFFFFFFFFFFFFF");
            return readerResult;
        }
        return readerResult;
    }

    @Override
    public ReaderResult mingWen(String cmd) {
        ServiceStatus serStatus = obuMan.transCommand("0", cmd);
        Log.i("读卡返回", "code " + serStatus.getServiceCode());
        ReaderResult readerResult = new ReaderResult();
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
            if (ming.endsWith("9000")) {
                readerResult.setSuccess(true);
                readerResult.setResult(ming.substring(0, ming.length() - 4));
                return readerResult;
            }
        }

        return readerResult;
    }

    @Override
    public ReaderResult esamMingWen(String cmd) {
        ServiceStatus serStatus = obuMan.transCommand("1", cmd);
        Log.i("读卡返回", "code " + serStatus.getServiceCode());
        ReaderResult readerResult = new ReaderResult();
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
            if (ming.endsWith("9000")) {
                readerResult.setSuccess(true);
                readerResult.setResult(ming.substring(0, ming.length() - 4));
                return readerResult;
            }
        }

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
