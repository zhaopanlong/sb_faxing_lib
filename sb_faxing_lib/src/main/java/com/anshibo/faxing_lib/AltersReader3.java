package com.anshibo.faxing_lib;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.artc.development.artcblehenansdk.ArtcObuManager;
import com.artc.development.artcblehenansdk.ServiceStatus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AltersReader3 implements IReader {
    private static AltersReader3 instance;
    private Context mContext;
    ArtcObuManager obuManager;

    public static AltersReader3 getInstance(Context context) {
        if (instance == null) {
            instance = new AltersReader3(context);
        }
        return instance;
    }

    private AltersReader3(Context context) {
        mContext = context;
        obuManager = ArtcObuManager.OBUManager();
    }

    @Override
    public ReaderResult connect(ReaderDevice device) {
        ReaderResult readerResult = new ReaderResult();
        ServiceStatus serviceStatus = obuManager.connectDevice(mContext, device.getBluetoothDevice());
        if (serviceStatus.serviceCode == 0) {
            readerResult.setSuccess(true);
            return readerResult;
        }
        return readerResult;
    }

    @Override
    public void disConnect() {
        obuManager.disconnectDevice();
    }

    @Override
    public ReaderResult getSE() {
        ReaderResult readerResult = new ReaderResult();
        ServiceStatus deviceSE = obuManager.getDeviceSE();
        if (deviceSE.serviceCode == 0) {
            readerResult.setSuccess(true);
            readerResult.setResult(deviceSE.serviceInfo);
            return readerResult;
        }
        return readerResult;
    }

    @Override
    public ReaderResult mingWen(String cmd) {
        ReaderResult readerResult = new ReaderResult();
        ServiceStatus serviceStatus = obuManager.transCommand("0", cmd);
        if (serviceStatus.serviceCode == 0) {
            //成功了
            String serviceInfo = serviceStatus.serviceInfo.replaceAll(" ", "").trim();
            serviceInfo = replaceBlank(serviceInfo).trim();//去除空格和换行
            String[] spilt = serviceInfo.split("&");
            String ming = spilt[0];
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
        ReaderResult readerResult = new ReaderResult();
        ServiceStatus serviceStatus = obuManager.transCommand("1", cmd);
        if (serviceStatus.serviceCode == 0) {
            //成功了
            String serviceInfo = serviceStatus.serviceInfo.replaceAll(" ", "").trim();
            serviceInfo = replaceBlank(serviceInfo).trim();//去除空格和换行
            String[] spilt = serviceInfo.split("&");
            String ming = spilt[0];
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
        ReaderResult readerResult = new ReaderResult();
        ServiceStatus serviceStatus = obuManager.miwenCommand("0", cmds);
        if (serviceStatus.serviceCode == 0) {
            //成功了
            String serviceInfo = serviceStatus.serviceInfo.replaceAll(" ", "").trim();
            serviceInfo = replaceBlank(serviceInfo).trim();//去除空格和换行
            String[] spilt = serviceInfo.split("&");
            String ming = spilt[0];
            if (ming.endsWith("9000")) {
                String result = ming;
                if (spilt.length > 1) {
                    result += ("00"+spilt[1]);
                }
                readerResult.setSuccess(true);
                readerResult.setResult(result);
                return readerResult;
            }
        }
        readerResult.setSuccess(false);
        readerResult.setError(serviceStatus.serviceInfo);
        return readerResult;
    }

    @Override
    public ReaderResult esamMiWen(String cmd) {
        ReaderResult readerResult = new ReaderResult();
        ServiceStatus serviceStatus = obuManager.miwenCommand("1", cmd);
        if (serviceStatus.serviceCode == 0) {
            //成功了
            String serviceInfo = serviceStatus.serviceInfo.replaceAll(" ", "").trim();
            serviceInfo = replaceBlank(serviceInfo).trim();//去除空格和换行
            String[] spilt = serviceInfo.split("&");
            String ming = spilt[0];
            if (ming.endsWith("9000")) {
                readerResult.setSuccess(true);
                readerResult.setResult(ming);
                return readerResult;
            }
        }
        readerResult.setSuccess(false);
        readerResult.setError(serviceStatus.serviceInfo);
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
}
