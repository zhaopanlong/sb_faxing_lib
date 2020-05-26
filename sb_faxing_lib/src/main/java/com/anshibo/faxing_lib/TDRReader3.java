package com.anshibo.faxing_lib;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.tendyron.etc.core.TDRimpl;
import com.tendyron.etc.ietc.OBUManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhaopanlong
 * @createtime：2020/5/25 下午2:58
 */
public class TDRReader3 implements IReader {
    private static TDRReader3 instance;
    private Context mContext;
    public OBUManager obuMan;

    public static TDRReader3 getInstance(Context context) {
        if (instance == null) {
            instance = new TDRReader3(context);
        }
        return instance;
    }

    private TDRReader3(Context context) {
        mContext = context;
        TDRimpl.OBUManager(context);
    }

    @Override
    public ReaderResult connect(ReaderDevice device) {
        ReaderResult readerResult = new ReaderResult();
        OBUManager.ServiceStatus serviceStatus = obuMan.connectDevice(device.getBluetoothDevice());
        if (serviceStatus.ServiceCode == 0) {
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
        /*
        天地融都是新设备 se返回的都是全F
         */
        ReaderResult readerResult = new ReaderResult();
        readerResult.setSuccess(true);
        readerResult.setResult("FFFFFFFFFFFFFFFF");
        return readerResult;
    }

    @Override
    public ReaderResult mingWen(String cmd) {
        OBUManager.ServiceStatus serviceStatus = obuMan.TransCommand("0", cmd);
        ReaderResult readerResult = new ReaderResult();
        if (serviceStatus.ServiceCode == 0) {
            LogUtils.i("返回内容 :" + serviceStatus.ServiceInfo);
            String serviceInfo = serviceStatus.ServiceInfo.replaceAll(" ", "").trim();
            serviceInfo = replaceBlank(serviceInfo).trim();//去除空格和换行
            if (serviceInfo.endsWith("9000")) {
                readerResult.setSuccess(true);
                readerResult.setResult(serviceInfo.substring(0, serviceInfo.length() - 4));
                return readerResult;
            }
        }
        return readerResult;
    }

    @Override
    public ReaderResult esamMingWen(String cmd) {
        OBUManager.ServiceStatus serviceStatus = obuMan.TransCommand("1", cmd);
        ReaderResult readerResult = new ReaderResult();
        if (serviceStatus.ServiceCode == 0) {
            LogUtils.i("返回内容 :" + serviceStatus.ServiceInfo);
            String serviceInfo = serviceStatus.ServiceInfo.replaceAll(" ", "").trim();
            serviceInfo = replaceBlank(serviceInfo).trim();//去除空格和换行
            if (serviceInfo.endsWith("9000")) {
                readerResult.setSuccess(true);
                readerResult.setResult(serviceInfo.substring(0, serviceInfo.length() - 4));
                return readerResult;
            }
        }
        return readerResult;
    }

    @Override
    public ReaderResult miWen(String cmds, boolean noNew) {
        //天地融的设备 走不到密文指令的都是用的明文传输 为了保持和其他厂商设备的一致性
        //天地融的密文走和明文一样的代码
        OBUManager.ServiceStatus serviceStatus = obuMan.TransCommand("0", cmds);
        ReaderResult readerResult = new ReaderResult();
        if (serviceStatus.ServiceCode == 0) {
            LogUtils.i("返回内容 :" + serviceStatus.ServiceInfo);
            String serviceInfo = serviceStatus.ServiceInfo.replaceAll(" ", "").trim();
            serviceInfo = replaceBlank(serviceInfo).trim();//去除空格和换行
            if (serviceInfo.endsWith("9000")) {
                readerResult.setSuccess(true);
                readerResult.setResult(serviceInfo.substring(0, serviceInfo.length() - 4));
                return readerResult;
            }
        }
        return readerResult;
    }

    @Override
    public ReaderResult esamMiWen(String cmd) {
        //天地融的设备 走不到密文指令的都是用的明文传输 为了保持和其他厂商设备的一致性
        //天地融的密文走和明文一样的代码
        OBUManager.ServiceStatus serviceStatus = obuMan.TransCommand("1", cmd);
        ReaderResult readerResult = new ReaderResult();
        if (serviceStatus.ServiceCode == 0) {
            LogUtils.i("返回内容 :" + serviceStatus.ServiceInfo);
            String serviceInfo = serviceStatus.ServiceInfo.replaceAll(" ", "").trim();
            serviceInfo = replaceBlank(serviceInfo).trim();//去除空格和换行
            if (serviceInfo.endsWith("9000")) {
                readerResult.setSuccess(true);
                readerResult.setResult(serviceInfo.substring(0, serviceInfo.length() - 4));
                return readerResult;
            }
        }
        return readerResult;
    }

    public String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
