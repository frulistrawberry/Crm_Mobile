package com.baihe.common.util;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author：xubo
 * Time：2020-02-28
 * Description：
 */
public class StringUtils {


    public static final String PHONE_BLUR_REGEX = "(\\d{3})\\d{4}(\\d{4})";
    public static final String PHONE_BLUR_REPLACE_REGEX = "$1****$2";

    public static final String blurPhone(String phone) {
        return phone.replaceAll(PHONE_BLUR_REGEX, PHONE_BLUR_REPLACE_REGEX);
    }


    /**
     * 插入字符
     *
     * @param srcStr
     * @param insertStr
     * @param index
     * @return
     */
    public static String insert(String srcStr, String insertStr, int index) {
        if (!TextUtils.isEmpty(srcStr)) {
            if (srcStr.length() > index) {
                String prefix = srcStr.substring(0, index);
                String suffix = srcStr.substring(index);
                return prefix + insertStr + suffix;
            } else {
                return srcStr + insertStr;
            }
        }
        return insertStr;
    }

    /**
     * 插入字符
     *
     * @param srcStr
     * @param interval
     * @param randomLength
     * @return
     */
    public static String insert(String srcStr, int interval, int randomLength) {
        if (!TextUtils.isEmpty(srcStr)) {
            StringBuffer buffer = new StringBuffer();
            int srcLength = srcStr.length();
            for (int i = 0; i < srcLength; i++) {
                if (i % interval == 0) {
                    buffer.append(nextRandom(randomLength));
                }
                buffer.append(srcStr.charAt(i));
            }
            buffer.append(nextRandom(randomLength));
            return buffer.toString();
        }
        return "";
    }

    /**
     * 生成随机字符串
     *
     * @param length
     * @return
     */
    public static String nextRandom(int length) {
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        String charStr = "0123456789abcdefghijklmnopqrstuvwxyz";
        int charLength = charStr.length();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charLength);
            buffer.append(charStr.charAt(index));
        }
        return buffer.toString();
    }

    //分割字符串为字符串数组
    public static String[] convertToArray(String str, String splitChar) {
        String[] resultArray = null;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        resultArray = str.split(splitChar);
        StringBuilder builder = new StringBuilder();
        for (String s : resultArray) {
            builder.append(s + " ");
        }
        return resultArray;
    }

    //分割字符串为字符串数组(兼容中英文字符)
    public static String[] convertToArray(String str, String englishChar, String chineseChar) {
        String[] resultArray = null;
        String splitChar = null;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        str.trim();
        if (str.contains(chineseChar)) {
            str = str.replace(chineseChar, englishChar);
        }
        resultArray = str.split(englishChar);
        if (resultArray != null && resultArray.length != 0) {
            for (int i = 0; i < resultArray.length; i++) {
                resultArray[i] = resultArray[i].trim();
            }
        }
        return resultArray;
    }


    //截取字符串生成字符串数组
    public static String[] cutStrToArray(String str, String fromStr) {
        String[] resultArray = null;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        int startIndex = str.indexOf(fromStr);
        String front = str.substring(0, startIndex);
        String behind = str.substring(startIndex + 1, str.length());
        resultArray = new String[]{front, behind};
        return resultArray;
    }

    // 判断一个字符串是否含有数字
    public static boolean hasDigit(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        if (m.matches()) {
            flag = true;
        }
        return flag;
    }

    /**
     * 检测String是否全是中文
     *
     * @param name
     */
    public static boolean checkNameChese(String name) {
        boolean res = true;
        char[] cTemp = name.toCharArray();

        for (int i = 0; i < name.length(); i++) {
            if (!isChinese(cTemp[i] + "")) {
                res = false;
                break;
            }
        }
        return res;
    }


    /**
     * 判断该字符串是否为中文
     *
     * @param string
     * @return
     */
    public static boolean isChinese(String string) {
        int n = 0;
        for (int i = 0; i < string.length(); i++) {
            n = (int) string.charAt(i);
            if (!(19968 <= n && n < 40869)) {
                return false;
            }
        }
        return true;
    }


    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

//    /* 判定输入的是否是汉字
//     *
//     * @param c 被校验的字符
//     * @return true代表是汉字
//     */
//    public static boolean isChinese(char c) {
//        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
//        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
//                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
//                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
//                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
//                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
//                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
//            return true;
//        }
//        return false;
//    }

    /**
     * 判断是否为空
     *
     * @param text
     * @return
     */
    public static boolean isNullOrEmpty(String text) {
        if (text == null || "".equals(text.trim()) || text.trim().length() == 0
                || "null".equals(text.trim())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证输入的身份证号是否符合格式要求
     *
     * @param IDNum 身份证号
     * @return 符合国家的格式要求为 true;otherwise,false;
     */
    public static boolean validateIDcard(String IDNum) {
        String id_regEx1 = "^([1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|(3[0-1]))\\d{3})|([1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|(3[0-1]))\\d{3}[0-9Xx])$";
        Pattern pattern = Pattern.compile(id_regEx1);
        Matcher m = pattern.matcher(IDNum);
        return m.matches();
    }


    public static Map<String, String> readParamsFromUrl(String url) {

//        Uri uri = Uri.parse(url);
//        String type = uri.getQueryParameter("key");
        Map<String, String> params = new HashMap<>();
        if (url.contains("?")) {
            String str = url.substring(url.indexOf("?") + 1, url.length());
            if (!TextUtils.isEmpty(str)) {
                String[] params1 = str.split("&");
                if (params1 != null && params1.length > 0) {
                    for (String s : params1) {
                        String[] params2 = s.split("=");
                        if (params2 != null && params2.length == 2) {
                            try {
                                String value = URLDecoder.decode(params2[1], "utf-8");
                                params.put(params2[0], value);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        return params;
    }

    public static Map<String, String> readParamsFromUrlDecode(String url) {

//        Uri uri = Uri.parse(url);
//        String type = uri.getQueryParameter("key");

        Map<String, String> params = new HashMap<>();
        if (url.contains("?")) {
            String str = url.substring(url.indexOf("?") + 1, url.length());
            if (!TextUtils.isEmpty(str)) {
                String[] params1 = str.split("&");
                if (params1 != null && params1.length > 0) {
                    for (String s : params1) {
                        String[] params2 = s.split("=");
                        if (params2 != null && params2.length == 2) {
                            try {
                                params.put(params2[0], URLDecoder.decode(params2[1], "UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        return params;
    }


    /**
     * 是否是邮箱
     *
     * @param str
     * @return
     */
    public static boolean isEmail(String str) {
        String regex = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        return match(regex, str);
    }

    /**
     * 保险公司要求邮箱以".com"或".cn"结尾
     */
    public static boolean isEndWithEmail(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        if (str.endsWith(".com") || str.endsWith(".cn")) {
            return true;
        }
        return false;
    }

    /**
     * 时是否是手机号
     *
     * @param str
     * @return
     */
    public static boolean IsTelephone(String str) {
        String regex = "^(\\d{3,4}-)?\\d{6,8}$";
        return match(regex, str);
    }

    /**
     * @param phone 字符串类型的手机号
     * 传入手机号,判断后返回
     * true为手机号,false相反
     * */
    public static boolean isPhone(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            return m.matches();
        }
    }

    /**
     * 时是否是手机号
     *
     * @param str
     * @return
     */
    public static boolean isPhoneNumber(String str) {
        if (!TextUtils.isEmpty(str) && TextUtils.isDigitsOnly(str) & str.startsWith("1") && str.length() == 11) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证输入身份证号
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     * @str 待验证的字符串
     */
    public static boolean IsCardID(String str) {
        String id_regEx1 = "^([1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|(3[0-1]))\\d{3})|([1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|(3[0-1]))\\d{3}[0-9Xx])$";
        return match(id_regEx1, str);
    }

    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static String formatDouble1(double d) {
        return String.format("%.1f", d);
    }

    public static String cutString(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        } else if (str.length() <= 8) {
            return str;
        } else {
            String str1 = str.substring(0, str.length() / 2);
            String str2 = str.substring(str.length() / 2, str.length());
            return str1 + "\n" + str2;
        }

    }

    /**
     * 格式化double 数值,为显示缓存大小用
     * 如：21.7  ->21.7  21.0  -> 21
     *
     * @param size
     * @return
     */
    public static String formatDouble(double size) {
        String temp = String.valueOf(size);
        if ((size > 0 || size == 0.0) && temp.endsWith("0")) {
            return temp.substring(0, temp.indexOf("."));
        }
        return temp;
    }

    /**
     * byte 转换成Mb 保留一位小数
     *
     * @param b
     * @return
     */
    public static double byte2Mb(long b) {
        return Math.round(b * 10 / 1024 / 1024) / 10.0;
    }

    /**
     * byte 转换成Mb 保留两位小数
     *
     * @param d
     * @return
     */
    public static double left2Count(double d) {
        BigDecimal b = new BigDecimal(d);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static Double getDouble(double num) {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(num));
    }


    /**
     * 获取String中数字
     *
     * @param strInput
     * @return
     */
    public static String getNumberInStr(String strInput) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(strInput);
        return m.replaceAll("").trim();
    }

    /**
     * 保留小数位数
     *
     * @param format
     * @param pattern
     * @return
     */
    public static String formatToString(double format, String pattern) {
        if (Math.round(format) - format == 0) {
            return String.valueOf((long) format);
        }
        return new DecimalFormat(pattern).format(format);
    }

    public static String getFormatCommonDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = sdf.parse(dateStr);//字符串转成date对象类型
            DateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            String str2 = sdf2.format(date);//date类型转换成字符串
            return str2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 返回标准的日期格式
     * @param dateStr
     * @return
     */
    public static String getFormatDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = sdf.parse(dateStr);//字符串转成date对象类型
            DateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String str2 = sdf2.format(date);//date类型转换成字符串
            return str2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 返回不带年份的 日期格式  格式：10-24 20:30
     * @param dateStr
     * @return
     */
    public static String getFormatTime(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = sdf.parse(dateStr);//字符串转成date对象类型
            DateFormat sdf2 = new SimpleDateFormat("MM-dd HH:mm");
            String str2 = sdf2.format(date);//date类型转换成字符串
            return str2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 返回格式化的日期格式  格式：2019年10月24日
     * @param dateStr
     * @return
     */
    public static String getFormatDateStr(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = sdf.parse(dateStr);//字符串转成date对象类型
            DateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
            String str2 = sdf2.format(date);//date类型转换成字符串
            return str2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 两个日期比较大小
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static int compareDate(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }


    /**
     * 将日期以字符串格式返回  格式：2019-10-24 20:30
     * @param calendar
     * @return
     */
    public static String calendarToString(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            // 设置你想要的格式
            String dateStr = df.format(calendar.getTime());
            return dateStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    /**
     * 返回当前的时间  格式：2019-10-24
     * @param calendar
     * @return
     */
    public static String getTodayDate(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            // 设置你想要的格式
            String dateStr = df.format(calendar.getTime());
            return dateStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }


    /**
     * 返回今天的时间 以零点开始 格式：2019-10-24 00:00
     * @return
     */
    public static String todayStartTime() {

        Calendar calendar = Calendar.getInstance();

        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            calendar.set(Calendar.HOUR_OF_DAY, 00);
            calendar.set(Calendar.MINUTE, 00);
            // 设置你想要的格式
            String dateStr = df.format(calendar.getTime());
            return dateStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    public static String formatPrice(String price) {
        try {
            DecimalFormat decimalFormat = new DecimalFormat("###,###");
            return decimalFormat.format(Integer.parseInt(price));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 去掉手机号内除数字外的所有字符 返回标准的手机号 例如：13716093201
     *
     * @param phoneNum 手机号
     * @return
     **/
    public static String formatPhoneNum(String phoneNum) {
        String regex = "(\\+86)|[^0-9]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNum);
        return matcher.replaceAll("");
    }


    /**
     * 给手机号增加空格  例如：137 1609 3201
     *
     * @param phoneStr
     * @return
     */
    public static String phoneNumberAddSpaceFormat(String phoneStr) {
        int len = phoneStr.length();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            builder.append(phoneStr.charAt(i));
            if (i == 2 || i == 6) {
                if (i != len - 1) builder.append(" ");
            }
        }
        return builder.toString();
    }

    /**
     * 给手机号增加横杠 例如137-1609-3201
     *
     * @param phoneStr
     * @return
     */
    public static String phoneNumberAddDividerFormat(String phoneStr) {
        StringBuilder builder = new StringBuilder();
        if(!TextUtils.isEmpty(phoneStr)) {
            int len = phoneStr.length();
            for (int i = 0; i < len; i++) {
                builder.append(phoneStr.charAt(i));
                if (i == 2 || i == 6) {
                    if (i != len - 1) builder.append("-");
                }
            }
        }
        return builder.toString();
    }

    /**
     * 返回浮点型数据字符串
     * @param num
     * @return
     */
    public static  String getFloadStr(float num){
        NumberFormat nf = NumberFormat.getInstance();
        String result = nf.format(num);
        return  result;
    }

    public static boolean isUrl(String text) {
        try {
            return TextUtils.equals(text.toLowerCase(Locale.getDefault()).substring(0, 7),"http://")
                    || TextUtils.equals(text.toLowerCase(Locale.getDefault()).substring(0, 8),"https://");
        }catch (Exception e){}
        return false;
    }

    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    public static String formatPublicTime(@Nullable String dateStr){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(dateStr);//字符串转成date对象类型
            long diff = new Date().getTime() - date.getTime();
            long r = 0;
            if (diff > day) {
                return dateStr;
            }
            if (diff > hour) {
                r = (diff / hour);
                return r + "小时前";
            }
            if (diff > minute) {
                r = (diff / minute);
                return r + "分钟前";
            }
            return dateStr;
        }catch (Exception e){
            e.printStackTrace();
            return dateStr;
        }
    }
}
