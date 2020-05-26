package com.anshibo.faxing_lib;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by 赵盼龙&谷彩云
 * 创建于 2016/7/20.
 * 描述:::
 */
public class FaxingDialog implements View.OnClickListener {

    private Context mContext;
    private Dialog dialog;

    private TextView tv_cancle, tv_sure, tv_title, tv_contect, tv_contect2;
    private View view_dangban;

    public FaxingDialog(Context context) {
        mContext = context;
        dialog = new Dialog(context, R.style.FaxingErrorDialog);
        dialog.setContentView(R.layout.error_dialog);
        tv_cancle = (TextView) dialog.findViewById(R.id.tv_cancle);
        tv_sure = (TextView) dialog.findViewById(R.id.tv_sure);
        tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        tv_contect = (TextView) dialog.findViewById(R.id.tv_contect);
        tv_contect2 = (TextView) dialog.findViewById(R.id.tv_contect2);
        view_dangban = dialog.findViewById(R.id.view_dangban);
        dialog.setCanceledOnTouchOutside(false);
        tv_sure.setOnClickListener(this);
        tv_cancle.setOnClickListener(this);
        tv_contect2.setOnClickListener(this);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (onBack != null) {
                        onBack.onBack();
                    }
                }
                return false;
            }
        });
    }

    public void show() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_cancle) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (this.cancleSureListen != null) {
                this.cancleSureListen.cancle();
            }

        } else if (i == R.id.tv_sure) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (this.cancleSureListen != null) {
                this.cancleSureListen.sure();
            }

        } else if (i == R.id.tv_contect2) {
            if (this.clickTextListen != null) {
                this.clickTextListen.clickText();
            }

        }
    }

    private MyDialogOnBack onBack;

    public void setOnBack(MyDialogOnBack onBack) {
        this.onBack = onBack;
    }

    public interface MyDialogOnBack {
        void onBack();
    }

    private CancleSureListen cancleSureListen;
    private ClickTextListen clickTextListen;

    public void setCancleSureListen(CancleSureListen cancleSureListen) {
        this.cancleSureListen = cancleSureListen;
    }

    public void setCancelable(boolean b) {
        dialog.setCancelable(false);
    }

    public interface CancleSureListen {
        void cancle();

        void sure();
    }

    public void setClickTextListen(ClickTextListen clickTextListen) {
        this.clickTextListen = clickTextListen;
    }

    public interface ClickTextListen {
        void clickText();
    }

    public void setTv_cancle(String tv_cancle) {
        this.tv_cancle.setText(tv_cancle);
    }

    public void setCancleColor(String color) {
        this.tv_cancle.setTextColor(Color.parseColor(color));
    }

    public void setTv_sure(String tv_sure) {
        this.tv_sure.setText(tv_sure);
    }

    public void setSureColor(String color) {
        this.tv_sure.setTextColor(Color.parseColor(color));
    }

    public void setTv_title(String tv_title) {
        this.tv_title.setText(tv_title);
    }

    public void setTv_title_color(String color) {
        this.tv_title.setTextColor(Color.parseColor(color));
    }

    public void setTv_titleGone() {
        this.tv_title.setVisibility(View.GONE);
    }

    public void setTv_contect(String tv_contect) {
        this.tv_contect.setText(tv_contect);
    }


}
