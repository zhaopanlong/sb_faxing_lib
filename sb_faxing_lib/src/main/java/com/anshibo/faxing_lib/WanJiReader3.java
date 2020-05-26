package com.anshibo.faxing_lib;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Base64;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wanji.etc.obu.service.ServiceStatus;
import wanji.etc.obu.service.wjOBU;

/**
 * @author zhaopanlong
 * @createtime：2020/4/23 上午9:16
 */
public class WanJiReader3 implements IReader {
    private static final String TAG = "万集设备";
    private static WanJiReader3 instance;
    private wjOBU obuMan;
    private Context mContext;

    public static WanJiReader3 getInstance(Context context) {
        if (instance == null) {
            instance = new WanJiReader3(context);
        }
        return instance;
    }

    private WanJiReader3(Context context) {
        obuMan = wjOBU.getInstance();
        mContext = context;
    }

    @Override
    public ReaderResult connect(ReaderDevice device) {
        ReaderResult readerResult = new ReaderResult();
        ServiceStatus serviceStatus = obuMan.connectDevice(device.getBluetoothDevice(), mContext);
        if (serviceStatus.ServiceCode == 0) {
            //连接失败
            readerResult.setSuccess(true);
            return readerResult;
        }
        readerResult.setSuccess(false);
        readerResult.setError(TAG+":连接失败："+":错误信息："+FaxingGsonHelper.toJson(serviceStatus));
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
        if (deivceSE.ServiceCode == 0){
            readerResult.setSuccess(true);
            readerResult.setResult(deivceSE.ServiceInfo);
            return readerResult;
        }
        readerResult.setSuccess(false);
        readerResult.setError(TAG+":获取se失败："+":错误信息："+FaxingGsonHelper.toJson(deivceSE));
        return readerResult;
    }

    @Override
    public ReaderResult mingWen(String cmd) {
        ReaderResult readerResult = new ReaderResult();
        ServiceStatus serviceStatus = obuMan.TransMingwenCommand("10", cmd, (cmd.length()) / 2);
        if (serviceStatus.ServiceCode == 0){
            readerResult.setSuccess(true);
            String serviceInfo = serviceStatus.ServiceInfo.replaceAll(" ", "").trim();
            LogUtils.i("带签名的明文指令结果："+serviceInfo);
            serviceInfo = replaceBlank(serviceInfo).trim();//去除空格和换行
            String[] split = serviceInfo.split("&");
            String ming = split[0];
            LogUtils.i("ming::" + ming);
            if (ming.trim().endsWith("9000")){
                readerResult.setSuccess(true);
                if (split.length>1){
                    readerResult.setResult(ming+"00"+split[1]);
                }else {
                    readerResult.setResult(ming);
                }
                return readerResult;
            }
        }
        readerResult.setSuccess(false);
        readerResult.setError(TAG+":明文指令："+cmd+":错误信息："+FaxingGsonHelper.toJson(serviceStatus));
        return readerResult;
    }

    @Override
    public ReaderResult esamMingWen(String cmd) {
        ReaderResult readerResult = new ReaderResult();
        ServiceStatus serviceStatus = obuMan.TransMingwenCommand("20", cmd, (cmd.length()) / 2);
        if (serviceStatus.ServiceCode == 0){
            readerResult.setSuccess(true);
            String serviceInfo = serviceStatus.ServiceInfo.replaceAll(" ", "").trim();
            serviceInfo = replaceBlank(serviceInfo).trim();//去除空格和换行
            String[] split = serviceInfo.split("&");
            String ming = split[0];
            LogUtils.i("ming::" + ming);
            if (ming.trim().endsWith("9000")){
                readerResult.setSuccess(true);
                readerResult.setResult(ming);
                return readerResult;
            }
        }
        readerResult.setSuccess(false);
        readerResult.setError(TAG+":esam明文指令："+cmd+":错误信息："+FaxingGsonHelper.toJson(serviceStatus));
        return readerResult;
    }

    @Override
    public ReaderResult miWen(String cmds, boolean noNew) {
        ReaderResult readerResult = new ReaderResult();

        String cmd2 = getCmd2(cmds);
        ServiceStatus serviceStatus = obuMan.TransMiwenCommand(cmd2);
        String serviceInfo = serviceStatus.ServiceInfo.replaceAll(" ", "").trim();
        serviceInfo = replaceBlank(serviceInfo).trim();//去除空格和换行
        LogUtils.i(TAG+"密文返回结果："+serviceInfo);
        String[] split = serviceInfo.split("&");
        String ming = split[0];
        String qianming = "";
        if (split.length > 1) {
            qianming = split[1];
        }

        if (ming.endsWith("9000")){
            readerResult.setSuccess(true);
            readerResult.setResult(ming+"00"+qianming);
            return readerResult;
        }
        readerResult.setSuccess(false);
        readerResult.setError(TAG+":密文指令："+cmds+":错误信息："+FaxingGsonHelper.toJson(serviceStatus));
        return readerResult;
    }

    @Override
    public ReaderResult esamMiWen(String cmd) {
        ReaderResult readerResult = new ReaderResult();
        String cmd2 = getCmd2(cmd);
        ServiceStatus serviceStatus = obuMan.TransESAMMiwenCommand(cmd2.length() / 2, cmd2);
        if (serviceStatus.ServiceCode == 0){
            String serviceInfo = serviceStatus.ServiceInfo.replaceAll(" ", "").trim();
            serviceInfo = replaceBlank(serviceInfo).trim();//去除空格和换行
            String[] spilt = null;
            if (serviceInfo.startsWith("9000")){
                readerResult.setSuccess(true);
                readerResult.setResult(serviceInfo);
                return readerResult;
            }
        }
        readerResult.setSuccess(false);
        readerResult.setError(TAG+":esam密文指令："+cmd+":错误信息："+FaxingGsonHelper.toJson(serviceStatus));
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

    private String getCmd2(String cmds){
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
