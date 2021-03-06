package com.jcsoft.ecar.utils;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.jcsoft.ecar.R;
import com.jcsoft.ecar.callback.DDoubleDialogCallback;
import com.jcsoft.ecar.callback.DSingleDialogCallback;
import com.jcsoft.ecar.view.CustomDialog;

import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公共方法的处理类
 * Created by dive on 2015/07/11.
 */
public class CommonUtils {
    // 默认日期转换格式
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String DATE_YEAR_MONTH = "MM/dd";
    public static final String DATE_YEAR_MONTH_CH = "MM月dd日";
    public static final String DATE_YEAR_MONTH_DAY_CH = "yyyy年MM月dd日";
    private static PopupWindow popupWindow;

    /**
     * 字符串为空的判断
     */
    public static Boolean strIsEmpty(String str) {
        if (str == null || str.equals("null") || str.length() <= 0) {
            return true;
        }
        return false;
    }

    /**
     * 进行MD5加密
     *
     * @param s
     * @return
     */
    public static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    //转化时间格式
    public static String DateToString(Date date, String format) {
        if (format == null || format.length() == 0) {
            format = DEFAULT_DATE_FORMAT;
        }
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    // 取得当前日期
    public static String getCurrentDateString(String format) {
        if (format == null || format.length() == 0) {
            format = DEFAULT_DATE_FORMAT;
        }
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(d);
    }

    //取得昨天日期
    public static String getYesterdayDateString(String format) {
        //先获取今天时间
        Date today = new Date();
        //得到当前时刻距离1970.1.1的毫秒数
        long t1 = today.getTime();
        //计算昨天时间：将当前时间毫秒数减去一天的毫秒数
        Date yesterday = new Date();
        long t2 = t1 - 24 * 60 * 60 * 1000;
        yesterday.setTime(t2);
        return DateToString(yesterday, format);
    }

    //改变日期格式:yyyy-MM-dd  -->  MM/dd
    public static String changeDateFormat1(String time) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        Date date = df.parse(time);
        return DateToString(date, DATE_YEAR_MONTH);
    }

    //改变日期格式:yyyy-MM-dd  -->  MM月dd日
    public static String changeDateFormat2(String time) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        Date date = df.parse(time);
        return DateToString(date, DATE_YEAR_MONTH_CH);
    }

    //改变日期格式:yyyy-MM-dd  -->  yyyy年MM月dd日
    public static String changeDateFormat3(String time) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        Date date = df.parse(time);
        return DateToString(date, DATE_YEAR_MONTH_DAY_CH);
    }

    //时间差大于一周
    public static boolean moreThanAWeek(String start, String end) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT);
            Date startDate = df.parse(start);
            Date endDate = df.parse(end);
            long t1 = startDate.getTime();
            long t2 = endDate.getTime();
            long timeDifference = t2 - t1;
            long week = 7 * 24 * 60 * 60 * 1000;
            if (timeDifference > week) {
                return true;
            }
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    //七天后日期
    public static String AfterAWeekDate(String date) {
        Date oldDate = new Date(date);
        long t1 = oldDate.getTime();
        long week = 7 * 24 * 60 * 60 * 1000;
        long t2 = t1 + week;
        Date aWeekDate = new Date();
        aWeekDate.setTime(t2);
        return DateToString(aWeekDate, DEFAULT_DATE_FORMAT);
    }

    /**
     * Description 友好显示时间
     *
     * @param time 要格式化的时间戳
     * @return String txt 格式化后的时间戳
     * @author ernest
     */
    public static String friendlyShowTime(long time) {
        long nowTime = new Date().getTime();
        String txt = "";
        long t = nowTime - time; //时间差（毫秒）
        if (t <= 0) {
            txt = "刚刚";
        } else if (t < 60 * 1000) {
            txt = t / 1000 + "秒前"; // 一分钟内
        } else if (t < 60 * 60 * 1000) {
            String s = subZeroAndDot(String.valueOf(Math.floor(t / (60 * 1000))));
            txt = s + "分钟前"; //一小时内
        } else if (t < 60 * 60 * 24 * 1000) {
            String s = subZeroAndDot(String.valueOf(Math.floor(t / (60 * 60 * 1000))));
            txt = s + "小时前"; // 一天内
        } else if (t < 60 * 60 * 24 * 3 * 1000) {
            String date = formatDateToStr("HH:mm", new Date(time));
            txt = Math.floor(time / (60 * 60 * 24 * 1000)) == 1 ? "昨天" + date : "前天 " + date; //昨天和前天
        } else if (t < 60 * 60 * 24 * 30 * 1000) {
            txt = formatDateToStr("MM月dd日 HH:mm", new Date(time)); //一个月内
        } else if (t < 60 * 60 * 24 * 365 * 1000) {
            txt = formatDateToStr("MM月dd日", new Date(time)); //一年内
        } else {
            txt = formatDateToStr("yyyy年MM月dd日", new Date(time));//一年以前
        }
        return txt;
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    //将日期转化为字符串
    public static String formatDateToStr(String format, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * @param mail 邮件
     * @return
     */
    public static Boolean checkEmail(String mail) {
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(mail);
        boolean isMatched = matcher.matches();
        return isMatched;
    }

    /**
     * 检查是否有“;;”
     *
     * @param str
     * @return
     */
    public static String checkString(String str) {
        String s = str.replace(";;", ";");
        if (s.contains(";;")) {
            return checkString(s);
        } else {
            return s;
        }
    }

    /**
     * 验证手机号码
     *
     * @param mobiles
     * @return [0-9]{5,9}
     */
    public static boolean isMobileNO(String mobiles, Boolean isphone) {
        boolean flag = false;
        if (isphone) {
            if (!TextUtils.isEmpty(mobiles)) {
                flag = true;
            } else {
                flag = false;
            }
        } else {
            if (!TextUtils.isEmpty(mobiles) && mobiles.length() > 6) {
                flag = true;
            } else {
                flag = false;
            }
        }
        return flag;
    }

    public static boolean isHanzi(String str) {
        if (str.getBytes().length == str.length()) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isNumSec(String number) {
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("[0-9]*");
            Matcher m = p.matcher(number);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 消息通知栏  定义NotificationManager
     *
     * @param context
     * @param clazz
     * @param title
     * @param content
     * @param bundle
     * @param num
     */
    public static void NotificationManager(Context context, Class clazz, String title, String content, Bundle bundle, int num) {
        String ns = context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);

        int noticeType = Notification.DEFAULT_SOUND;//默认是铃声

        NotificationCompat.Builder mBuilder;
        mBuilder = new NotificationCompat.Builder(context)   //Notification 的兼容类
                .setSmallIcon(R.mipmap.ic_launcher)   //若没有设置largeicon，此为左边的大icon，设置了largeicon，则为右下角的小icon，无论怎样，都影响Notifications area显示的图标
                .setContentTitle(title) //标题
                .setContentText(content)         //正文
                .setNumber(1)                       //设置信息条数
                        //.setContentInfo("3")        //作用同上，设置信息的条数
                        // .setLargeIcon(smallicon)           //largeicon，

                .setDefaults(noticeType)//设置声音，此为默认声音
                        //.setVibrate(vT) //设置震动，此震动数组为：long vT[]={300,100,300,100}; 还可以设置灯光.setLights(argb, onMs, offMs)
                .setOngoing(false)      //true使notification变为ongoing，用户不能手动清除，类似QQ,false或者不设置则为普通的通知
                .setAutoCancel(true);//点击之后自动消失
        Intent resultIntent = new Intent(context, clazz);
        resultIntent.putExtras(bundle);
        resultIntent.putExtra("isNotification", "1");
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        //stackBuilder.addParentStack(context);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        //把刚才的pending添加进去
        mBuilder.setContentIntent(resultPendingIntent);
        /*//resultPendingIntent
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder.build()//会返回一个Notifications对象，1000为Notifications的id，可变动。就可以notify出来了。*/
        mNotificationManager.notify(num, mBuilder.build());
    }

    /**
     * 千分位处理 3为添加一个，
     */
    public static String formateBlance(Number blance) {
        String resutl = null;
        try {
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.CHINA);
            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            decimalFormat.setMaximumFractionDigits(2);
            decimalFormat.setMinimumFractionDigits(2);
            String regx = "###,###";
            decimalFormat.applyPattern(regx);

            resutl = decimalFormat.format(blance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resutl;
    }

    /**
     * 拦截短信后截取验证码
     *
     * @param msg
     * @return
     */
    public static String getSmsCodeFromMsg(String msg) {
        StringBuffer codeBuffer = new StringBuffer("");
        if (!TextUtils.isEmpty(msg)) {
            for (int i = 0; i < msg.length(); i++) {
                if (msg.charAt(i) >= 48 && msg.charAt(i) <= 57) {
                    codeBuffer.append(msg.charAt(i));
                }
            }
        }

        String codeString = codeBuffer.toString();
        if (!TextUtils.isEmpty(codeString) && codeString.length() > 6) {
            codeString = codeString.substring(0, 6);
        }
        return codeString;
    }

    /**
     * IOS风格
     * 固定按钮文字的多选对话框
     *
     * @param ctx
     * @param title
     * @param mesage
     */
    public static CustomDialog showNetCustomDialog(Context ctx, String title, String mesage, String positiveText, final DDoubleDialogCallback dialogCallback) {
        CustomDialog.Builder builder = new CustomDialog.Builder(ctx);
        builder.setTitle(title);
        builder.setMessage(mesage);
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (dialogCallback != null) {
                    dialogCallback.onPositiveButtonClick("");
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (dialogCallback != null) {
                    dialogCallback.onNegativeButtonClick("");
                }
            }
        });
        CustomDialog customDialog = builder.create();
        // 设置点击屏幕Dialog不消失
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.show();
        return customDialog;
    }

    /**
     * IOS风格
     * 固定按钮文字的多选对话框
     *
     * @param ctx
     * @param title
     * @param mesage
     * @param callback
     */
    public static void showCustomDialog0(Context ctx, String title, String mesage, final DSingleDialogCallback callback) {
        CustomDialog.Builder builder = new CustomDialog.Builder(ctx);
        builder.setTitle(title);
        builder.setMessage(mesage);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (callback != null) {
                    callback.onPositiveButtonClick("");
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    public static void showCustomDialog1(Context ctx, String title, View view, final DSingleDialogCallback callback) {
        CustomDialog.Builder builder = new CustomDialog.Builder(ctx);
        builder.setTitle(title);
        builder.setContentView(view);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (callback != null) {
                    callback.onPositiveButtonClick("");
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    /**
     * IOS风格
     * 固定按钮文字的单选对话框
     *
     * @param ctx
     * @param title
     * @param mesage
     * @param clickInterface
     */
    public static void showCustomDialogSignle2(Context ctx, String title, String mesage, final DSingleDialogCallback clickInterface) {
        CustomDialog.Builder builder = new CustomDialog.Builder(ctx);
        builder.setTitle(title);
        builder.setMessage(mesage);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialog.cancel();
                if (clickInterface != null) {
                    clickInterface.onPositiveButtonClick("");
                }
            }
        });
        CustomDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void showCustomDialogSignle(Context ctx, String title, String mesage, int msgGravity, final DSingleDialogCallback clickInterface) {
        CustomDialog.Builder builder = new CustomDialog.Builder(ctx);
        builder.setTitle(title);
        builder.setMessage(mesage);
        builder.setMsgGravity(msgGravity);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialog.cancel();
                if (clickInterface != null) {
                    clickInterface.onPositiveButtonClick("");
                }
            }
        });
        CustomDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * IOS风格
     * 固定按钮文字的单选对话框
     *
     * @param ctx
     * @param title
     * @param mesage
     */
    public static void showCustomDialogSignle3(Context ctx, String title, String mesage) {
        CustomDialog.Builder builder = new CustomDialog.Builder(ctx);
        builder.setTitle(title);
        builder.setMessage(mesage);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialog.cancel();
            }
        });
        CustomDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * IOS风格
     * 不固定按钮文字的多选对话框
     *
     * @param ctx
     * @param title
     * @param mesage
     * @param clickInterface
     */
    public static void showCustomDialog3(Context ctx, String positiveText, String negativeText, String title, String mesage, final DSingleDialogCallback clickInterface) {
        CustomDialog.Builder builder = new CustomDialog.Builder(ctx);
        builder.setTitle(title);
        builder.setMessage(mesage);
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (clickInterface != null) {
                    clickInterface.onPositiveButtonClick("");
                }
            }
        });
        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    /**
     * IOS风格
     * 含输入框固定按钮文字的多选对话框
     *
     * @param ctx
     * @param title
     * @param mesage
     * @param clickInterface
     */
    public static void showCustomDialog6(Context ctx, String title, String mesage, String hint, final DSingleDialogCallback clickInterface) {
        final CustomDialog.Builder builder = new CustomDialog.Builder(ctx);
        builder.setTitle(title);
        builder.setMessage(mesage);
        builder.setHasEditText(true);
        builder.setHintText(hint);
        builder.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (clickInterface != null) {
                    clickInterface.onPositiveButtonClick(builder.getEditText());
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    public static boolean isMobileNetworkType(Context ctx) {
        ConnectivityManager connectMgr = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = connectMgr.getActiveNetworkInfo();
        if (mNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    /**
     * 隐藏键盘
     *
     * @param ctx
     * @param view
     */
    public static void hideSoftInput(Context ctx, View view) {
        if (ctx == null || view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 打开键盘
     *
     * @param ctx
     * @param view
     */
    public static void showSoftInput(Context ctx, View view) {
        if (ctx == null || view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 打开、关闭键盘
     *
     * @param ctx
     * @param view
     */
    public static void toggleSoftInput(Context ctx, View view) {
        if (ctx == null || view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

    }

    /**
     * 要判断App是否在前台运行
     *
     * @param context
     * @return
     */
    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName())) {
            return true;
        }
        return false;
    }

    /**
     * 处理手机号码 保留前三位后三位
     *
     * @param phone
     * @return
     */
    public static String formatPhone(String phone) {
        if (phone != null && phone.length() >= 11) {
            return phone.substring(0, 3) + "******" + phone.substring(9, 11);
        } else {
            return phone;
        }
    }

    /**
     * 检查网络
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    //月份小于10，补零
    public static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    //判断APP是否在后台运行
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                Log.i(context.getPackageName(), "此appimportace ="
                        + appProcess.importance
                        + ",context.getClass().getName()="
                        + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台"
                            + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "处于前台"
                            + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 短文本提示
     */
    public static void showShortText(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 窗口显示在控件下方
     *
     * @param popupView
     * @return
     */
    public static PopupWindow createPopupWindow(View popupView) {
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        return popupWindow;
    }

    /**
     * 窗口显示在控件上方
     *
     * @param popupView
     * @return
     */
    public static PopupWindow createAbovePopupWindow(View popupView) {
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.AbovePopupWindowAnimation);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        return popupWindow;
    }

    /**
     * 给组件设置margin
     *
     * @param v
     * @param l
     * @param t
     * @param r
     * @param b
     */
    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    /**
     * 将textview中的字符全角化。
     * 即将所有的数字、字母及标点全部转为全角字符，使它们与汉字同占两个字节，
     * 这样就可以避免由于占位导致的排版混乱问题了。
     * 半角转为全角的代码如下，只需调用即可。
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i< c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }if (c[i]> 65280&& c[i]< 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 获取包名称
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
        return versionName;
    }
}
