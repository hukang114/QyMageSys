package com.qymage.sys.common.tools;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtils {
    /**
     * 格式化价格，强制保留2位小数
     *
     * @param price
     * @return
     */
    public static String formatPrice(double price) {
        DecimalFormat df = new DecimalFormat("0.00");
        String format = "￥" + df.format(price);
        return format;
    }

    /**
     * 格式化价格，强制保留2位小数
     *
     * @param price
     * @return
     */
    public static String formatPrice2(double price) {
        DecimalFormat df = new DecimalFormat("0.00");
        String format =  df.format(price);
        return format;
    }


    /**
     * 判断是否是手机号码
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNum(String mobiles) {

        Pattern p = Pattern.compile("^((13[0-9]{1})|(14[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))\\d{8}$");

        Matcher m = p.matcher(mobiles);


        return m.matches();

    }

    /**
     * 身份证
     *
     * @param idCard
     * @return
     */
    public static boolean isIDCard(String idCard) {
        return IdcardUtils.validateCard(idCard);

    }



    /*
   * 将日期转换为时间戳
   */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime()/1000;
        res = String.valueOf(ts);
        return res;
    }


    /*
   * 将日期转换为时间戳
   */
    public static String dateToStamp1(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime()/1000;
        res = String.valueOf(ts);
        return res;
    }

    /*
     * 将日期转换为时间戳
     */
    public static long longStamp(String s) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime()/1000;

        return ts;
    }

    /*
  * 将时间戳转换为时间
  */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
* 将时间戳转换为时间
*/
    public static String stampToDate1(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long lt = new Long(s)*1000;
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

}
