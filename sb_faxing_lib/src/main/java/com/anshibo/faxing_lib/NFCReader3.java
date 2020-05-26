package com.anshibo.faxing_lib;

import android.content.Context;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Parcelable;

/**
 * @author zhaopanlong
 * @createtime：2020/5/26 下午4:15
 */
public class NFCReader3 implements IReader {
    private static NFCReader3 nfcReader3;
    private Context mContext;
    Parcelable p;
    private Iso7816.Tag isotag = null;
    //sn号码
    String terminalNo = "0000000000000002";

    public static NFCReader3 getInstance(Context context) {
        if (nfcReader3 == null) {
            nfcReader3 = new NFCReader3(context);
        }
        return nfcReader3;
    }

    private NFCReader3(Context context) {
        mContext = context;
    }

    @Override
    public ReaderResult connect(ReaderDevice device) {
        ReaderResult readerResult = new ReaderResult();
        p = device.getNfcDevice().p;
        final Tag tag = (Tag) p;
        final IsoDep isoDep = IsoDep.get(tag);
        if (isoDep != null) {
            isotag = new Iso7816.Tag(isoDep);
            isotag.connect();
            readerResult.setSuccess(true);
            return readerResult;
        }
        return readerResult;
    }

    @Override
    public void disConnect() {
        isotag.close();
    }

    @Override
    public ReaderResult getSE() {
        ReaderResult readerResult = new ReaderResult();
        readerResult.setSuccess(true);
        readerResult.setResult("FFFFFFFFFFFFFFFF");
        return readerResult;
    }

    @Override
    public ReaderResult mingWen(String cmd) {
        ReaderResult readerResult = new ReaderResult();
        String result = isotag.cmd(cmd);
        if (result.endsWith("9000")) {
            readerResult.setSuccess(true);
            readerResult.setResult(result);
        }
        return readerResult;
    }

    @Override
    public ReaderResult esamMingWen(String cmd) {
        ReaderResult readerResult = new ReaderResult();
        if (cmd.equals(ReaderConst.GET_SN_NUM_CMD)) {
            readerResult.setSuccess(true);
            readerResult.setResult(terminalNo);
            return readerResult;
        }
        String result = isotag.cmd(cmd);
        if (result.endsWith("9000")) {
            readerResult.setSuccess(true);
            readerResult.setResult(result);
        }
        return readerResult;
    }

    @Override
    public ReaderResult miWen(String cmds, boolean noNew) {
        ReaderResult readerResult = new ReaderResult();
        String result = isotag.cmd(cmds);
        if (result.endsWith("9000")) {
            readerResult.setSuccess(true);
            readerResult.setResult(result);
        }
        return readerResult;
    }

    @Override
    public ReaderResult esamMiWen(String cmd) {
        ReaderResult readerResult = new ReaderResult();
        String result = isotag.cmd(cmd);
        if (result.endsWith("9000")) {
            readerResult.setSuccess(true);
            readerResult.setResult(result);
        }
        return readerResult;
    }
}
