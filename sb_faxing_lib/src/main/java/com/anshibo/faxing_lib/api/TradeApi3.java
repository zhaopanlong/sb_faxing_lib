package com.anshibo.faxing_lib.api;

import com.anshibo.faxing_lib.BuildConfig;
import com.anshibo.faxing_lib.ReadException3;
import com.anshibo.faxing_lib.bean.CardBalanceQueryBean;
import com.anshibo.faxing_lib.bean.DeviceBean;
import com.anshibo.faxing_lib.bean.QcCardSuccessBean;
import com.anshibo.faxing_lib.bean.QcInitBean;


import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @author Administrator
 * @createtime：2018/8/31 14:38
 */
public class TradeApi3 {
    private static TradeApi3 instance;
    private TradeApiServer3 mService;

    public static TradeApi3 getInstance() {
        if (instance == null) {
            instance = new TradeApi3();
        }
        return instance;
    }

    private TradeApi3() {
        if (BuildConfig.DEBUG){
            mService = ReaderApiBuild.createApi("https://wx.cywetc.com/", TradeApiServer3.class);
        }else {
            mService = ReaderApiBuild.createApi("https://weixin.cywetc.com/", TradeApiServer3.class);
        }
    }

    public CardBalanceQueryBean getEtcCardData(Map map) throws ReadException3 {

        try {
            Response<BaseReaderBean<CardBalanceQueryBean>> execute = mService.getETCCard(map).execute();
            BaseReaderBean<CardBalanceQueryBean> body = execute.body();
            if (body.isSuccess()) {
                return body.data;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new ReadException3("");
    }

    public QcInitBean getQcinit(Map map) throws ReadException3 {
        Call<QcInitBean> qcInit = mService.getQcInit(map);
        try {
            Response<QcInitBean> execute = qcInit.execute();
            QcInitBean body = execute.body();
            if (body.getCode() == 1) {
                return body;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new ReadException3("");
    }

    public DeviceBean getDeviceBySe(String se) throws ReadException3 {
        Map map = new LinkedHashMap();
        map.put("se", se);
        Call<DeviceBean> deviceBySe = mService.getDeviceBySe(map);
        try {
            DeviceBean body = deviceBySe.execute().body();
            if (body.isSuccess()) {
                return body;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new ReadException3("");
    }

    public DeviceBean getDeviceBySn(String sn) throws ReadException3 {
        Map map = new LinkedHashMap();
        map.put("sn", sn);
        Call<DeviceBean> deviceBySn = mService.getDeviceInfoBySn(map);
        try {
            DeviceBean body = deviceBySn.execute().body();
            if (body.isSuccess()) {
                return body;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new ReadException3("");
    }

    public QcCardSuccessBean qcFinish(Map map) throws ReadException3 {
        Call<BaseReaderBean<QcCardSuccessBean>> qcFinishCall = mService.etcCardQcFinish(map);
        try {
            BaseReaderBean<QcCardSuccessBean> body = qcFinishCall.execute().body();
            if (body.isSuccess()) {
                return body.data;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new ReadException3("");
    }
}
