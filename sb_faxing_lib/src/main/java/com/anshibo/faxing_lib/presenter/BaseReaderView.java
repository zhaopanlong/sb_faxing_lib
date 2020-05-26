package com.anshibo.faxing_lib.presenter;

import com.anshibo.faxing_lib.ReadException3;

/**
 * @author zhaopanlong
 * @createtime：2020/5/22 下午3:54
 */
public interface BaseReaderView  {
    void connectSuccess();

    void readerError(ReadException3 readException3);

    public void showReaderLoading();

    public void hildeReaderLoading();
}
