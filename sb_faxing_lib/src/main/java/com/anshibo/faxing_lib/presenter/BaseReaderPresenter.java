package com.anshibo.faxing_lib.presenter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.anshibo.faxing_lib.ReadException3;
import com.anshibo.faxing_lib.ReaderDevice;
import com.anshibo.faxing_lib.ReaderManager3;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zhaopanlong
 * @createtime：2020/5/22 下午3:42
 */
public class BaseReaderPresenter<V extends BaseReaderView> {
    protected ReaderManager3 readerManager3;
    protected Context mContext;
    protected V mView;

    public BaseReaderPresenter(Context context) {
        mContext = context;
        readerManager3 = new ReaderManager3();
    }

    /**
     * 连接读卡器操作
     *
     * @param currentDevice
     */
    public void connect(final ReaderDevice currentDevice) {
        readerManager3.initReader(currentDevice.getDeviceName(),mContext);
        mView.showReaderLoading();
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                readerManager3.content(currentDevice);
                emitter.onNext(true);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean b) {
                        mView.hildeReaderLoading();
                        mView.connectSuccess();

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof ReadException3) {
                            mView.readerError((ReadException3) e);
                        }
                        readerManager3.disConnect();
                        mView.hildeReaderLoading();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public boolean isConnect() {
        return readerManager3.isConnect();
    }

    /**
     * 与view绑定
     * 此方法必须要调用
     * 不然会报错
     *
     * @param view
     */
    public void setView(V view) {
        this.mView = view;
    }

    /**
     * 销毁presenter
     */
    public void destory() {
        if (this.mView != null) {
            mView = null;
        }
    }
}
