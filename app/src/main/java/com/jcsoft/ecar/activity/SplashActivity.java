package com.jcsoft.ecar.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcsoft.ecar.R;
import com.jcsoft.ecar.base.BaseActivity;
import com.jcsoft.ecar.bean.ResponseBean;
import com.jcsoft.ecar.bean.UserInfoBean;
import com.jcsoft.ecar.callback.DCommonCallback;
import com.jcsoft.ecar.callback.DDoubleDialogCallback;
import com.jcsoft.ecar.constants.AppConfig;
import com.jcsoft.ecar.http.DHttpUtils;
import com.jcsoft.ecar.http.HttpConstants;
import com.jcsoft.ecar.utils.CommonUtils;
import com.jcsoft.ecar.view.CustomDialog;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;

public class SplashActivity extends BaseActivity {
    @ViewInject(R.id.tv_version)
    TextView versionTV;
    private final static long MILLIS = 2000;// 启动画面延迟时间
    private boolean isReciverRegiest;
    //弹出框
    private CustomDialog dialog;
    //是否需要启动
    private boolean isStartUp;

    @Override
    public void loadXml() {
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void getIntentData(Bundle savedInstanceState) {

    }

    @Override
    public void init() {
        versionTV.setText("V" + CommonUtils.getVersionName(this));
        isStartUp = true;
        try {
            if (!CommonUtils.isNetworkConnected(SplashActivity.this)) {
                //添加网络变化监听
                initNoNetReciver();
                if (dialog == null) {
                    dialog = CommonUtils.showNetCustomDialog(SplashActivity.this, "温馨提示", "当前网络不可用，请检查你的网络设置", "设置网络", new DDoubleDialogCallback() {

                        @Override
                        public void onNegativeButtonClick(String editText) {
                            SplashActivity.this.finish();
                            System.exit(0);
                        }

                        @Override
                        public void onPositiveButtonClick(String editText) {
                            //在Android版本10以下，调用的是：ACTION_WIRELESS_SETTINGS，版本在10以上的调用：ACTION_SETTINGS。
                            if (android.os.Build.VERSION.SDK_INT > 10) {
                                // 3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
                                startActivity(new Intent(Settings.ACTION_SETTINGS));
                            } else {
                                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                            }
                        }
                    });
                } else if (dialog != null && !dialog.isShowing()) {
                    dialog.show();
                }
                return;
            } else {
                if (isStartUp) {
                    isStartUp = false;
                    //获取share中的账号、密码
                    final String loginName = preferencesUtil.getPrefString(SplashActivity.this, AppConfig.LOGIN_NAME, "");
                    final String password = preferencesUtil.getPrefString(SplashActivity.this, AppConfig.PASSWORD, "");
                    if (!CommonUtils.strIsEmpty(loginName) && !CommonUtils.strIsEmpty(password)) {
                        RequestParams params = new RequestParams(HttpConstants.getLoginUrl(loginName, password));
                        DHttpUtils.get_String(SplashActivity.this, false, params, new DCommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                ResponseBean<UserInfoBean> responseBean = new Gson().fromJson(result, new TypeToken<ResponseBean<UserInfoBean>>() {
                                }.getType());
                                if (responseBean.getCode() == 1) {
                                    //保存数据信息
                                    AppConfig.userInfoBean = responseBean.getData();
                                    preferencesUtil.setPrefString(SplashActivity.this, AppConfig.LOGIN_NAME, loginName);
                                    preferencesUtil.setPrefString(SplashActivity.this, AppConfig.PASSWORD, password);
                                    //注册极光推送别名
                                    setAlias();
                                    //进入首页
                                    goToActivity(MainActivity.class);
                                } else {
                                    goToActivity(LoginActivity.class);
                                }
                            }
                        });
                    } else {
                        goToActivity(LoginActivity.class);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToActivity(final Class clazz) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, clazz);
                startActivity(intent);
                finish();
            }
        }, MILLIS);
    }

    //添加网络变化广播监听
    private void initNoNetReciver() {
        IntentFilter connectFilter = new IntentFilter();
        connectFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mLoadingConnectReciver, connectFilter);
        isReciverRegiest = true;
    }

    //网络变化广播处理
    private BroadcastReceiver mLoadingConnectReciver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            init();
        }
    };

    @Override
    public void setListener() {

    }

    @Override
    public void setData() {

    }
}
