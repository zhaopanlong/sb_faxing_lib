package com.anshibo.faxing_lib;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;


import com.ctfo.bdqf.etc.obulib.QFHenan;
import com.ctfo.bdqf.etc.obulib.ServiceStatus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QFReader3 implements IReader {

    private static QFReader3 instance;
    private Context mContext;
    QFHenan obuMan;

    public static QFReader3 getInstance(Context context) {
        if (instance == null) {
            instance = new QFReader3(context);
        }
        return instance;
    }

    private QFReader3(Context context) {
        mContext = context;
        obuMan = new QFHenan(mContext, BuildConfig.DEBUG);
    }

    @Override
    public ReaderResult connect(ReaderDevice device) {
        ReaderResult readerResult = new ReaderResult();
        ServiceStatus serviceStatus = obuMan.connectDevice(device.getBluetoothDevice());
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
        ReaderResult readerResult = new ReaderResult();
        ServiceStatus deviceSN = obuMan.getDeviceSN();
        if (deviceSN.ServiceCode == 0) {
            readerResult.setSuccess(true);
            readerResult.setResult(deviceSN.ServiceInfo);
            return readerResult;
        }
        return readerResult;
    }

    @Override
    public ReaderResult mingWen(String cmds) {
        String len2 = HexBytes.desToHex(cmds.length() / 2, 2);
        String len3 = HexBytes.desToHex(("01" + len2 + cmds).length() / 2, 2);
        LogUtils.i("发送的命令::" + "80" + len3 + "01" + len2 + cmds);
        ServiceStatus serviceStatus = obuMan.transA0Command(1, "80" + len3 + "01" + len2 + cmds);

        ReaderResult readerResult = new ReaderResult();
        if (serviceStatus.ServiceCode == 0) {
            String info = serviceStatus.ServiceInfo;
            if (info.endsWith("9000")) {
                readerResult.setSuccess(true);
                readerResult.setResult(info.substring(8, info.length() - 4));
                return readerResult;
            }
        }

        return readerResult;
    }

    @Override
    public ReaderResult esamMingWen(String command) {
        LogUtils.e("command:::;" + command);
        String len2 = HexBytes.desToHex(command.length() / 2, 2);
        String len3 = HexBytes.desToHex(("01" + len2 + command).length() / 2, 2);
        LogUtils.i("发送的命令::" + "80" + len3 + "01" + len2 + command);
        ServiceStatus status = obuMan.transA0Command(2, "80" + len3 + "01" + len2 + command);
        ReaderResult readerResult = new ReaderResult();
        if (status.ServiceCode == 0) {
            String info = status.ServiceInfo.replace(" ", "");
            info = replaceBlank(info).trim();//去除空格和换行
            if (info.contains("9000")){
                String[] splitResult = info.split("9000");
                String ming = splitResult[0];
                readerResult.setSuccess(true);
                readerResult.setResult(ming.substring(8));
                return readerResult;
            }
        }

        return readerResult;
    }

    @Override
    public ReaderResult miWen(String cmds, boolean noNew) {
        ReaderResult readerResult = new ReaderResult();
        ServiceStatus serviceStatus = obuMan.transA1Command(1, cmds);
        if (serviceStatus.ServiceCode == 0){
          String serviceInfo =   serviceStatus.ServiceInfo;
          String mingwen = "";
          if (serviceInfo != null && serviceInfo.length()>12){
              mingwen = serviceInfo.substring(0,12);
          }
          if (mingwen != null && mingwen.contains("9000")){
              readerResult.setSuccess(true);
              readerResult.setResult(mingwen+"00"+serviceInfo.substring(12));
              return readerResult;
          }
        }
        return readerResult;
    }

    @Override
    public ReaderResult esamMiWen(String cmd) {
        LogUtils.e("command:::;" + cmd);
        String len2 = HexBytes.desToHex(cmd.length() / 2, 2);
        String len3 = HexBytes.desToHex(("01" + len2 + cmd).length() / 2, 2);
        LogUtils.i("发送的命令::" + "80" + len3 + "01" + len2 + cmd);
        ServiceStatus status = obuMan.transA0Command(2, "80" + len3 + "01" + len2 + cmd);
        ReaderResult readerResult = new ReaderResult();
        if (status.ServiceCode == 0) {
            String info = status.ServiceInfo.replace(" ", "");
            info = replaceBlank(info).trim();//去除空格和换行
            if (info.contains("9000")){
                String[] splitResult = info.split("9000");
                String ming = splitResult[0];
                readerResult.setSuccess(true);
                readerResult.setResult(ming.substring(8));
                return readerResult;
            }
        }

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
