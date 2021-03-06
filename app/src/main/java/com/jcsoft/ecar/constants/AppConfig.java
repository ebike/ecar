package com.jcsoft.ecar.constants;

import android.os.Environment;

import com.jcsoft.ecar.bean.UserInfoBean;
import com.readystatesoftware.viewbadger.BadgeView;

/*
 * 全局的常量存放工具
 */
public class AppConfig {
    //手机IMEI码
    public static String imei;
    //登录名
    public static String loginName;
    public static String LOGIN_NAME = "loginName";
    //登录密码（MD5加密）
    public static String password;
    public static String PASSWORD = "password";
    //调取过所有统计数据的日期
    public static String IS_USED_DATE = "isUsedDate";
    //调取过所有统计数据的用户
    public static String IS_USED_CARID = "isUsedCarId";
    //当前用户信息
    public static UserInfoBean userInfoBean;
    //用户被禁用或删除
    public static boolean isDisabled;
    public static int eventType;
    public static String eventMsg;
    //正在执行开启/关闭电子围栏命令(1/0)
    public static Integer isExecuteVF;
    /**
     * 自定义字段类型
     */
    public static final String SELECT2_FIELD = "select2_field";
    public static final String ADDRESS_SELECT = "address_select";
    public static final String ADDRESS_TEL = "address_tel";
    public static final String FIELD_MAP_FIELD = "field_map_field";
    public static final String MOBILE_FIELD = "mobile_field";
    public static final String TEL_FIELD = "tel_field";
    public static final String CURRENCY_FIELD = "currency_field";//商机的预计销售金额
    public static final String NUMBER_FIELD = "number_field";//数值类型
    public static final String FILE_FIELD = "file_field";
    public static final String CHECK_BOX = "check_box";
    public static final String RADIO_BUTTON = "radio_button";
    public static final String SELECT = "select";
    public static final String TIME_FIELD = "time_field";
    public static final String DATE_FIELD = "date_field";
    public static final String DATETIME_FIELD = "datetime_field";
    public static final String URL_FIELD = "url_field";
    public static final String EMAIL_FIELD = "email_field";
    public static final String TEXT_AREA = "text_area";
    public static final String TEXT_FIELD = "text_field";
    public static final String HIDDEN_FIELD = "hidden_field";
    public static final String ATTACHMENTS_FIELD = "attachments_field";//附件
    public static final String GO_ADDRESS_FIELD = "geo_address_field";
    public static final String EXPECT_SIGN_DATE = "expect_sign_date";

    /**
     * 当前选择的照片位置
     **/
    public static final String CAMERA_PIC_PATH = Environment.getExternalStorageDirectory() + "/ebike/CameraPic/";
    //压缩图片路径
    public static final String compressedImage = Environment.getExternalStorageDirectory() + "/ebike/compressedImage/";
    //底部菜单报警消息图标的角标组件
    public static BadgeView badge;
    //历史位置
    public static final String CenterLngLat = "CenterLngLat";
}
