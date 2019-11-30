package com.example.steven.myapplication.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

/**
 * 来自 伍玉南 的装逼小尾巴 on 17/6/22.
 */

public class StringUtil {

    /**
     * 格式化价格显示多少位小数
     *
     * @param decimal 要显示的位数
     *
     * @return
     */
    public static String formatMoney(int decimal, double money) {
        if (isIntegerForDouble(money)) {
            int a = (int) money;
            return String.valueOf(a);

        } else {
            return String.format(Locale.CHINA, "%." + decimal + "f", money);
        }
    }

    /**
     * 格式化价格显示多少位小数
     *
     * @param decimal 要显示的位数
     *
     * @return
     */
    public static String formatMoney(int decimal, String money) {
        if (TextUtils.isEmpty(money))
            return "";
        try {
            double p = Double.valueOf(money);
            return formatMoney(decimal, p);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    // 保留两位小数
    public static String formatMoney2(double money) {
        return String.format(Locale.CHINA, "%." + 2 + "f", money);
    }

    // 保留两位小数
    public static String formatMoney2(String money) {
        if (TextUtils.isEmpty(money))
            return "";
        try {
            double p = Double.valueOf(money);
            return formatMoney2(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0.00";
    }

    /**
     * 格式化量词 转成万为单位
     *
     * @return
     */
    public static String formatCount(String count) {
        Double dCount = StringUtil.toDouble(count);
        return formatCount(dCount);
    }

    /**
     * 不带.0的万位处理
     */
    public static String formatCountRead(String count) {
        Double dCount = StringUtil.toDouble(count);
        if (dCount < 10000) {
            return count;
        } else {
            // 亿
            if (dCount > 99999999) {
                return (int) (dCount / 100000000) + "亿";

                // 百万
            } else if (dCount > 999999) {
                return (int) (dCount / 10000) + "万";

                // 十万
            } else if (dCount > 99999) {
                return (int) (dCount / 10000) + "万";

            } else if (dCount > 9999) {
                return (int) (dCount / 10000) + "万";

            } else {
                return count + "";
            }
        }
    }

    // 大于一万的都保留两位小数
    public static String formatCountRead2(String count) {
        Double dCount = StringUtil.toDouble(count);
        if (dCount < 10000) {
            return count;
        } else {
            return formatCount(dCount);
        }
    }

    public static String formatCount(double count) {
        Double dCount = count;
        if (dCount > 99999999) { // 亿
            return String.format(Locale.CHINA, "%.2f", dCount / 100000000) + "亿";
        } else if (dCount > 999999) { // 百万
            return String.format(Locale.CHINA, "%.2f", dCount / 10000) + "万";
        } else if (dCount > 99999) {  // 十万
            return String.format(Locale.CHINA, "%.2f", dCount / 10000) + "万";
        } else if (dCount > 9999) {
            return String.format(Locale.CHINA, "%.2f", dCount / 10000) + "万";
        } else {
            return count + "";
        }
    }

    // 特殊地方用到，交易帖子笔数，圈子详情等
    public static String infoCount(String count) {
        if (!TextUtils.isEmpty(count)) {
            Double dCount = StringUtil.toDouble(count);
            if (dCount > 99999999) { // 亿
                return String.format(Locale.CHINA, "%.2f", dCount / 100000000) + "亿";
            } else if (dCount > 999999) { // 百万
                return String.format(Locale.CHINA, "%.0f", dCount / 10000) + "万";
            } else if (dCount > 99999) {  // 十万
                return String.format(Locale.CHINA, "%.1f", dCount / 10000) + "万";
            } else if (dCount > 9999) {
                return String.format(Locale.CHINA, "%.2f", dCount / 10000) + "万";
            }
        } else {
            count = "0";
        }
        return count;
    }

    /**
     * 判断double是否是整数
     *
     * @param obj
     *
     * @return
     */
    public static boolean isIntegerForDouble(double obj) {
        double eps = 1e-10;  // 精度范围
        return obj - Math.floor(obj) < eps;
    }

    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception var3) {
            return defValue;
        }
    }

    public static int toInt(Object obj) {
        return obj == null ? 0 : toInt(obj.toString(), 0);
    }

    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception var2) {
            return 0L;
        }
    }

    public static double toDouble(String obj) {
        try {
            return Double.parseDouble(obj);
        } catch (Exception var2) {
            return 0.0D;
        }
    }

    public static float toFloat(String obj) {
        try {
            return Float.parseFloat(obj);
        } catch (Exception var2) {
            return 0.0f;
        }
    }

    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception var2) {
            return false;
        }
    }


    //将大于10000的转换成 万为单位 例如 1.2万
    public static String Format10000(String num) {
        double n = toDouble(num);
        if (n < 10000) {
            return String.valueOf(n);
        } else {
            return (double) n / 10000 + "";
        }
    }


    /**
     * 计算出该TextView中文字的长度(像素)
     */
    public static int getTextViewLength(TextView textView, String text) {
        TextPaint paint = textView.getPaint();
        // 得到使用该paint写上text的时候,像素为多少
        return (int) paint.measureText(text);
    }

    //改变部分字体颜色  只能改变一段字体
    public static SpannableString changeTextColor(Context context, String content, String targetText, int color) {
        SpannableString ss = new SpannableString(content);
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(targetText)) {
            return ss;
        }

        int mColor;
        try {
            mColor = context.getResources().getColor(color);
        } catch (Exception e) {
            mColor = color;
        }

        int start = content.indexOf(targetText.toLowerCase());
        if (start < 0) {
            return ss;
        }

        ForegroundColorSpan textSpan = new ForegroundColorSpan(mColor);
        ss.setSpan(textSpan, start, start + targetText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    //改变部分字体颜色和大小  只能改变一段字体
    public static SpannableString changeTextColorAndSize(Context context, String content, String targetText, int color, int size) {
        SpannableString ss = new SpannableString(content);
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(targetText)) {
            return ss;
        }

        int mColor;
        try {
            mColor = context.getResources().getColor(color);
        } catch (Exception e) {
            mColor = color;
        }

        int start = content.indexOf(targetText);
        if (start < 0) {
            return ss;
        }
        ss.setSpan(new AbsoluteSizeSpan(size), start, start + targetText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(mColor), start, start + targetText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    public static void MoveCursorToEnd(EditText editText, String content) {
        if (editText != null && !TextUtils.isEmpty(content) && content.length() <= editText.getText().length()) {
            editText.setSelection(content.length());
        }
    }


    //隐藏身份证中间的数字
    public static String hideIdCardString(String target) {
        if (!TextUtils.isEmpty(target) && target.length() >= 18) {
            return target.replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1*****$2");
        }

        return "";
    }

    public static String listToString(List list, String symbol) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    sb.append(list.get(i)).append(symbol);
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }
}
