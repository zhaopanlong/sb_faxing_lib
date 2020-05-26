package com.anshibo.faxing_lib;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Base64;

import com.bluetoothobu.obusdk.OBUManager;
import com.bluetoothobu.obusdk.ServiceStatus;

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
        return null;
    }

    @Override
    public ReaderResult esamMingWen(String cmd) {
        return null;
    }

    @Override
    public ReaderResult miWen(String cmds, boolean noNew) {
        return null;
    }

    @Override
    public ReaderResult esamMiWen(String cmd) {
        return null;
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
