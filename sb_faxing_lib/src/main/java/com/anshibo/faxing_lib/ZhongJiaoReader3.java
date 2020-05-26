package com.anshibo.faxing_lib;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.supernode.bleopt.OBUManager;
import com.supernode.bleopt.obuoption;

/**
 * @author zhaopanlong
 * @createtime：2020/5/25 下午5:00
 */
public class ZhongJiaoReader3 implements IReader {
    private static ZhongJiaoReader3 instnce;
    private Context mContext;
    public obuoption obuMan;

    public static ZhongJiaoReader3 getInstance(Context context) {
        if (instnce == null) {
            instnce = new ZhongJiaoReader3(context);
        }
        return instnce;
    }

    private ZhongJiaoReader3(Context context) {
        mContext = context;
        obuMan = new obuoption(context);
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
        ReaderResult readerResult = new ReaderResult();
        readerResult.setSuccess(true);
        readerResult.setResult("FFFFFFFFFFFFFFFF");
        return readerResult;
    }

    @Override
    public ReaderResult mingWen(String cmd) {
        ReaderResult readerResult = new ReaderResult();
        OBUManager.ServiceStatus serviceStatus = obuMan.TransCommand("0", cmd);
        if (serviceStatus.ServiceCode == 0) {
            String serviceInfo = serviceStatus.ServiceInfo;
            if (serviceInfo.endsWith("9000")) {
                readerResult.setSuccess(true);
                readerResult.setResult(serviceInfo);
                return readerResult;
            }
        }
        return readerResult;
    }

    @Override
    public ReaderResult esamMingWen(String cmd) {
        ReaderResult readerResult = new ReaderResult();
        OBUManager.ServiceStatus serviceStatus = obuMan.TransCommand("1", cmd);
        if (serviceStatus.ServiceCode == 0) {
            String serviceInfo = serviceStatus.ServiceInfo;
            if (serviceInfo.endsWith("9000")) {
                readerResult.setSuccess(true);
                readerResult.setResult(serviceInfo);
                return readerResult;
            }
        }
        return readerResult;
    }

    @Override
    public ReaderResult miWen(String cmds, boolean noNew) {
        //zhongjiao的设备 走不到密文指令的都是用的明文传输 为了保持和其他厂商设备的一致性
        //zhongjiao的密文走和明文一样的代码
        ReaderResult readerResult = new ReaderResult();
        OBUManager.ServiceStatus serviceStatus = obuMan.TransCommand("0", cmds);
        if (serviceStatus.ServiceCode == 0) {
            String serviceInfo = serviceStatus.ServiceInfo;
            if (serviceInfo.endsWith("9000")) {
                readerResult.setSuccess(true);
                readerResult.setResult(serviceInfo);
                return readerResult;
            }
        }
        return readerResult;
    }

    @Override
    public ReaderResult esamMiWen(String cmd) {
        //zhongjiao的设备 走不到密文指令的都是用的明文传输 为了保持和其他厂商设备的一致性
        //zhongjiao的密文走和明文一样的代码
        ReaderResult readerResult = new ReaderResult();
        OBUManager.ServiceStatus serviceStatus = obuMan.TransCommand("1", cmd);
        if (serviceStatus.ServiceCode == 0) {
            String serviceInfo = serviceStatus.ServiceInfo;
            if (serviceInfo.endsWith("9000")) {
                readerResult.setSuccess(true);
                readerResult.setResult(serviceInfo);
                return readerResult;
            }
        }
        return readerResult;
    }
}
