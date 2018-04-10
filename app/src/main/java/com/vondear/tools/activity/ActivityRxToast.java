package com.vondear.tools.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.vondear.rxtools.activity.ActivityBase;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.RxToast;
import com.vondear.tools.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityRxToast extends ActivityBase {

    @BindView(R.id.button_error_toast)
    Button mButtonErrorToast;
    @BindView(R.id.button_success_toast)
    Button mButtonSuccessToast;
    @BindView(R.id.button_info_toast)
    Button mButtonInfoToast;
    @BindView(R.id.button_warning_toast)
    Button mButtonWarningToast;
    @BindView(R.id.button_normal_toast_wo_icon)
    Button mButtonNormalToastWoIcon;
    @BindView(R.id.button_normal_toast_w_icon)
    Button mButtonNormalToastWIcon;
    @BindView(R.id.activity_main)
    RelativeLayout mActivityMain;
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_toast);
        ButterKnife.bind(this);
        initView();
    }

    protected void initView() {
        mRxTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.button_error_toast, R.id.button_success_toast, R.id.button_info_toast, R.id.button_warning_toast, R.id.button_normal_toast_wo_icon, R.id.button_normal_toast_w_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_error_toast:
                String errorStr = "java.lang.ClassNotFoundException: android.support.v4.view.TintableBackgroundView\n" +
                        " at org.jetbrains.android.uipreview.ModuleClassLoader.load(ModuleClassLoader.java:181)\n" +
                        " at com.android.tools.idea.rendering.RenderClassLoader.findClass(RenderClassLoader.java:56)\n" +
                        " at org.jetbrains.android.uipreview.ModuleClassLoader.findClass(ModuleClassLoader.java:119)\n" +
                        " at java.lang.ClassLoader.loadClass(ClassLoader.java:424)\n" +
                        " at java.lang.ClassLoader.loadClass(ClassLoader.java:357)\n" +
                        " at org.jetbrains.android.uipreview.ModuleClassLoader.loadClass(ModuleClassLoader.java:214)\n" +
                        " at java.lang.ClassLoader.defineClass1(Native Method)\n" +
                        " at java.lang.ClassLoader.defineClass(ClassLoader.java:763)\n" +
                        " at java.lang.ClassLoader.defineClass(ClassLoader.java:642)\n" +
                        " at com.android.tools.idea.rendering.RenderClassLoader.defineClassAndPackage(RenderClassLoader.java:177)";
//                RxToast.error(mContext, /*"这是一个提示错误的Toast！"*/errorStr, Toast.LENGTH_SHORT, true).show();
                RxToast.error(errorStr);
                break;
            case R.id.button_success_toast:
                String successStr ="流程主题：这是一个提示成功的【审批】，这是一个流程请回复";
                RxToast.success(mContext, successStr, Toast.LENGTH_SHORT, true).show();
                break;
            case R.id.button_info_toast:
                RxToast.info(mContext, "这是一个提示信息的Toast.", Toast.LENGTH_SHORT, true).show();
                break;
            case R.id.button_warning_toast:
                RxToast.warning(mContext, "这是一个提示警告的Toast.", Toast.LENGTH_SHORT, true).show();
                break;
            case R.id.button_normal_toast_wo_icon:
                RxToast.normal(mContext, "这是一个普通的没有ICON的Toast").show();
                break;
            case R.id.button_normal_toast_w_icon:
                Drawable icon = getResources().getDrawable(R.drawable.set);
                RxToast.normal(mContext, "这是一个普通的包含ICON的Toast", icon).show();
                break;
        }
    }
}
