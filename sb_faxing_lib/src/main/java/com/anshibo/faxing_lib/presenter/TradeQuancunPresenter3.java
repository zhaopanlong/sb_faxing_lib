package com.anshibo.faxing_lib.presenter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.anshibo.faxing_lib.HexBytes;
import com.anshibo.faxing_lib.LogUtils;
import com.anshibo.faxing_lib.NFCReader3;
import com.anshibo.faxing_lib.ReadException3;
import com.anshibo.faxing_lib.api.TradeApi3;
import com.anshibo.faxing_lib.bean.CardBalanceQueryBean;
import com.anshibo.faxing_lib.bean.DeviceBean;
import com.anshibo.faxing_lib.bean.QcCardSuccessBean;
import com.anshibo.faxing_lib.bean.QcInitBean;


import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 圈存的业务逻辑
 * 主要包含了 获取etc卡信息
 * 圈存两个方法
 * @author zhaopanlong
 * @createtime：2020/4/24 下午3:27
 */
public class TradeQuancunPresenter3 extends TradeQuancunContract3.Presenter {
    private TradeApi3 tradeApi3;
    private SharedPreferences sp;


    public TradeQuancunPresenter3(Context context) {
        super(context);
        sp = mContext.getSharedPreferences("config", 0);
        tradeApi3 = TradeApi3.getInstance();
    }

    /**
     * 获取etc卡信息
     */
    @Override
    public void getCardMsg() {
        mView.showQuancunProgress("读卡中,请不要移动ETC卡");
        Observable.create(new ObservableOnSubscribe<Map>() {
            @Override
            public void subscribe(ObservableEmitter<Map> emitter) throws Exception {
                if (readerManager3 == null) {
                    emitter.onError(new ReadException3(""));
                }
                Map params = new LinkedHashMap();
                //获取sn
                String sn = readerManager3.getSN();
                LogUtils.i("sn::" + sn);
                params.put("sn", sn);
                //获取卡号
                String etcCardNum = readerManager3.getEtcCardNum();
                LogUtils.i("etcCardNum::" + etcCardNum);
                params.put("cardNo", etcCardNum);
                //圈存初始化
                String[] qcInit = readerManager3.qcInit(0);
                params.put("cardInfo", qcInit[0]);
                emitter.onNext(params);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Function<Map, ObservableSource<CardBalanceQueryBean>>() {
                    @Override
                    public ObservableSource<CardBalanceQueryBean> apply(Map map) throws Exception {
                        //联网获取etc卡信息
                        CardBalanceQueryBean etcCardData = tradeApi3.getEtcCardData(map);
                        String cardInfo = (String) map.get("cardInfo");
                        etcCardData.setCard_balance(Integer.parseInt(cardInfo.substring(0, 8), 16));
                        return Observable.just(etcCardData);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CardBalanceQueryBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CardBalanceQueryBean s) {
                        mView.hidleQuancunProgress();
                        mView.showCardMsg(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof ReadException3) {
                            mView.readerError((ReadException3) e);
                        }
                        readerManager3.disConnect();
                        mView.hidleQuancunProgress();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 圈存操作
     *
     * @param money    圈存的钱数
     * @param isBuQaun 是否为补圈存
     */
    public void quanCun(final int money, boolean isBuQaun) {
        if (isBuQaun) {
            mView.showQuancunProgress("正在补圈存中，请不要移动ETC卡。");
        } else {
            mView.showQuancunProgress("正在圈存，请不要移动ETC卡。");
        }
        Observable.create(new ObservableOnSubscribe<Map>() {
            @Override
            public void subscribe(ObservableEmitter<Map> emitter) throws Exception {
                Map params = new LinkedHashMap();
                //获取sn
                String sn = readerManager3.getSN();
                params.put("terminal", sn);
                //获取se
                String se = readerManager3.getSe();
                LogUtils.i("se::" + se);
                //获取设备信息 如果se为空或者是nfc读卡跳过此步骤
                if (TextUtils.isEmpty(se) || readerManager3.getiReader() instanceof NFCReader3) {
                } else if (se.equals("FFFFFFFFFFFFFFFF") || se.equals("ffffffffffffffff")) {
                    DeviceBean deviceBean = tradeApi3.getDeviceBySn(sn);
                    params.put("obuType", deviceBean.getObuType());
                    params.put("se", sn);
                } else {
                    DeviceBean deviceBean = tradeApi3.getDeviceBySe(se);
                    params.put("obuType", deviceBean.getObuType());
                    params.put("se", se);
                }
                //获取卡号
                String etcCardNum = readerManager3.getEtcCardNum();
                LogUtils.i("etcCardNum::" + etcCardNum);
                params.put("cardNo", etcCardNum);
                //圈存初始化
                String[] qcInit = readerManager3.qcInit(money);
                params.put("cardInfo", qcInit[0]);
                params.put("operationMoney", money + "");
                params.put("signData", Base64.encodeToString(HexBytes.hex2Bytes(qcInit[1]), Base64.DEFAULT));
                params.put("employeeNo", sp.getString("mobile", ""));
                emitter.onNext(params);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Function<Map, ObservableSource<Map>>() {
                    @Override
                    public ObservableSource<Map> apply(Map map) throws Exception {
                        QcInitBean qcInitBean = tradeApi3.getQcinit(map);
                        if (qcInitBean.isEncry()) {
                            readerManager3.quanCunMi(qcInitBean.getShsmk());
                        } else {
                            readerManager3.quanCunMing(qcInitBean.getShsmk());
                        }
                        map.put("tradeNum", qcInitBean.getTradeNum());
                        map.put("userId", sp.getString("memberId", ""));
                        return Observable.just(map);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<Map, ObservableSource<QcCardSuccessBean>>() {
                    @Override
                    public ObservableSource<QcCardSuccessBean> apply(Map map) throws Exception {
                        QcCardSuccessBean qcCardSuccessBean = tradeApi3.qcFinish(map);
                        qcCardSuccessBean.setBusinessNo((String) map.get("tradeNum"));
                        return Observable.just(qcCardSuccessBean);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<QcCardSuccessBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(QcCardSuccessBean map) {
                        //圈存完成
                        mView.qcFinish(map);
                        readerManager3.disConnect();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ReadException3) {
                            mView.readerError((ReadException3) e);
                        }
                        readerManager3.disConnect();
                        mView.hidleQuancunProgress();
                    }

                    @Override
                    public void onComplete() {
                        mView.hidleQuancunProgress();
                    }
                });
    }
}
