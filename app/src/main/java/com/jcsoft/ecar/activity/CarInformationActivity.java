package com.jcsoft.ecar.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcsoft.ecar.R;
import com.jcsoft.ecar.base.BaseActivity;
import com.jcsoft.ecar.bean.CarInfoBean;
import com.jcsoft.ecar.bean.ImageItem;
import com.jcsoft.ecar.bean.ResponseBean;
import com.jcsoft.ecar.bean.SendParamsBean;
import com.jcsoft.ecar.callback.DCommonCallback;
import com.jcsoft.ecar.constants.AppConfig;
import com.jcsoft.ecar.event.SelectPhotoEvent;
import com.jcsoft.ecar.http.DHttpUtils;
import com.jcsoft.ecar.http.DRequestParamsUtils;
import com.jcsoft.ecar.http.HttpConstants;
import com.jcsoft.ecar.utils.CommonUtils;
import com.jcsoft.ecar.utils.ImageCompress;
import com.jcsoft.ecar.view.ActionSheetDialog;
import com.jcsoft.ecar.view.RowLabelValueView;

import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 车辆资料
 */
public class CarInformationActivity extends BaseActivity implements RowLabelValueView.OnClickCallback {
    @ViewInject(R.id.rlvv_car_number)
    RowLabelValueView carNumberRowLabelValueView;
    @ViewInject(R.id.rlvv_brand)
    RowLabelValueView brandRowLabelValueView;
    @ViewInject(R.id.rlvv_models)
    RowLabelValueView modelsRowLabelValueView;
    @ViewInject(R.id.rlvv_motor_number)
    RowLabelValueView motorNumberRowLabelValueView;
    @ViewInject(R.id.rlvv_frame_number)
    RowLabelValueView frameNumberRowLabelValueView;
    @ViewInject(R.id.rlvv_car_color)
    RowLabelValueView carColorRowLabelValueView;
    @ViewInject(R.id.rlvv_buy_date)
    RowLabelValueView buyDateRowLabelValueView;
    @ViewInject(R.id.rlvv_buy_price)
    RowLabelValueView buyPriceRowLabelValueView;
    @ViewInject(R.id.rlvv_car_photo)
    RowLabelValueView carPhotoRowLabelValueView;
    @ViewInject(R.id.iv_car_photo)
    ImageView carPhotoImageView;
    //车辆信息
    private CarInfoBean carInfoBean;
    //拍照时间
    private long takePhotoTime;
    //图片压缩类
    private ImageCompress compress;

    @Override
    public void loadXml() {
        setContentView(R.layout.activity_car_information);
    }

    @Override
    public void getIntentData(Bundle savedInstanceState) {

    }

    @Override
    public void init() {
        compress = new ImageCompress();
        if (carInfoBean != null) {
            carNumberRowLabelValueView.setValue(carInfoBean.getCarPlate());
            brandRowLabelValueView.setValue(carInfoBean.getCarBrand());
            modelsRowLabelValueView.setValue(carInfoBean.getCarModel());
            motorNumberRowLabelValueView.setValue(carInfoBean.getMotorNum());
            frameNumberRowLabelValueView.setValue(carInfoBean.getFrameNum());
            carColorRowLabelValueView.setValue(carInfoBean.getCarColor());
            try {
                if (!CommonUtils.strIsEmpty(carInfoBean.getCarDate())) {
                    buyDateRowLabelValueView.setValue(CommonUtils.changeDateFormat3(carInfoBean.getCarDate()));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            buyPriceRowLabelValueView.setValue("￥" + carInfoBean.getCarPrice());
            carPhotoRowLabelValueView.setRightImage(R.mipmap.icon_camera);
            ImageOptions imageOptions = new ImageOptions.Builder()
                    .setLoadingDrawableId(R.mipmap.img_default_car)
                    .setFailureDrawableId(R.mipmap.img_default_car)
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setUseMemCache(false)
                    .build();
            x.image().bind(carPhotoImageView, carInfoBean.getCarPic(), imageOptions);
        }
    }

    @Override
    public void setListener() {
        brandRowLabelValueView.setOnClickCallback(this);
        modelsRowLabelValueView.setOnClickCallback(this);
        motorNumberRowLabelValueView.setOnClickCallback(this);
        frameNumberRowLabelValueView.setOnClickCallback(this);
        carColorRowLabelValueView.setOnClickCallback(this);
        buyDateRowLabelValueView.setOnClickCallback(this);
        buyPriceRowLabelValueView.setOnClickCallback(this);
        carPhotoRowLabelValueView.setOnClickCallback(this);
    }

    @Override
    public void setData() {
        //获取车辆基本信息
        RequestParams params = DRequestParamsUtils.getRequestParams_Header(HttpConstants.getCarInfoUrl());
        DHttpUtils.get_String(this, true, params, new DCommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ResponseBean<CarInfoBean> bean = new Gson().fromJson(result, new TypeToken<ResponseBean<CarInfoBean>>() {
                }.getType());
                if (bean.getCode() == 1) {
                    carInfoBean = bean.getData();
                    //更新界面
                    init();
                } else {
                    showShortText(bean.getErrmsg());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.rlvv_brand:
                intent = new Intent(this, UpdateTextValueActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("fieldName_CH", getString(R.string.brand));
                intent.putExtra("fieldValue", carInfoBean.getCarBrand());
                intent.putExtra("fieldName", "carBrand");
                startActivity(intent);
                break;
            case R.id.rlvv_models:
                intent = new Intent(this, UpdateTextValueActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("fieldName_CH", getString(R.string.models));
                intent.putExtra("fieldValue", carInfoBean.getCarModel());
                intent.putExtra("fieldName", "carModel");
                startActivity(intent);
                break;
            case R.id.rlvv_motor_number:
                intent = new Intent(this, UpdateTextValueActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("fieldName_CH", getString(R.string.motor_number));
                intent.putExtra("fieldValue", carInfoBean.getMotorNum());
                intent.putExtra("fieldName", "motorNum");
                startActivity(intent);
                break;
            case R.id.rlvv_frame_number:
                intent = new Intent(this, UpdateTextValueActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("fieldName_CH", getString(R.string.frame_number));
                intent.putExtra("fieldValue", carInfoBean.getFrameNum());
                intent.putExtra("fieldName", "frameNum");
                startActivity(intent);
                break;
            case R.id.rlvv_car_color:
                intent = new Intent(this, UpdateTextValueActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("fieldName_CH", getString(R.string.car_color));
                intent.putExtra("fieldValue", carInfoBean.getCarColor());
                intent.putExtra("fieldName", "carColor");
                startActivity(intent);
                break;
            case R.id.rlvv_buy_date:
                try {
                    showDataCardelar();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rlvv_buy_price:
                intent = new Intent(this, UpdateTextValueActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("fieldName_CH", getString(R.string.buy_price));
                intent.putExtra("fieldValue", carInfoBean.getCarPrice() + "");
                intent.putExtra("fieldName", "carPrice");
                startActivity(intent);
                break;
            case R.id.rlvv_car_photo:
                takePhotoTime = System.currentTimeMillis();
                showPhotoDialog(CarInformationActivity.this, takePhotoTime);
                break;
        }
    }

    public static void showPhotoDialog(final Activity activity, final long takePhotoTime) {
        new ActionSheetDialog(activity)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                File file = new File(AppConfig.CAMERA_PIC_PATH);
                                if (!file.exists()) {
                                    file.mkdirs();
                                }
                                File file2 = new File(AppConfig.CAMERA_PIC_PATH, takePhotoTime + ".jpg");
                                try {
                                    file2.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file2));
                                activity.startActivityForResult(intent, 2);
                            }
                        })
                .addSheetItem("从手机相册选择", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                Intent intent = new Intent(activity, PhotoAlbumListActivity.class);
                                activity.startActivity(intent);
                            }
                        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //判断请求码
        switch (requestCode) {
            case 2://拍照
                //设置文件保存路径这里放在跟目录下
                File mFile = new File(AppConfig.CAMERA_PIC_PATH + takePhotoTime + ".jpg");
                if (mFile.length() != 0) {
                    ImageItem item = new ImageItem();
                    item.imageId = takePhotoTime + "";
                    item.picName = takePhotoTime + ".jpg";
                    item.size = String.valueOf(mFile.length());
                    item.sourcePath = AppConfig.CAMERA_PIC_PATH + takePhotoTime + ".jpg";
                    uploadImage(item);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //根据当前操作的照片进行赋值
    private void uploadImage(final ImageItem imageItem) {
        //由于目前没有查看图片，每次选择图片都是覆盖更新，所以，只用到路径字段，其他字段预留
        if (imageItem != null && !CommonUtils.strIsEmpty(imageItem.sourcePath)) {
            //对图片做压缩处理
            Bitmap bitmap = compress.getimage(imageItem.sourcePath);
            if (null != bitmap) {
                try {
                    compress.compressAndGenImage(bitmap, imageItem.sourcePath, AppConfig.compressedImage + imageItem.picName, 100);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //压缩后的图片文件
            File file = new File(AppConfig.compressedImage + imageItem.picName);
            List<SendParamsBean> sendParamsBeans = new ArrayList<SendParamsBean>();
            sendParamsBeans.add(new SendParamsBean("carId", AppConfig.userInfoBean.getCarId() + "", false));
            sendParamsBeans.add(new SendParamsBean("carPic", file, true));
            RequestParams params = DRequestParamsUtils.getRequestParamsHasFile_Header(HttpConstants.getUpdateCarUrl(), sendParamsBeans);
            DHttpUtils.post_String(this, true, params, new DCommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    ResponseBean<CarInfoBean> bean = new Gson().fromJson(result, new TypeToken<ResponseBean<CarInfoBean>>() {
                    }.getType());
                    if (bean.getCode() == 1) {
                        carInfoBean = bean.getData();
                        init();
                    } else {
                        showShortText(bean.getErrmsg());
                    }
                }
            });
        }
    }

    //从相册选择
    public void onEvent(SelectPhotoEvent event) {
        if (event != null && event.getItem() != null) {
            uploadImage(event.getItem());
        }
    }

    public void onEvent(CarInfoBean car) {
        if (car != null) {
            carInfoBean = car;
            init();
        }
    }

    private int mYear;
    private int mMonth;
    private int mDay;
    private String mDate;
    private Date mDatetime;
    private Date nowDate;

    private void showDataCardelar() throws ParseException {
        String buyDate = carInfoBean.getCarDate();
        SimpleDateFormat sdft1 = new SimpleDateFormat("yyyy-MM-dd");
        if (!CommonUtils.strIsEmpty(buyDate) && !buyDate.equals("必选")) {
            String[] sub = buyDate.split("-");
            mYear = Integer.parseInt(sub[0]);
            mMonth = Integer.parseInt(sub[1]) - 1;
            mDay = Integer.parseInt(sub[2]);
            mDate = buyDate;
        } else {
            Calendar calendar = Calendar.getInstance();
            Calendar mCalendar = new GregorianCalendar();
            nowDate = calendar.getTime();
            if (mDatetime == null) {
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
            } else {
                mCalendar.setTime(mDatetime);
                mYear = mCalendar.get(Calendar.YEAR);
                mMonth = mCalendar.get(Calendar.MONTH);
                mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
            }
            mDate = sdft1.format(nowDate);
        }
        View mTargetView = LayoutInflater.from(this).inflate(R.layout.view_picker_date_time, null);
        DatePicker mDatePicker = (DatePicker) mTargetView.findViewById(R.id.datePicker);
        TimePicker mTimePicker = (TimePicker) mTargetView.findViewById(R.id.timePicker);
        mTimePicker.setVisibility(View.GONE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择购买日期");
        builder.setView(mTargetView);
        mDatePicker.init(mYear, mMonth, mDay, datePickerChangeListener);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                buyDateRowLabelValueView.setValue(mDate);
            }
        });
        builder.show();
    }

    private DatePicker.OnDateChangedListener datePickerChangeListener = new DatePicker.OnDateChangedListener() {
        @Override
        public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
            mDate = new StringBuilder().append(year).append("年").append(CommonUtils.pad(month + 1)).append("月").append(CommonUtils.pad(day)).append("日").toString();
        }
    };
}
