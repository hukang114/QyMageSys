package com.qymage.sys.common.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import android.widget.EditText;


import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 判断是否未空工具类
 */
public class VerifyUtils {

    public static final String[] IDCARD = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "x", "X",};
    /**
     * 正则表达式:验证密码(不包含特殊字符)
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,18}$";

    private VerifyUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断对象是否为空
     *
     * @param obj 对象
     * @return {@code true}: 为空<br>{@code false}: 不为空
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String && obj.toString().length() == 0) {
            return true;
        }
        if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
            return true;
        }
        if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof Map && ((Map) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof SparseArray && ((SparseArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseBooleanArray && ((SparseBooleanArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseIntArray && ((SparseIntArray) obj).size() == 0) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (obj instanceof SparseLongArray && ((SparseLongArray) obj).size() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断对象是否非空
     *
     * @param obj 对象
     * @return {@code true}: 非空<br>{@code false}: 空
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 比较真实完整的判断身份证号码的工具
     *
     * @param IdCard 用户输入的身份证号码
     * @return [true符合规范, false不符合规范]
     */
    public static boolean isRealIDCard(String IdCard) {
//        if (IdCard != null) {
//            int correct = new IdCardUtil(IdCard).isCorrect();
//            if (0 == correct) {// 符合规范
//                return true;
//            }
//        }
        return false;
    }

    /**
     * 身份证号输入规则
     *
     * @return
     */
    @NonNull
    public static InputFilter idCardEntryRules(final EditText editText) {
        // 居民身份证号的组成元素
        // 在这里设置才有用！[对EditText可以输入的字符进行了限制][使用Android原生的键盘会停留在数字键盘无切换到英文键盘][第三方的键盘正常使用]
        // et_idcard.setInputType(InputType.TYPE_CLASS_TEXT);
        // et_idcard.setKeyListener(DigitsKeyListener.getInstance("1234567890Xx"));
        final List<String> idCardList = Arrays.asList(IDCARD);
        return new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                // 返回空字符串，就代表匹配不成功，返回null代表匹配成功
                for (int i = 0; i < source.toString().length(); i++) {
                    if (!idCardList.contains(String.valueOf(source.charAt(i)))) {
                        return "";
                    }
                    if (editText.getText().toString().length() < 17) {
                        if ("x".equals(String.valueOf(source.charAt(i))) || "X".equals(String.valueOf(source.charAt(i)))) {
                            return "";
                        }
                    }
                }
                return null;
            }
        };
    }

    /**
     * 校验手机号码格式是否合法
     * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
     * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
     * 电信号段: 133,149,153,170,173,177,180,181,189
     *
     * @param number 用户输入的手机号码
     * @return [true符合规范, false不符合规范]
     */
    public static boolean isPhoneNumber(String number) {
        // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(16[0-9])|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[0-9]))\\d{8}$";
        if (TextUtils.isEmpty(number))
            return false;
        else
            return number.matches(telRegex);
    }

    /**
     * 区号+座机号码+分机号码
     *
     * @param fixedPhone
     * @return
     */
    public static boolean isFixedPhone(String fixedPhone) {
        String reg = "(?:(\\(\\+?86\\))(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)|" +
                "(?:(86-?)?(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)";
        return Pattern.matches(reg, fixedPhone);
    }

    /**
     * 校验座机号
     *
     * @param str
     * @return
     */
    public static boolean isLandline(String str) {
        Pattern p1 = null, p2 = null;
        Matcher m = null;
        boolean isPhone = false;
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
        if (str.length() > 9) {
            m = p1.matcher(str);
            isPhone = m.matches();
        } else {
            m = p2.matcher(str);
            isPhone = m.matches();
        }
        return isPhone;
    }

    /**
     * 校验密码
     *
     * @param password
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    /**
     * 将double转为数值，并最多保留num位小数。例如当num为2时，1.268为1.27，1.2仍为1.2；1仍为1，而非1.00;100.00则返回100。
     *
     * @param d   源数据
     * @param num 需要保留的小数位数
     * @return
     */
    public static String double2String(double d, int num) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(num);//保留两位小数
        nf.setGroupingUsed(false);//去掉数值中的千位分隔符
        String temp = nf.format(d);
        if (temp.contains(".")) {
            String s1 = temp.split("\\.")[0];
            String s2 = temp.split("\\.")[1];
            for (int i = s2.length(); i > 0; --i) {
                if (!s2.substring(i - 1, i).equals("0")) {
                    return s1 + "." + s2.substring(0, i);
                }
            }
            return s1;
        }
        return temp;
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param context  上下文对象
     * @param phoneNum 手机号
     */
    public static void callPhone(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }

    /**
     * 手机号用****号隐藏中间数字
     *
     * @param phone
     * @return
     */
    public static String setTingPhone(String phone) {
        String phone_s = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        return phone_s;
    }

    /**
     * 邮箱用****号隐藏前面的字母
     *
     * @return
     */
    public static String setTingEmail(String email) {
        String emails = email.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
        return emails;
    }

    /**
     * toDecimal(保留两位小数) (这里描述这个方法适用条件 – 可选)
     *
     * @return void
     * @throws
     * @since 1.0.0
     */
    public static String toDecimal(Double d) {
        String decimal = new DecimalFormat("0.00").format(d);
        if (decimal != null) {
            if (".00".equals(decimal)) {
                return "0" + decimal;
            } else {
                return decimal;
            }
        } else {
            return "0.00";
        }
    }

    /**
     * strint 转 double 保留两位小数
     *
     * @param obj
     * @return
     */
    public static double toDouble(String obj) {
        try {
            return Double.parseDouble(obj);
        } catch (Exception var2) {
            return 0.00;
        }
    }

    /**
     * 判断邮编
     *
     * @param zipString 邮编
     * @return
     */
    public static boolean isZipNO(String zipString) {
        String str = "^[1-9][0-9]{5}$";
        return Pattern.compile(str).matcher(zipString).matches();
    }

    /**
     * 将银行卡中间八个字符隐藏为*
     *
     * @param bankCardNum 银行卡号
     * @return
     */
    public static String getHideBankCardNum(String bankCardNum) {
        try {
            if (bankCardNum == null) return "未绑定";
            int length = bankCardNum.length();
            if (length > 4) {
                String startNum = bankCardNum.substring(0, 4);
                String endNum = bankCardNum.substring(length - 4, length);
                bankCardNum = startNum + " ******** " + endNum;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bankCardNum;
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @return
     */
    public static int strToInt(String str) {
        if (!VerifyUtils.isEmpty(str)) {
            return (new Double(Double.valueOf(str))).intValue();
        } else {
            return 0;
        }
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param str
     * @return
     */
    public static String subZeroAndDot(String str) {
        if (str.indexOf(".") > 0) {
            str = str.replaceAll("0+?$", "");//去掉多余的0
            str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return str;
    }

    /**
     * 判断是否有安装APP
     *
     * @param context 上下文
     * @param uri     包名
     * @return
     */
    public static boolean isApplicationAvilible(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    /**
     * 字符串切割
     *
     * @param str  字符串
     * @param regx 条件
     * @return
     */
    public static String[] strCutting(String str, String regx) {
        if (!VerifyUtils.isEmpty(str) && !VerifyUtils.isEmpty(regx)) {
            return str.split(regx);
        }
        return null;
    }

     /*
    校验过程：
    1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
    2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，将个位十位数字相加，即将其减去9），再求和。
    3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
    */

    /**
     * 校验银行卡卡号
     */
    public static boolean checkBankCard(String bankCard) {
        if (bankCard.length() < 15 || bankCard.length() > 19) {
            return false;
        }
        char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return bankCard.charAt(bankCard.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeBankCard
     * @return
     */
    private static char getBankCardCheckCode(String nonCheckCodeBankCard) {
        if (nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0
                || !nonCheckCodeBankCard.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeBankCard.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 校验邮箱地址是否合法
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (null == email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
