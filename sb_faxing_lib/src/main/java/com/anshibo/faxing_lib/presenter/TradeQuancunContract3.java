package com.anshibo.faxing_lib.presenter;


import android.content.Context;

import com.anshibo.faxing_lib.bean.CardBalanceQueryBean;
import com.anshibo.faxing_lib.bean.QcCardSuccessBean;

/**
 * 创建日期：2018/9/16 on 15:51
 * 描述:
 * 作者:王甜
 */
public interface TradeQuancunContract3 {

    interface View extends BaseReaderView {
        void showCardMsg(CardBalanceQueryBean cardBalanceQueryBean);

        void qcFinish(QcCardSuccessBean qcCardSuccessBean);

        void showQuancunProgress(String progress);

        void hidleQuancunProgress();
    }

    abstract class Presenter extends BaseReaderPresenter<View> {

        public Presenter(Context context) {
            super(context);
        }

        public abstract void getCardMsg();
    }
}
