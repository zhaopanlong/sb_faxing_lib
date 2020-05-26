package com.anshibo.faxing_lib.api;


import com.anshibo.faxing_lib.bean.CardBalanceQueryBean;
import com.anshibo.faxing_lib.bean.DeviceBean;
import com.anshibo.faxing_lib.bean.QcCardSuccessBean;
import com.anshibo.faxing_lib.bean.QcInitBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 创建日期：2018/9/16 on 15:35
 * 描述:
 * 作者:王甜
 */
public interface TradeApiServer3 {
    @POST("etcpay/appTrans/appTranserSearch")
    Call<BaseReaderBean<CardBalanceQueryBean>> getETCCard(@Body Map map);

    @POST("finance/quanCun/appTranserInitNew")
    Call<QcInitBean> getQcInit(@Body Map map);

    @POST("finance/quanCun/getDeviceInfoBySe")
    Call<DeviceBean> getDeviceBySe(@Body Map map);

    @POST("/finance/quanCun/getDeviceInfoBySn")
    Call<DeviceBean> getDeviceInfoBySn(@Body Map map);

    @POST("etcpay/appTrans/etcCardQcFinish")
    Call<BaseReaderBean<QcCardSuccessBean>> etcCardQcFinish(@Body Map map);
}
