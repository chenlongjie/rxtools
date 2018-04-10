package com.vondear.rxtools.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vondear.rxtools.R;
import com.vondear.rxtools.RxDataUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.progress.SpinKitView;

/**
 * Created by Administrator on 2017/3/16.
 */

public class RxDialogLoading extends RxDialog implements View.OnClickListener {

    private SpinKitView mLoadingView;
    private View mDialogContentView;
    private TextView mTextView;
    private ImageView btn_cancel;
    private OnCancelClickListener cancelClickListener = null;

    public interface OnCancelClickListener {
        void onItemClick();
    }

    public void setCancelClickListener(OnCancelClickListener listener) {
        this.cancelClickListener = listener;
    }

    public RxDialogLoading(Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    public RxDialogLoading(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    public RxDialogLoading(Context context) {
        super(context);
        initView(context);
    }

    public RxDialogLoading(Activity context) {
        super(context);
        initView(context);
    }

    public RxDialogLoading(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView(context);
    }

    private void initView(Context context) {
        mDialogContentView = LayoutInflater.from(context).inflate(R.layout.dialog_loading_spinkit, null);
        mLoadingView = (SpinKitView) mDialogContentView.findViewById(R.id.spin_kit);
        mTextView = (TextView) mDialogContentView.findViewById(R.id.name);
        btn_cancel = (ImageView) mDialogContentView.findViewById(R.id.btn_cancel);
        setContentView(mDialogContentView);
        btn_cancel.setOnClickListener(this);
    }

    public void setLoadingText(CharSequence charSequence) {
        mTextView.setText(charSequence);
        if (RxDataUtils.isEmpty(charSequence)) {
            mTextView.setVisibility(View.GONE);
        }
    }

    public void setLoadingColor(int color) {
        mLoadingView.setColor(color);
    }

    public void cancel(cancelType code, String str) {
        cancel();
        switch (code) {
            case normal:
                RxToast.normal(str);
                break;
            case error:
                RxToast.error(str);
                break;
            case success:
                RxToast.success(str);
                break;
            case info:
                RxToast.info(str);
                break;
            default:
                RxToast.normal(str);
                break;
        }
    }

    public void cancel(String str) {
        cancel();
        RxToast.normal(str);
    }

    public SpinKitView getLoadingView() {
        return mLoadingView;
    }

    public View getDialogContentView() {
        return mDialogContentView;
    }

    public TextView getTextView() {
        return mTextView;
    }

    @Override
    public void onClick(View v) {
        if (cancelClickListener != null) {
            //注意这里使用getTag方法获取数据
            cancelClickListener.onItemClick();
        }
    }

    private enum cancelType {normal, error, success, info}
}
