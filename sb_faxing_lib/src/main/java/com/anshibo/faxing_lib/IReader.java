package com.anshibo.faxing_lib;

import android.bluetooth.BluetoothDevice;

/**
 * @author zhaopanlong
 * @createtime：2020/4/16 上午9:44
 */
public interface IReader {
    ReaderResult connect(ReaderDevice device);

    void disConnect();

     ReaderResult getSE();

    ReaderResult mingWen(String cmd);

    ReaderResult esamMingWen(String cmd);

    ReaderResult miWen(String cmds, boolean noNew);

    ReaderResult esamMiWen(String cmd);

}
