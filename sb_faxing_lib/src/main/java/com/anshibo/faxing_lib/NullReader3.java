package com.anshibo.faxing_lib;

import android.bluetooth.BluetoothDevice;

/**
 * @author zhaopanlong
 * @createtime：2020/5/22 下午3:49
 */
public class NullReader3 implements IReader {
    @Override
    public ReaderResult connect(ReaderDevice device) {
        ReaderResult readerResult = new ReaderResult();
        readerResult.setSuccess(false);
        readerResult.setError("reader未初始化");
        return readerResult;
    }

    @Override
    public void disConnect() {

    }

    @Override
    public ReaderResult getSE() {
        ReaderResult readerResult = new ReaderResult();
        readerResult.setSuccess(false);
        readerResult.setError("reader未初始化");
        return readerResult;
    }

    @Override
    public ReaderResult mingWen(String cmd) {
        ReaderResult readerResult = new ReaderResult();
        readerResult.setSuccess(false);
        readerResult.setError("reader未初始化");
        return readerResult;
    }

    @Override
    public ReaderResult esamMingWen(String cmd) {
        ReaderResult readerResult = new ReaderResult();
        readerResult.setSuccess(false);
        readerResult.setError("reader未初始化");
        return readerResult;
    }

    @Override
    public ReaderResult miWen(String cmds, boolean noNew) {
        ReaderResult readerResult = new ReaderResult();
        readerResult.setSuccess(false);
        readerResult.setError("reader未初始化");
        return readerResult;
    }

    @Override
    public ReaderResult esamMiWen(String cmd) {
        ReaderResult readerResult = new ReaderResult();
        readerResult.setSuccess(false);
        readerResult.setError("reader未初始化");
        return readerResult;
    }
}
