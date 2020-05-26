package com.anshibo.faxing_lib;

import android.content.Context;
import android.text.TextUtils;

public class ReaderManager3 {
    IReader iReader;
    boolean isConnect;
    ReaderDevice readerDevice;

    public ReaderManager3() {

    }

    public IReader initReader(String mDeviceName, Context mContext) {
        //为了防止ireader空指针 设置一个默认值
        if (!TextUtils.isEmpty(mDeviceName)) {
            if (mDeviceName.startsWith("ZYT008")) {
                //万集设备
                iReader = WanJiReader3.getInstance(mContext);
            } else if (mDeviceName.startsWith("ZYT012")) {
                //握奇设备
                iReader = WoqiReader3.getInstance(mContext);
            } else if (mDeviceName.startsWith("JULI") || mDeviceName.startsWith("JL") || mDeviceName.startsWith("juli") || mDeviceName.startsWith("ZYT003")) {
                //聚利
                iReader = JuLiReader3.getInstance(mContext);
            } else if (mDeviceName.startsWith("0000") || mDeviceName.startsWith("5218") || mDeviceName.startsWith("ZYT018")) {
                //成谷
                iReader = CguReader3.getInstance(mContext);
            } else if (mDeviceName.startsWith("ZYT002")) {
                //金溢
                iReader = JinYiReader3.getInstance(mContext);
            } else if (mDeviceName.startsWith("ZYT001")) {
                //艾特斯
                iReader = AltersReader3.getInstance(mContext);
            } else if (mDeviceName.startsWith("Etc_Device") || mDeviceName.startsWith("ZYT013")) {
                //粤高科
                iReader = YGKReader3.getInstance(mContext);
            } else if (mDeviceName.startsWith("ZYT007")) {
                //千方
                iReader = QFReader3.getInstance(mContext);
            } else if (mDeviceName.startsWith("ZYT051")) {
                //楚天
                //  iReader = ChuTianReader3
            } else if (mDeviceName.startsWith("ZYT059")) {
                //天地融
                iReader = TDRReader3.getInstance(mContext);
            } else if (mDeviceName.startsWith("FAIRWIN") || mDeviceName.startsWith("ZYT016")) {
                //中交汇能
                iReader = ZhongJiaoReader3.getInstance(mContext);
            } else if (mDeviceName.equals("nfc")){
                //nfc
                iReader = NFCReader3.getInstance(mContext);
            }else {
                iReader = new NullReader3();
            }
        } else {
            iReader = new NullReader3();
        }
        return iReader;
    }

    //连接
    public void content(ReaderDevice device) throws ReadException3 {
        if (isConnect) {
            return;
        }
        if (device == null) {
            // throw new ReadException3(ReaderConst.BLUE_TOOTH_NULL, "");
            throw new ReadException3(FaxingErrorCode.ERR, "");
        }

        readerDevice = device;
        ReaderResult connect = iReader.connect(device);
        if (!connect.isSuccess()) {
            //throw new ReadException3(ReaderConst.QC_STEP_FAIL, connect.getError());
            throw new ReadException3(FaxingErrorCode.ERR, connect.getError());
        }
        isConnect = true;
    }

    public void disConnect() {
        isConnect = false;
        iReader.disConnect();
        this.readerDevice = null;
    }

    /**
     * 获取ETC卡号
     *
     * @return
     */
    public String getEtcCardNum() throws ReadException3 {
        //先进入df01目录
        String carddf01 = etcCarddf01();
        return carddf01.substring(46).substring(20, 40);
    }

    //进入df01目录
    public String etcCarddf01() throws ReadException3 {
        ReaderResult readerResult = iReader.mingWen(ReaderConst.IN_DF_FILE_CMD);
        if (!readerResult.isSuccess()) {
            //throw new ReadException3(ReaderConst.GET_FILE_DF, readerResult.getError());
            throw new ReadException3(FaxingErrorCode.ERR, readerResult.getError());
        }
        return readerResult.getResult();
    }

    //读取f15文件
    private String file15() throws ReadException3 {
        ReaderResult readerResult = iReader.mingWen(ReaderConst.GET_FILE15INFO_CMD);
        if (!readerResult.isSuccess()) {
            //throw new ReadException3(ReaderConst.TRANSFER_READ_CARD_NO, readerResult.getError());
            throw new ReadException3(FaxingErrorCode.ERR, readerResult.getError());
        }
        return readerResult.getResult();
    }

    //获取SE
    public String getSe() throws ReadException3 {
        ReaderResult se = iReader.getSE();
        if (!se.isSuccess()) {
            throw new ReadException3(FaxingErrorCode.QC_READ_SE_ERR, se.getError());
        }
        String result = se.getResult();
        return result;
    }

    //写卡4位随机数
    public String getCardRundom() throws ReadException3 {
        ReaderResult readerResult = iReader.mingWen(ReaderConst.GET_RANDOM_NUM_CMD);
        if (!readerResult.isSuccess()) {
            //  throw new ReadException3(ReaderConst.GET_RANDOM_NUM, readerResult.getError());
            throw new ReadException3(FaxingErrorCode.ERR, readerResult.getError());
        }
        return readerResult.getResult().substring(0, readerResult.getResult().length() - 4);
    }

    //记账卡明文写时间
    public void writeMingDate(String cmd) throws ReadException3 {
        ReaderResult readerResult = iReader.mingWen(cmd);
        if (!readerResult.isSuccess()) {
            // throw new ReadException3(ReaderConst.TRANSFER_WRITE_MINGWEN_DATE, readerResult.getError());
            throw new ReadException3(FaxingErrorCode.ERR, readerResult.getError());
        }
    }

    //校验pin
    public String checkPin() throws ReadException3 {
        ReaderResult readerResult = iReader.mingWen(ReaderConst.CHECKPIN_CMD);
        if (!readerResult.isSuccess()) {
            throw new ReadException3(FaxingErrorCode.QC_PIN_VERIFY_ERR, readerResult.getError());
        }
        return readerResult.getResult();
    }

    //圈存初始化 貌似有两个地方用到 一个获取卡信息 一个才是正式的圈存初始化
    public String[] qcInit(int quancunMoney) throws ReadException3 {
        //转换成16进制
        String hexMoney = Integer.toHexString(quancunMoney).toUpperCase();
        LogUtils.e("hexMoney===" + hexMoney);

        //将16进制转换成4位字节,不足则前补0
        if (hexMoney.length() < 8) {
            int len = 8 - hexMoney.length();
            for (int i = 0; i < len; i++) {
                hexMoney = "0" + hexMoney;
            }
        }
        LogUtils.i("hexMoney::" + hexMoney);
        String se = getSe();
        if (se.equals("ffffffffffffffff") || se.equals("FFFFFFFFFFFFFFFF")) {
            se = getSN();
        }
        //拼接命令  国标+金额+终端号
        String hexComman = ReaderConst.QUAN_INIT_CMD + hexMoney + se.substring(4);
        ReaderResult readerResult = iReader.mingWen(hexComman);
        if (!readerResult.isSuccess()) {
            throw new ReadException3(FaxingErrorCode.QC_QCINIT_BEFORE_ERR, readerResult.getError());
        }
        String[] result = new String[2];
        String cmdResult = readerResult.getResult();
        result[0] = cmdResult.substring(0, 36);
        if (cmdResult.length() > 165) {
            result[1] = cmdResult.substring(38, 166);
        } else {
            result[1] = "";
        }

        return result;
    }

    //往卡里面写钱
    public void writeMoney(String cmd) throws ReadException3 {
        ReaderResult readerResult = iReader.mingWen(cmd);
        if (!readerResult.isSuccess()) {
            //throw new ReadException3(ReaderConst.TRANSFER_WRITE_MONEY, readerResult.getError());
            throw new ReadException3(FaxingErrorCode.ERR, readerResult.getError());
        }
    }

    public void quanCunMi(String cmd) throws ReadException3 {
        ReaderResult readerResult = iReader.miWen(cmd, false);
        if (!readerResult.isSuccess()) {
            throw new ReadException3(FaxingErrorCode.QC_READ_QC_ERR, readerResult.getError());
        }
    }

    public void quanCunMing(String cmd) throws ReadException3 {
        ReaderResult readerResult = iReader.mingWen(cmd);
        if (!readerResult.isSuccess()) {
            throw new ReadException3(FaxingErrorCode.QC_READ_QC_ERR, readerResult.getError());
        }
    }

    //获取卡余额
    public String readCardBalance() throws ReadException3 {
        ReaderResult readerResult = iReader.mingWen(ReaderConst.READ_CARD_BALANCE);
        if (!readerResult.isSuccess()) {
            throw new ReadException3(FaxingErrorCode.QC_READ_ACCOUNT_BALANCE_ERR, readerResult.getError());
        }
        return readerResult.getResult();
    }

    //记账卡些时间
    public void transferWriteDate(String cmd) throws ReadException3 {
        ReaderResult readerResult = iReader.miWen(cmd, false);
        if (!readerResult.isSuccess()) {
            // throw new ReadException3(ReaderConst.TRANSFER_WRITE_DATE, readerResult.getError());
            throw new ReadException3(FaxingErrorCode.ERR, readerResult.getError());
        }
    }

    //写0015文件指令
    public void write0015(String cmd) throws ReadException3 {
        ReaderResult readerResult = iReader.miWen(cmd, true);
        if (!readerResult.isSuccess()) {
            //throw new ReadException3(ReaderConst.WRITE_CARD15_DATA, readerResult.getError());
            throw new ReadException3(FaxingErrorCode.ERR, readerResult.getError());
        }
    }

    //写0016卡标记
    public void write0016(String cmd) throws ReadException3 {
        ReaderResult readerResult = iReader.miWen(cmd, true);
        if (!readerResult.isSuccess()) {
            // throw new ReadException3(ReaderConst.WRITE_CARD16_DATA, readerResult.getError());
            throw new ReadException3(FaxingErrorCode.ERR, readerResult.getError());
        }
    }

    //卡注销指令
    public void cardZhuxiao(String cmd) throws ReadException3 {
        ReaderResult readerResult = iReader.mingWen(cmd);
        if (!readerResult.isSuccess()) {
//            throw new ReadException3(ReaderConst.WRITE_CARD15_MINGWEN_DATA, readerResult.getError());
            throw new ReadException3(FaxingErrorCode.ERR, readerResult.getError());
        }
    }

    /***********************卡相关指令**************************************/


    /***********************标签相关指令**************************************/
    //获取SN
    public String getSN() throws ReadException3 {
        ReaderResult readerResult = iReader.esamMingWen(ReaderConst.GET_SN_NUM_CMD);
        if (!readerResult.isSuccess()) {
//            throw new ReadException3(ReaderConst.GET_SN_NUM, readerResult.getError());
            throw new ReadException3(FaxingErrorCode.ERR, readerResult.getError());
        }
        String result = readerResult.getResult();
        String sn = result.substring(0, 16);
        return sn;
    }

    //进入3f00文件
    public void read3F00() throws ReadException3 {
        ReaderResult readerResult = iReader.esamMingWen(ReaderConst.GET_FILE_3F_CMD);
        if (!readerResult.isSuccess()) {
//            throw new ReadException3(ReaderConst.GET_DF_3f00_FILE, readerResult.getError());
            throw new ReadException3(FaxingErrorCode.ERR, readerResult.getError());
        }
    }

    //获取拆卸位随机数
    public String getRemoveSerialNum() throws ReadException3 {
        ReaderResult readerResult = iReader.esamMingWen(ReaderConst.GET_REMOVE_SERIAL_NUM_CMD);
        if (!readerResult.isSuccess()) {
//            throw new ReadException3(ReaderConst.GET_REMOVE_SERIAL_NUM, readerResult.getError());
            throw new ReadException3(FaxingErrorCode.ERR, readerResult.getError());
        }
        return readerResult.getResult();
    }

    //写拆卸位明文
    public void removeOBUStateMing(String cmd) throws ReadException3 {
        ReaderResult readerResult = iReader.esamMingWen(cmd);
        if (!readerResult.isSuccess()) {
//            throw new ReadException3(ReaderConst.MODIFY_REMOVE_MINGWEN_STATE, readerResult.getError());
            throw new ReadException3(FaxingErrorCode.ERR, readerResult.getError());
        }
    }

    //修改拆卸位
    public void motifyOBUState(String cmd) throws ReadException3 {
        ReaderResult readerResult = iReader.esamMiWen(cmd);
        if (!readerResult.isSuccess()) {
//            throw new ReadException3(ReaderConst.MODIFY_REMOVE_STATE, readerResult.getError());
            throw new ReadException3(FaxingErrorCode.ERR, readerResult.getError());
        }
    }

    //obudf01目录
    public void obudf01() throws ReadException3 {
        ReaderResult readerResult = iReader.esamMingWen(ReaderConst.GET_OBU_DF_CMD);
        if (!readerResult.isSuccess()) {
//            throw new ReadException3(ReaderConst.GET_FILE_DF01, readerResult.getError());
            throw new ReadException3(FaxingErrorCode.ERR, readerResult.getError());
        }
    }

    //写OBU数据
    public void writeObuData(String cmd) throws ReadException3 {
        ReaderResult readerResult = iReader.esamMiWen(cmd);
        if (!readerResult.isSuccess()) {
//            throw new ReadException3(ReaderConst.WRITE_OBU_DATA, readerResult.getError());
            throw new ReadException3(FaxingErrorCode.ERR, readerResult.getError());
        }
    }

    /***********************标签相关指令**************************************/

    public boolean isConnect() {
        return isConnect;
    }

    public IReader getiReader() {
        return iReader;
    }
}
