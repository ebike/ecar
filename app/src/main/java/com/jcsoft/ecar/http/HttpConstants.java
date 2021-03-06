package com.jcsoft.ecar.http;

import com.jcsoft.ecar.constants.AppConfig;

/**
 * Created by jimmy on 15/12/28.
 */
public class HttpConstants {

    //接口前缀
    public static String baseUrl = "http://api.car.gnets.cn/app";

    //登录
    public static String getLoginUrl(String loginName, String password) {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/checkLogin.do");
        sb.append("?loginName=").append(loginName);
        sb.append("&password=").append(password);
        sb.append("&clientId=").append(AppConfig.imei);
        sb.append("&system=android:").append(android.os.Build.VERSION.RELEASE);
        return sb.toString();
    }

    //获取用户基本信息
    public static String getUserInfo() {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/car/getUserInfo.do");
        sb.append("?carId=").append(AppConfig.userInfoBean.getCarId());
        return sb.toString();
    }

    //返回车辆基本信息
    public static String getCarInfoUrl() {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/car/getCarInfo.do");
        sb.append("?carId=").append(AppConfig.userInfoBean.getCarId());
        return sb.toString();
    }

    //用户注册
    public static String getRegDeviceUrl() {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/car/regDevice.do");
        return sb.toString();
    }

    //根据用户输入IMEI后八位自动补全IMEI
    public static String getSearchCarLikeImei(String imei) {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/car/searchCarLikeImei.do");
        sb.append("?imei=").append(imei);
        return sb.toString();
    }

    //验证数据卡号
    public static String getCheckTelNumUrl(String telNum) {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/car/checkTelNum.do");
        sb.append("?telNum=").append(telNum);
        return sb.toString();
    }

    //获取每日统计数据
    public static String getDayDataUrl(String date) {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/chart/getDayData.do");
        sb.append("?carId=").append(AppConfig.userInfoBean.getCarId());
        sb.append("&date=").append(date);
        return sb.toString();
    }

    //获取指定天数的统计数据
    public static String getSomeDayDataUrl() {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/chart/getSomeDayData.do");
        sb.append("?carId=").append(AppConfig.userInfoBean.getCarId());
        sb.append("&dayNum=").append(15);
        return sb.toString();
    }

    //开启电子围栏
    public static String getOpenVfUrl(double lon, double lat, double maxLon, double maxLat, double minLon, double minLat) {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/vf/openVf.do");
        sb.append("?carId=").append(AppConfig.userInfoBean.getCarId());
        sb.append("&maxLon=").append(maxLon);
        sb.append("&maxLat=").append(maxLat);
        sb.append("&minLon=").append(minLon);
        sb.append("&minLat=").append(minLat);
        sb.append("&lon=").append(lon);
        sb.append("&lat=").append(lat);
        return sb.toString();
    }

    //关闭电子围栏
    public static String getCloseVfUrl() {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/vf/closeVf.do");
        sb.append("?carId=").append(AppConfig.userInfoBean.getCarId());
        return sb.toString();
    }

    //查询当前车辆位置信息
    public static String getLocInfoUrl() {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/map/getLocInfo.do");
        if (AppConfig.userInfoBean != null) {
            sb.append("?carId=").append(AppConfig.userInfoBean.getCarId());
        }
        return sb.toString();
    }

    //根据起止时间查询轨迹信息
    public static String getTrackInfoUrl(String startTime, String endTime) {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/map/searchTrack.do");
        if (AppConfig.userInfoBean != null) {
            sb.append("?carId=").append(AppConfig.userInfoBean.getCarId());
        }
        sb.append("&startTime=").append(startTime);
        sb.append("&endTime=").append(endTime);
        return sb.toString().replace(" ", "%20");
    }

    //查询报警消息
    public static String getNewAlarmEventInfo(int mark, int eventId) {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/alarm/getNewAlarmEventInfo.do");
        sb.append("?carId=").append(AppConfig.userInfoBean.getCarId());
        sb.append("&mark=").append(mark);
        sb.append("&eventId=").append(eventId);
        return sb.toString();
    }

    //查看报警消息
    public static String viewAlarmEvent(int eventId) {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/alarm/viewAlarmEvent.do");
        sb.append("?eventId=").append(eventId);
        sb.append("&carId=").append(AppConfig.userInfoBean.getCarId());
        return sb.toString();
    }

    //在线预订
    public static String saveOnlineBookUrl() {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/book/saveOnlineBook.do");
        return sb.toString();
    }

    //退出账号
    public static String getLogoutUrl() {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/logout.do");
        sb.append("?carId=").append(AppConfig.userInfoBean.getCarId());
        return sb.toString();
    }

    //修改基本资料
    public static String getUpdateUserUrl() {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/car/updateUser.do");
        return sb.toString();
    }

    //修改车辆资料
    public static String getUpdateCarUrl() {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/car/updateCar.do");
        return sb.toString();
    }

}
