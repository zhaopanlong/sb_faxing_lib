package com.anshibo.faxing_lib;

import android.content.Context;
import android.widget.Toast;

/**
 * @author zhaopanlong
 * @createtime：2020/5/7 上午10:17
 */
public class ReaderErrorHelper {

    public static void handleReaderException(Context context, ReadException3 readException3, FaxingDialog.CancleSureListen cancleSureListen) {
        FaxingErrorCode errCode = readException3.getErrCode();
        switch (errCode) {
            case QC_READ_QC_ERR:
            case QC_READ_SE_ERR:
            case QC_READ_FILE_ERR:
            case QC_PIN_VERIFY_ERR:
            case QC_QCINIT_BEFORE_ERR:
            case QC_READ_ACCOUNT_BALANCE_ERR:
                showErrorDialog(context, errCode.getErrorDes(), cancleSureListen);
                break;
            default:
                Toast.makeText(context,errCode.getErrorDes(),Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private static void showErrorDialog(Context context, String content, FaxingDialog.CancleSureListen cancleSureListen) {
        FaxingDialog errorDialog = new FaxingDialog(context);
        errorDialog.setTv_contect(content);
        errorDialog.setCancleSureListen(cancleSureListen);
        errorDialog.show();
    }
}
