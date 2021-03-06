package com.njnu.kai.mockclick.util;

import android.text.SpannableStringBuilder;
import android.text.Spanned;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class StringUtils {

    public static final String UTF_8 = "UTF-8";

    private static final int CACHE_SIZE = 4096;

    /**
     * 将s中字符用delimiter分割连接起来
     *
     * @param delimiter 分隔符
     * @param segments  被连接的数据
     * @return 返回连接号的字符串
     * @see StringUtils#join(String, Object[])
     */
    public static String join(String delimiter, Collection<?> segments) {
        StringBuilder stringBuilder = new StringBuilder();
        if (segments != null) {
            appendCollectionObjectToStringBuilder(stringBuilder, delimiter, segments);
        }
        String outString = stringBuilder.toString();
        if (outString.endsWith(delimiter)) {
            return outString.substring(0, outString.length() - delimiter.length());
        }
        return outString;
    }

    /**
     * 将对象链接成一个字符串，使用delimiter传入的字符串分割，
     * <p><b>注意:</b>如果前一个片段为字符串且以delimiter结束或者为空(null获取为"")，将不会重复添加此字符串</p>
     * <p>字符串末尾不会自动添加delimiter字符串</p>
     * <p>如果没有传入参数，返回一个<b>空字符串</b></p>
     *
     * @param delimiter 分割字符串
     * @param segments  所有传入的部分
     * @return 连接完毕后的字符串
     */
    public static String join(String delimiter, Object... segments) {
        StringBuilder stringBuilder = new StringBuilder();
        if (segments != null) {
            int size = segments.length;
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    appendObjectToStringBuilder(stringBuilder, delimiter, segments[i]);
                }
            }
        }
        String outString = stringBuilder.toString();
        if (outString.endsWith(delimiter)) {
            return outString.substring(0, outString.length() - delimiter.length());
        }
        return outString;
    }


    private static void appendArrayObjectToStringBuilder(StringBuilder stringBuilder, String delimiter, Object array) {
        int length = Array.getLength(array);
        for (int i = 0; i < length; i++) {
            appendObjectToStringBuilder(stringBuilder, delimiter, Array.get(array, i));
        }
    }

    private static void appendCollectionObjectToStringBuilder(StringBuilder stringBuilder, String delimiter, Collection<?> collection) {
        Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            appendObjectToStringBuilder(stringBuilder, delimiter, iterator.next());
        }
    }

    private static void appendObjectToStringBuilder(StringBuilder stringBuilder, String delimiter, Object object) {
        if (object == null) {
            return;
        }
        if (object.getClass().isArray()) {
            appendArrayObjectToStringBuilder(stringBuilder, delimiter, object);
        } else if (object instanceof Collection) {
            appendCollectionObjectToStringBuilder(stringBuilder, delimiter, (Collection) object);
        } else {
            String objectString = object.toString();
            stringBuilder.append(objectString);
            if (!isEmpty(objectString) && !objectString.endsWith(delimiter)) {
                stringBuilder.append(delimiter);
            }
        }
    }

    /**
     * 测试传入的字符串是否为空
     *
     * @param string 需要测试的字符串
     * @return 如果字符串为空（包括不为空但其中为空白字符串的情况）返回true，否则返回false
     */
    public static boolean isEmpty(String string) {
        return string == null || string.trim().length() == 0;
    }

    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }

    /**
     * 传入的字符串是否相等
     *
     * @param a a字符串
     * @param b b字符串
     * @return 如果字符串相等（值比较）返回true，否则返回false
     */
    public static boolean equal(String a, String b) {
        return a == b || (a != null && b != null && a.length() == b.length() && a.equals(b));
    }

    /**
     * 将字符串用分隔符分割为long数组
     * <p><b>只支持10进制的数值转换</b></p>
     * <p><b>如果格式错误，将抛出NumberFormatException</b></p>
     *
     * @param string    字符串
     * @param delimiter 分隔符
     * @return 分割后的long数组.
     */
    public static long[] splitToLongArray(String string, String delimiter) {
        List<String> stringList = splitToStringList(string, delimiter);

        long[] longArray = new long[stringList.size()];
        int i = 0;
        for (String str : stringList) {
            longArray[i++] = Long.parseLong(str);
        }
        return longArray;
    }

    /**
     * 将字符串用分隔符分割为Long列表
     * <p><b>只支持10进制的数值转换</b></p>
     * <p><b>如果格式错误，将抛出NumberFormatException</b></p>
     *
     * @param string    字符串
     * @param delimiter 分隔符
     * @return 分割后的Long列表.
     */
    public static ArrayList<Long> splitToLongList(String string, String delimiter) {
        List<String> stringList = splitToStringList(string, delimiter);

        ArrayList<Long> longList = new ArrayList<Long>(stringList.size());
        for (String str : stringList) {
            longList.add(Long.parseLong(str));
        }
        return longList;
    }

    /**
     * 将字符串用分隔符分割为字符串数组
     *
     * @param string    字符串
     * @param delimiter 分隔符
     * @return 分割后的字符串数组.
     */
    public static String[] splitToStringArray(String string, String delimiter) {
        List<String> stringList = splitToStringList(string, delimiter);
        return stringList.toArray(new String[stringList.size()]);
    }

    /**
     * 将字符串用分隔符分割为字符串列表
     *
     * @param string    字符串
     * @param delimiter 分隔符
     * @return 分割后的字符串数组.
     */
    public static ArrayList<String> splitToStringList(String string, String delimiter) {
        ArrayList<String> stringList = new ArrayList<String>();
        if (!isEmpty(string)) {
            int length = string.length();
            int pos = 0;

            do {
                int end = string.indexOf(delimiter, pos);
                if (end == -1) {
                    end = length;
                }
                stringList.add(string.substring(pos, end));
                pos = end + delimiter.length(); // skip the delimiter
            } while (pos < length);
        }
        return stringList;
    }

    /**
     * InputSteam 转换到 String，会把输入流关闭
     *
     * @param inputStream 输入流
     * @return String 如果有异常则返回null
     */
    public static String stringFromInputStream(InputStream inputStream) {
        try {
            byte[] readBuffer = new byte[CACHE_SIZE];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while (true) {
                int readLen = inputStream.read(readBuffer, 0, CACHE_SIZE);
                if (readLen <= 0) {
                    break;
                }

                byteArrayOutputStream.write(readBuffer, 0, readLen);
            }

            return byteArrayOutputStream.toString(UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 测试是否为有效的电子邮件格式
     *
     * @param string 内容
     * @return true if yes
     */
    public static boolean isEmailFormat(String string) {
        if (StringUtils.isEmpty(string)) {
            return false;
        }
        String emailFormat = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        return Pattern.matches(emailFormat, string);
    }

    /**
     * 是否在长度范围之类
     *
     * @param string 内容
     * @param begin  最小长度（inclusive）
     * @param end    最大长度（inclusive）
     * @return 字符串长度在begin和end之内返回true，否则返回false。<p><b>输入字符串为null时，返回false</b></p>
     */
    public static boolean lengthInRange(String string, int begin, int end) {
        return string != null && string.length() >= begin && string.length() <= end;
    }

    /**
     * 可读性字节数
     *
     * @param bytes bytes
     * @return MB KB
     */
    public static String readableByte(long bytes) {

        int unit = 1024;
        if (bytes < unit) {
            return bytes + "B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        char pre = "KMGTPE".charAt(exp - 1);
        return String.format("%.1f%cB", bytes / Math.pow(unit, exp), pre);
    }

    /**
     * 检查字符是不是英文字母或数字，系统方法Character.isLetterOrDigit(char codePoint)对中文字符不能作出正确的判断。
     *
     * @param codePoint 字符
     * @return 是否是英文字符或数字
     */
    public static boolean isLetterOrDigit(char codePoint) {
        return ('A' <= codePoint && codePoint <= 'Z')
                || ('a' <= codePoint && codePoint <= 'z')
                || ('0' <= codePoint && codePoint <= '9');
    }


    public static SpannableStringBuilder append(SpannableStringBuilder builder, CharSequence text, Object what) {
        int start = builder.length();
        if (text != null && text.length() > 0) {
            builder.append(text);
            builder.setSpan(what, start, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

    public static String urlEncode(String string) {
        try {
            return URLEncoder.encode(string, UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    public static String urlDecode(String string) {
        try {
            return URLDecoder.decode(string, UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    public static long toLong(String str) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }
}

