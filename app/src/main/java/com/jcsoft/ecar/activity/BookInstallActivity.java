package com.jcsoft.ecar.activity;

import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcsoft.ecar.R;
import com.jcsoft.ecar.base.BaseActivity;
import com.jcsoft.ecar.bean.CarLikeImeiBean;
import com.jcsoft.ecar.bean.LocationJson;
import com.jcsoft.ecar.bean.ResponseBean;
import com.jcsoft.ecar.callback.DCommonCallback;
import com.jcsoft.ecar.callback.DSingleDialogCallback;
import com.jcsoft.ecar.db.ProvinceInfoDao;
import com.jcsoft.ecar.http.DHttpUtils;
import com.jcsoft.ecar.http.DRequestParamsUtils;
import com.jcsoft.ecar.http.HttpConstants;
import com.jcsoft.ecar.utils.CommonUtils;
import com.jcsoft.ecar.view.RowLabelEditView;
import com.jcsoft.ecar.view.RowLabelValueView;
import com.jcsoft.ecar.view.TopBarView;
import com.jcsoft.ecar.view.wheel.AddressThreeWheelViewDialog;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预约安装
 */
public class BookInstallActivity extends BaseActivity {
    @ViewInject(R.id.top_bar)
    TopBarView topBarView;
    @ViewInject(R.id.rlev_name)
    RowLabelEditView nameRowLabelEditView;
    @ViewInject(R.id.rlev_phone)
    RowLabelEditView phoneRowLabelEditView;
    @ViewInject(R.id.rlvv_region)
    RowLabelValueView areaRowLabelValueView;
    @ViewInject(R.id.rlev_address)
    RowLabelEditView addressRowLabelEditView;
    @ViewInject(R.id.rlev_install_count)
    RowLabelEditView installCountRowLabelEditView;
    private AddressThreeWheelViewDialog dialog;
    private ProvinceInfoDao provinceDao;
    private List<LocationJson> mProvinceList;
    private int provinceId;
    private int cityId;
    private int districtId;
    private String provinceName;
    private String cityName;
    private String districtName;

    @Override
    public void loadXml() {
        setContentView(R.layout.activity_book_install);
        x.view().inject(this);
    }

    @Override
    public void getIntentData(Bundle savedInstanceState) {

    }

    @Override
    public void init() {
        phoneRowLabelEditView.setEditInteger();
        installCountRowLabelEditView.setEditInteger();
        installCountRowLabelEditView.setEditLength(8);
        dialog = new AddressThreeWheelViewDialog(this);
        provinceDao = new ProvinceInfoDao(this);
        mProvinceList = provinceDao.queryAll();
    }

    @Override
    public void setListener() {
        //选择省市县
        areaRowLabelValueView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setData(mProvinceList);
                dialog.show(new AddressThreeWheelViewDialog.ConfirmAction() {
                    @Override
                    public void doAction(LocationJson root, LocationJson child, LocationJson child2) {
                        areaRowLabelValueView.setValue(root.getName() + " " + child.getName() + " " + child2.getName());
                        areaRowLabelValueView.setValueColor(R.color.gray_6);
                        provinceId = root.getId();
                        provinceName = root.getName();
                        cityId = child.getId();
                        cityName = child.getName();
                        districtId = child2.getId();
                        districtName = child2.getName();
                    }
                });
            }
        });

        //提交
        topBarView.setRightCallback(new TopBarView.TopBarRightCallback() {
            @Override
            public void setRightOnClickListener() {
                //验证必填
                if (CommonUtils.strIsEmpty(nameRowLabelEditView.getValue())) {
                    nameRowLabelEditView.setHint(R.string.app_require_input);
                    nameRowLabelEditView.setHintColor(R.color.orange_dark);
                    return;
                }
                if (CommonUtils.strIsEmpty(phoneRowLabelEditView.getValue())) {
                    phoneRowLabelEditView.setHint(R.string.app_require_input);
                    phoneRowLabelEditView.setHintColor(R.color.orange_dark);
                    return;
                }
                if (provinceId <= 0 && cityId <= 0 && districtId <= 0) {
                    areaRowLabelValueView.setValueColor(R.color.orange_dark);
                    return;
                }
                if (CommonUtils.strIsEmpty(addressRowLabelEditView.getValue())) {
                    addressRowLabelEditView.setHint(R.string.app_require_input);
                    addressRowLabelEditView.setHintColor(R.color.orange_dark);
                    return;
                }
                if (CommonUtils.strIsEmpty(installCountRowLabelEditView.getValue())) {
                    installCountRowLabelEditView.setHint(R.string.app_require_input);
                    installCountRowLabelEditView.setHintColor(R.color.orange_dark);
                    return;
                }
                //整理参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", nameRowLabelEditView.getValue());
                map.put("phone", phoneRowLabelEditView.getValue());
                map.put("province", provinceName);
                map.put("city", cityName);
                map.put("area", districtName);
                map.put("address", addressRowLabelEditView.getValue());
                map.put("num", installCountRowLabelEditView.getValue());
                //提交
                RequestParams params = DRequestParamsUtils.getRequestParams(HttpConstants.saveOnlineBookUrl(), map);
                DHttpUtils.post_String(BookInstallActivity.this, true, params, new DCommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        ResponseBean<Object> bean = new Gson().fromJson(result, new TypeToken<ResponseBean<CarLikeImeiBean>>() {
                        }.getType());
                        if (bean.getCode() == 1) {
                            CommonUtils.showCustomDialogSignle2(BookInstallActivity.this, "", "" + bean.getErrmsg(), new DSingleDialogCallback() {
                                @Override
                                public void onPositiveButtonClick(String editText) {
                                    BookInstallActivity.this.finish();
                                }
                            });
                        } else {
                            showShortText(bean.getErrmsg());
                        }
                    }
                });
            }
        });
    }

    @Override
    public void setData() {

    }
}
