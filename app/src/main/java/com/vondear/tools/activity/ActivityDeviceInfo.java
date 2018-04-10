package com.vondear.tools.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.connect.share.QQShare;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.vondear.rxtools.RxAppUtils;
import com.vondear.rxtools.RxVibrateUtils;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.vondear.tools.R;
import com.vondear.rxtools.RxDeviceUtils;
import com.vondear.rxtools.activity.ActivityBase;
import com.vondear.rxtools.view.RxTitle;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;

public class ActivityDeviceInfo extends ActivityBase {

    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.btn_get_phone_info)
    Button mBtnGetPhoneInfo;
    @BindView(R.id.tv_device_is_phone)
    TextView mTvDeviceIsPhone;
    @BindView(R.id.tv_device_software_phone_type)
    TextView mTvDeviceSoftwarePhoneType;
    @BindView(R.id.tv_device_density)
    TextView mTvDeviceDensity;
    @BindView(R.id.tv_device_manu_facturer)
    TextView mTvDeviceManuFacturer;
    @BindView(R.id.tv_device_width)
    TextView mTvDeviceWidth;
    @BindView(R.id.tv_device_height)
    TextView mTvDeviceHeight;
    @BindView(R.id.tv_device_version_name)
    TextView mTvDeviceVersionName;
    @BindView(R.id.tv_device_version_code)
    TextView mTvDeviceVersionCode;
    @BindView(R.id.tv_device_imei)
    TextView mTvDeviceImei;
    @BindView(R.id.tv_device_imsi)
    TextView mTvDeviceImsi;
    @BindView(R.id.tv_device_software_version)
    TextView mTvDeviceSoftwareVersion;
    @BindView(R.id.tv_device_mac)
    TextView mTvDeviceMac;
    @BindView(R.id.tv_device_software_mcc_mnc)
    TextView mTvDeviceSoftwareMccMnc;
    @BindView(R.id.tv_device_software_mcc_mnc_name)
    TextView mTvDeviceSoftwareMccMncName;
    @BindView(R.id.tv_device_software_sim_country_iso)
    TextView mTvDeviceSoftwareSimCountryIso;
    @BindView(R.id.tv_device_sim_operator)
    TextView mTvDeviceSimOperator;
    @BindView(R.id.tv_device_sim_serial_number)
    TextView mTvDeviceSimSerialNumber;
    @BindView(R.id.tv_device_sim_state)
    TextView mTvDeviceSimState;
    @BindView(R.id.tv_device_sim_operator_name)
    TextView mTvDeviceSimOperatorName;
    @BindView(R.id.tv_device_subscriber_id)
    TextView mTvDeviceSubscriberId;
    @BindView(R.id.tv_device_voice_mail_number)
    TextView mTvDeviceVoiceMailNumber;
    @BindView(R.id.tv_device_adnroid_id)
    TextView mTvDeviceAdnroidId;
    @BindView(R.id.tv_device_build_brand_model)
    TextView mTvDeviceBuildBrandModel;
    @BindView(R.id.tv_device_build_manu_facturer)
    TextView mTvDeviceBuildManuFacturer;
    @BindView(R.id.tv_device_build_brand)
    TextView mTvDeviceBuildBrand;
    @BindView(R.id.tv_device_serial_number)
    TextView mTvDeviceSerialNumber;
    @BindView(R.id.tv_device_iso)
    TextView mTvDeviceIso;
    @BindView(R.id.tv_device_phone)
    TextView mTvDevicePhone;
    @BindView(R.id.ll_info_root)
    LinearLayout mLlInfoRoot;

    private int shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT;
    private Subscriber<String> subscriber;
    private Observable<String> observable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        ButterKnife.bind(this);
        mRxTitle.setLeftFinish(mContext);
        initEvent();
        createSubscriber();
    }
    private void initEvent() {
        mRxTitle.setRightIcon(R.drawable.ic_menu);
        mRxTitle.setRightIconVisibility(true);
        mRxTitle.setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenshot();
            }
        });
    }
    private void createSubscriber() {

        subscriber = new Subscriber<String>() {

            @Override
            public void onCompleted() {
                RxVibrateUtils.vibrateOnce(ActivityDeviceInfo.this,200);
            }

            @Override
            public void onError(Throwable e) {
                RxToast.error(e.toString());
                e.printStackTrace();
            }

            @Override
            public void onNext(String file) {
                showDialog(file);
            }
        };
    }

    private void screenshot() {
        // 获取屏幕
        View dView = this.getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bmp = dView.getDrawingCache();
        if (bmp != null)
        {
            try {
                // 获取内置SD卡路径
                String sdCardPath = Environment.getExternalStorageDirectory().getPath();
                // 图片文件路径
                String filePath = sdCardPath + File.separator + "sixia.png";
                File file = new File(filePath);
                FileOutputStream os = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
                createObservable(file.getAbsolutePath());
            } catch (Exception e) {
            }
        }

    }
    private void bindSubscriber() {
        observable.subscribe(subscriber);
    }
    private void createObservable(final String file) {
        observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(file);
                subscriber.onCompleted();
            }
        });
        bindSubscriber();
    }
    private void doShareToQQ(final Bundle params) {
        // QQ分享要在主线程做
        ThreadManager.getMainHandler().post(new Runnable() {

            @Override
            public void run() {
                if (null != ActivityMain.mTencent) {
                    ActivityMain.mTencent.shareToQQ(ActivityDeviceInfo.this, params, qqShareListener);
                }
            }
        });
    }

    private void showDialog(final String path){
        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(mContext);//提示弹窗
        rxDialogSureCancel.setTitle(path);
        rxDialogSureCancel.setContent("要用你滴绳命去玩耍嘛！");
        rxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxDialogSureCancel.cancel();
                Bundle params = new Bundle();
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,path);
                params.putString(QQShare.SHARE_TO_QQ_APP_NAME, RxAppUtils.getAppName(ActivityDeviceInfo.this));
                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, 5);
                params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, 0x00);
                params.putString(QQShare.SHARE_TO_QQ_ARK_INFO, "{app:com.tencent.music,view:Share,meta:{Share:{musicId:4893051}}}");
                doShareToQQ(params);

            }
        });
        rxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxDialogSureCancel.cancel();
            }
        });
        rxDialogSureCancel.show();
    }
    private void getPhoneInfo() {
        if (RxDeviceUtils.isPhone(mContext)) {
            mTvDeviceIsPhone.setText("是");
        } else {
            mTvDeviceIsPhone.setText("否");
        }

        mTvDeviceSoftwarePhoneType.setText(RxDeviceUtils.getPhoneType(mContext) + "");
        mTvDeviceDensity.setText(RxDeviceUtils.getScreenDensity(mContext) + "");
        mTvDeviceManuFacturer.setText(RxDeviceUtils.getUniqueSerialNumber() + "");
        mTvDeviceWidth.setText(RxDeviceUtils.getScreenWidth(mContext) + " ");
        mTvDeviceHeight.setText(RxDeviceUtils.getScreenHeight(mContext) + " ");
        mTvDeviceVersionName.setText(RxDeviceUtils.getAppVersionName(mContext) + "");
        mTvDeviceVersionCode.setText(RxDeviceUtils.getAppVersionNo(mContext) + "");
        mTvDeviceImei.setText(RxDeviceUtils.getDeviceIdIMEI(mContext) + "");
        mTvDeviceImsi.setText(RxDeviceUtils.getIMSI(mContext) + "");
        mTvDeviceSoftwareVersion.setText(RxDeviceUtils.getDeviceSoftwareVersion(mContext) + "");
        mTvDeviceMac.setText(RxDeviceUtils.getMacAddress(mContext));
        mTvDeviceSoftwareMccMnc.setText(RxDeviceUtils.getNetworkOperator(mContext) + "");
        mTvDeviceSoftwareMccMncName.setText(RxDeviceUtils.getNetworkOperatorName(mContext) + "");
        mTvDeviceSoftwareSimCountryIso.setText(RxDeviceUtils.getNetworkCountryIso(mContext) + "");
        mTvDeviceSimOperator.setText(RxDeviceUtils.getSimOperator(mContext) + "");
        mTvDeviceSimSerialNumber.setText(RxDeviceUtils.getSimSerialNumber(mContext) + "");
        mTvDeviceSimState.setText(RxDeviceUtils.getSimState(mContext) + "");
        mTvDeviceSimOperatorName.setText(RxDeviceUtils.getSimOperatorName(mContext) + "");
        mTvDeviceSubscriberId.setText(RxDeviceUtils.getSubscriberId(mContext) + "");
        mTvDeviceVoiceMailNumber.setText(RxDeviceUtils.getVoiceMailNumber(mContext) + "");
        mTvDeviceAdnroidId.setText(RxDeviceUtils.getAndroidId(mContext) + "");
        mTvDeviceBuildBrandModel.setText(RxDeviceUtils.getBuildBrandModel() + "");
        mTvDeviceBuildManuFacturer.setText(RxDeviceUtils.getBuildMANUFACTURER() + "");
        mTvDeviceBuildBrand.setText(RxDeviceUtils.getBuildBrand() + "");
        mTvDeviceSerialNumber.setText(RxDeviceUtils.getSerialNumber() + "");
        mTvDeviceIso.setText(RxDeviceUtils.getNetworkCountryIso(mContext) + "");
        mTvDevicePhone.setText(RxDeviceUtils.getLine1Number(mContext) + "");
    }

    @OnClick(R.id.btn_get_phone_info)
    public void onViewClicked() {
        mLlInfoRoot.setVisibility(View.VISIBLE);
        getPhoneInfo();
        mBtnGetPhoneInfo.setVisibility(View.GONE);
    }

    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {
            if (shareType != QQShare.SHARE_TO_QQ_TYPE_IMAGE) {
                RxToast.normal(ActivityDeviceInfo.this,"onCancel: ");
            }
        }
        @Override
        public void onComplete(Object response) {
            // TODO Auto-generated method stub
            RxToast.normal(ActivityDeviceInfo.this, "onComplete: " + response.toString());
        }
        @Override
        public void onError(UiError e) {
            // TODO Auto-generated method stub
            RxToast.error(ActivityDeviceInfo.this, "onError: " + e.errorMessage);
        }
    };

}
