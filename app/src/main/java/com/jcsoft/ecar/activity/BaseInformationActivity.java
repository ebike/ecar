package com.jcsoft.ecar.activity;

import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcsoft.ecar.R;
import com.jcsoft.ecar.base.BaseActivity;
import com.jcsoft.ecar.bean.ResponseBean;
import com.jcsoft.ecar.bean.UserInfoBean;
import com.jcsoft.ecar.callback.DCommonCallback;
import com.jcsoft.ecar.constants.AppConfig;
import com.jcsoft.ecar.http.DHttpUtils;
import com.jcsoft.ecar.http.DRequestParamsUtils;
import com.jcsoft.ecar.http.HttpConstants;
import com.jcsoft.ecar.view.RowLabelValueView;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 基本资料
 */
public class BaseInformationActivity extends BaseActivity implements RowLabelValueView.OnClickCallback {
    @ViewInject(R.id.rlvv_equipment_serial_number)
    RowLabelValueView equipmentSerialNumberRowLabelValueView;
    @ViewInject(R.id.rlvv_imei)
    RowLabelValueView imeiRowLabelValueView;
    @ViewInject(R.id.rlvv_data_card)
    RowLabelValueView dataCardRowLabelValueView;
    @ViewInject(R.id.rlvv_open_date)
    RowLabelValueView openDateRowLabelValueView;
    @ViewInject(R.id.rlvv_name)
    RowLabelValueView nameRowLabelValueView;
    @ViewInject(R.id.rlvv_sex)
    RowLabelValueView sexRowLabelValueView;
    @ViewInject(R.id.rlvv_contact_phone)
    RowLabelValueView contactPhoneRowLabelValueView;
    @ViewInject(R.id.rlvv_work_phone)
    RowLabelValueView workPhoneRowLabelValueView;
    @ViewInject(R.id.rlvv_area)
    RowLabelValueView areaRowLabelValueView;
    @ViewInject(R.id.rlvv_address)
    RowLabelValueView addressRowLabelValueView;
    @ViewInject(R.id.rlvv_belongs_to_unit)
    RowLabelValueView belongsToUnitRowLabelValueView;
    //用户信息
    private UserInfoBean userInfoBean;

    @Override
    public void loadXml() {
        setContentView(R.layout.activity_base_information);
        x.view().inject(this);
    }

    @Override
    public void getIntentData(Bundle savedInstanceState) {

    }

    @Override
    public void init() {
        if (userInfoBean != null) {
            equipmentSerialNumberRowLabelValueView.setValue(userInfoBean.getCarId() + "");
            imeiRowLabelValueView.setValue(userInfoBean.getImei() + "");
            dataCardRowLabelValueView.setValue(userInfoBean.getTelNum());
            openDateRowLabelValueView.setValue(userInfoBean.getActiveDate());
            nameRowLabelValueView.setValue(userInfoBean.getUserName());
            if (userInfoBean.getSex() == 0) {
                sexRowLabelValueView.setValue("男");
            } else if (userInfoBean.getSex() == 1) {
                sexRowLabelValueView.setValue("女");
            }
            contactPhoneRowLabelValueView.setValue(userInfoBean.getPhone());
            workPhoneRowLabelValueView.setValue(userInfoBean.getWorkPhone());
            areaRowLabelValueView.setValue(userInfoBean.getProvince() + "-" + userInfoBean.getCity() + "-" + userInfoBean.getArea());
            addressRowLabelValueView.setValue(userInfoBean.getAddress());
            belongsToUnitRowLabelValueView.setValue(userInfoBean.getOrgName());
        }
    }

    @Override
    public void setListener() {
        nameRowLabelValueView.setOnClickCallback(this);
        sexRowLabelValueView.setOnClickCallback(this);
        contactPhoneRowLabelValueView.setOnClickCallback(this);
        workPhoneRowLabelValueView.setOnClickCallback(this);
        areaRowLabelValueView.setOnClickCallback(this);
        addressRowLabelValueView.setOnClickCallback(this);
    }

    @Override
    public void setData() {
        RequestParams params = DRequestParamsUtils.getRequestParams_Header(HttpConstants.getUserInfo());
        DHttpUtils.get_String(this, true, params, new DCommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ResponseBean<UserInfoBean> bean = new Gson().fromJson(result, new TypeToken<ResponseBean<UserInfoBean>>() {
                }.getType());
                if (bean.getCode() == 1) {
                    userInfoBean = bean.getData();
                    //更新界面
                    init();
                    //更新缓存
                    AppConfig.userInfoBean = bean.getData();
                } else {
                    showShortText(bean.getErrmsg());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rlvv_name:

                break;
            case R.id.rlvv_sex:

                break;
            case R.id.rlvv_contact_phone:

                break;
            case R.id.rlvv_work_phone:

                break;
            case R.id.rlvv_area:

                break;
            case R.id.rlvv_address:

                break;
        }
    }
}
