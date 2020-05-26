package com.anshibo.faxing_lib;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 创建日期：2018/10/26 on 15:26
 * 描述:
 * 作者:王甜
 */
public class QuanCunProgressDialog extends Dialog {
    TextView tv_msg;
    ProgressBar my_progress;
    private boolean isCanNOtCancel;//点击返回键不能取消
    int progress = 0;
    Handler handler = new Handler(Looper.getMainLooper());
    Context context;
    Runnable runnable;

    public QuanCunProgressDialog(Context context) {
        super(context, R.style.FaxingProgressDialog);
        this.context = context;
        setContentView(R.layout.quancun_dialog_progress);
        setCancelable(false);
        tv_msg = (TextView) findViewById(R.id.txt_msg);
        my_progress = findViewById(R.id.my_progress);

        runnable = new Runnable() {
            @Override
            public void run() {
                if (progress < 100) {
                    progress = progress + 5;
                    my_progress.setProgress(progress);
                    handler.postDelayed(this, 100);
                } else {
                    progress = 0;
                    handler.postDelayed(this, 100);
                }
            }
        };
    }


    public void setMsg(String msg) {
        this.tv_msg.setText(msg);
    }

    @Override
    public void show() {
        super.show();
        progress = 0;
        handler.postDelayed(runnable, 100);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        handler.removeCallbacks(runnable);
    }
}
