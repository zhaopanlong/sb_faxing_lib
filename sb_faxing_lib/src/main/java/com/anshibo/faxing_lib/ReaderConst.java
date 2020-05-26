package com.anshibo.faxing_lib;

/**
 * Created by 王甜 on 2017/8/10.
 */

public class ReaderConst {
    public final static int QC_STEP_NONE = 9000;
    public final static int BLUE_TOOTH_NULL = 9014;
    public final static int QC_STEP_SUCCESS = 9015;//成功
    public final static int QC_STEP_FAIL = 9016;//失败

    public final static int QC_STEP_DISCONNECT = 9017;//断开连接
    public final static int QC_STEP_CARD_OK = 9018;//卡片未放好

    /**
     * 激活标记
     */
    public final static int CHENGGU_SLEEP = 1000;//成谷设置休眠时间
    public final static int GET_REMOVE_STATE = 1001;//获取拆卸状态
    public final static int GET_FILE_3F = 1002;//选择3f00
    public final static int GET_REMOVE_SERIAL_NUM = 1003;//修改拆卸状态获取随机数
    public final static int MODIFY_REMOVE_STATE = 1004;//修改拆卸状态


    public final static int ESAM_MULTIPLE_INSTRUCTIONS = 20000;//ESAM明文多条传输

    /**
     * 写卡标记
     */
    public final static int GET_FILE_DF = 1005;//选取df01
    public final static int GET_RANDOM_NUM = 1006;//获取随机数
    public final static int GET_CARD_NUM = 1007;//获取卡号
    public final static int WRITE_CARD15_DATA = 1008;//写0015卡标记
    public final static int WRITE_CARD16_DATA = 1009;//写0016卡标记
    public final static int GET_SN_NUM = 1010;//获取SN编号
    public final static int GET_FILE15_INFO = 1011;//获取0015文件信息
    public final static int GET_RANDOM16_NUM = 1012;//获取0016随机数
    public final static int GET_FILE16_INFO = 1013;//获取16文件信息
    public final static int GET_FILE3F_INFO = 1014;//完成绑定取3f00文件信息


    /**
     * 写标签标记
     */
    public final static int GET_SE_NUM = 1015;//获取se编号
    public final static int GET_FILE_DF01 = 1016;//选择df01文件
    public final static int WRITE_OBU_DATA = 1017;//写标签标记
    public final static int GET_OBU_RANDOM_NUM = 1018;//获取OBU随机数
    public final static int GET_DF_CAR_FILE = 1019;//进入df01目录
    public final static int GET_DF_SYS_INFO = 1020;//获取OBU随机数
    public final static int GET_DF_CAR_INFO_INFO = 1021;//获取车辆信息
    public final static int GET_DF_3f00_FILE = 1022;//获取车辆信息
    public final static int GET_DF_3f00_FILE1 = 1088;//获取车辆信息

    public final static int WRITE_OBU_MINGWEN_DATA = 1024;//写车辆信息明文
    public final static int MODIFY_REMOVE_MINGWEN_STATE = 1025;//写拆卸状态明文
    public final static int WRITE_CARD15_MINGWEN_DATA = 1026;//写0015卡标记明文
    public final static int WRITE_CARD16_MINGWEN_DATA = 1027;//写0016卡标记明文

    /**
     * 圈存标记
     */
    public final static int QC_STEP_READ_SE = 2001;//获取SE
    public final static int QC_STEP_READ_FILE = 2002;// 读文件信息
    public final static int QC_STEP_READ_QCINIT_BEFORE = 2003;//圈存初始化获取卡信息用于卡账查询
    public final static int QC_STEP_PIN_VERIFY = 2004;//校验PIN
    public final static int QC_STEP_BALANCE_QUERY = 2005;//卡账查询请求
    public final static int QC_STEP_READ_QCINIT = 2006;//正式圈存初始化信息
    public final static int QC_STEP_READ_SMALL_MONEY = 3005;//几张卡圈存16万的标记
    public final static int QC_STEP_REQUEST_QC_CMD = 2007;//获取圈存指令
    public final static int QC_STEP_QC_CMD_WRITE = 2008;//圈存写卡
    public final static int QC_STEP_CARD_BALANCE = 2009;//获取卡余额
    public final static int QC_STEP_REQUEST_FINISH = 2010;//圈存完成请求
    public final static int QC_STEP_QC_CMD_MINGWEN_WRITE = 2011;//圈存明文指令
    public final static int QC_STEP_READ_QC = 2012;//圈存密文指令
    public final static int QC_STEP_READ_ACCOUNT_BALANCE = 2013;//读取卡余额

    /**
     * 卡转账相关标记
     */
    public final static int TRANSFER_READ_CARD_NO = 3001;//读取卡号信息

    public final static int TRANSFER_WRITE_DATE = 3002;//记账卡写时间

    public final static int TRANSFER_WRITE_MINGWEN_DATE = 3010;//记账卡明文写时间

    public final static int TRANSFER_WRITE_MONEY = 3003;//记账卡写钱

    /**
     * 激活指令
     */
    public static final String REMOVE_STATE_CMD = "00B0810a11";//获取拆卸状态指令
    public static final String GET_FILE_3F_CMD = "00a40000023f00";//进入3f00文件
    public static final String GET_REMOVE_SERIAL_NUM_CMD = "0084000004";//获取修改拆卸状态获取随机数


    /**
     * 写卡指令
     */
    public static final String IN_DF_FILE_CMD = "00A40000021001";//进入df01文件
    public static final String GET_RANDOM_NUM_CMD = "0084000004";//写卡获取4字节随机数
    //    public static final String GET_RANDOM_NUM_CMD = "0084000008";//写卡获取8字节随机数
    public static final String GET_FILE16INFO_CMD = "00B0960037";//读持卡人信息
    public static final String GET_FILE15INFO_CMD = "00B095002B";//读15文件信息
    public static final String GET_CARD_INFO_CMD = "00B0950014";//读0015文件
    public static final String GET_SE_NUM_CMD = "55000A010000000000000000005E";//获取SE编号指令
    public static final String GET_SN_NUM_CMD = "00B0810a08";//获取SN编号指令


    public static final String GET_CAR_INFO_CMD = "00B400000A00000000000000004F00";//读OBU车辆信息
    public static final String GET_SYS_INFO_CMD = "00B081001b";//读OBU3f00文件信息

    /**
     * 写签指令
     */
    public static final String GET_OBU_DF_CMD = "00a4000002df01";//进入df01文件


    /**
     * 圈存指令
     */
    public static final String QC_READ_INFO = "00A40000021001";//圈存读信息指令
    public static final String CHECKPIN_CMD = "0020000006313233343536";//表示校验pin(指令+密码（313233343536）)
    public static final String QUAN_INIT_CMD = "805000020B01";//圈存初始化命令
    public static final String READ_CARD_BALANCE = "805C000204";//获取卡余额
}
