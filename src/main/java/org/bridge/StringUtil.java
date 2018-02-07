package org.bridge;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    /**
     * <p>1</p>
     * <p>方法名称: arrayInsertArray|描述: 数组插入到另一个数组特定位置</p>
     * @author ixiaobai
     * @param arr 被插入数组
     * @param seat 插入位置(>=0；将data插入到arr第seat位置后)
     * @param data 插入数组
     * @return
     */
    public static String[] arrayInsertArray(String[] arr, int seat, String[] data){
        if(seat>=0 && arr!=null && data!=null) {
            int arrLength = arr.length;
            int dataLen = data.length;
            //声明一个长度为二者加和的数组
            String [] arr2 = new String [arrLength + dataLen];
            if(seat<=arrLength) {
                //插入的位置小于等于arr长度时
                //将插入位置前的内容添加到新数组
                System.arraycopy(arr, 0, arr2, 0, seat);
                //将需要插入的内容加到新数组
                //arr2[seat] = data;
                System.arraycopy(data, 0, arr2, seat, dataLen);
                //将原数组剩余部分插入到新数组
                System.arraycopy(arr, seat, arr2, seat+dataLen, arrLength-seat);
            } else {
                //插入的位置大于array的长度时
                //array后直接拼接data
                System.arraycopy(arr, 0, arr2, 0, arrLength);
                System.arraycopy(data, 0, arr2, arrLength, dataLen);
            }
            //返回新数组
            return arr2;
        } else {
            return null;
        }
    }

    /**
     * <p>2</p>
     * <p>方法名称: isNullOrEmpty|描述: 判断字符串是否为null或者空</p>
     * @author ixiaobai
     * @param source 原字符串
     * @return
     */
    public static boolean isNullOrEmpty(Object source){
        if(null == source ||"".equals(source.toString()) || 0==source.toString().length()) return true;
        return false;
    }

    /**
     * <p>3</p>
     * <p>方法名称: ignoreCaseReplace|描述: 忽略大小写替换</p>
     * @author ixiaobai
     * @param source 原字符串
     * @param oldString 被替换字符串
     * @param newString 替换字符串
     * @return
     */
    public static String ignoreCaseReplace(String source, String oldString, String newString){
        if(isNullOrEmpty(source) || null==oldString || 0==oldString.length()) {
            return source;
        }
        Pattern p = Pattern.compile(oldString, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(source);
        String ret=m.replaceAll(newString);
        return ret;
    }

    /**
     * <p>4</p>
     * <p>方法名称: isIE|描述: 是否为IE处理器</p>
     * @author ixiaobai
     * @param agent 原字符串
     * @return
     */
    public static boolean isIE(String agent){
        if(isNullOrEmpty(agent)) return false;
        agent = agent.toLowerCase();
        return agent.indexOf("msie")>0 || (agent.indexOf("gecko")>0 && agent.indexOf("rv:11")>0);
    }
    public static boolean isIE(HttpServletRequest request){
        return isIE(request.getHeader("USER-AGENT"));
    }

    /**
     * <p>方法名称: IgnoreCaseReplace|描述: 忽略大小写替换</p>
     * @author ixiaobai
     * @param source
     * @param oldString
     * @param newString
     * @return
     */
    public static String IgnoreCaseReplace(String source, String oldString, String newString){
        Pattern p = Pattern.compile(oldString, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(source);
        String ret=m.replaceAll(newString);
        return ret;
    }

    /**
     * <p>方法名称: textToHtml|描述: 文本转化为html</p>
     * @param s
     * @return
     */
    public static String textToHtml(String s){
        if(isNullOrEmpty(s)){
            s = "";
        }else{
            s = s.replaceAll(" ","&nbsp;");
//			s = s.replaceAll(" ","　");
            s = s.replaceAll("\n","<br />");
        }
        return s;
    }

    /**
     * <p>方法名称: deleteZKH|描述: 去掉字符串前后的中括号[](如果有的话)</p>
     * @param s
     * @return
     */
    public static String deleteZKH(String s){
        if(!isNullOrEmpty(s)){
            if(s.startsWith("["))
                s=s.substring(1);
            if(s.endsWith("]"))
                s=s.substring(0, s.length()-1);
        }
        return s;
    }
    /**
     * <p>方法名称: stringToArray|描述: 将以中括号规范的字符串分割成数组</p>
     * @param s
     * @return
     */
    public static String[] stringToArray(String s){
        String[] strs=new String[0];
        if(!isNullOrEmpty(s)){
            s=deleteZKH(s);
            strs = s.split("\\]\\[");
        }
        return strs;
    }
    public static <T> List<T> arrayToList(T[] array){
        List<T> list=new ArrayList<T>();
        if(null != array){
            for(int i=0;i<array.length;i++){
                list.add(array[i]);
            }
        }
        return list;
    }

    /**
     * <p>方法名称: arrayToString|描述: 数组转化为以指定分隔符分隔的字符串</p>
     * @param array 数组
     * @param separator 分隔符
     * @return
     */
    public static String arrayToString(String[] array,String separator){
        if(array != null){
            StringBuilder sb=new StringBuilder("");
            for (int i = 0; i < array.length; i++) {
                if(i !=0 )sb.append(separator);
                sb.append(array[i]);
            }
            return sb.toString();
        }else{
            return "";
        }
    }

    /**
     * <p>日期转化为指定格式的字符串</p>
     * @param date 要转化的日期(应是一个有效的date)
     * @param format 格式字符串，yyyy-MM-dd HH:mm:ss.SSS（详见SimpleDateFormat类）
     * @return
     */
    public static String dateToString(Object date,String format){
        if(isNullOrEmpty(date)) return "";
        String str = "";
        try{
            str = new SimpleDateFormat(format, Locale.CHINA).format((Date)date);
        }catch(Exception e){
            e.printStackTrace();
        }
        return str;
    }

    /**
     * <p>方法名称: stringToDate|描述: 字符串转化为日期</p>
     * <p>注意：此方法中字符串的0不可省略</p>
     * @param str 要转化的字符串
     * @param format 格式字符串，yyyy-MM-dd HH:mm:ss.SSS（详见SimpleDateFormat类）
     * @return 转化失败返回null
     */
    public static Date stringToDate(String str,String format){
        if(isNullOrEmpty(str)) return null;
        Date date = null;
        try{
            if(str.length() != format.length()) return null;
            date = new SimpleDateFormat(format).parse(str);
        }catch(Exception e){
            e.printStackTrace();
        }
        return date;
    }

    /**从指定格式的日期字符串中读取月份
     * @param dateString yyyy-MM or yyyyMM
     * @return
     */
    public static String getMonthFromDateString(String dateString, String regex){
        dateString=dateString.replaceAll(regex, "");//去掉间隔符号
        String month=dateString.substring(4,6);
        if(month.startsWith("0")){
            month=month.substring(1,2);
        }
        return month;
    }

    /**
     * 如超过指定长度则截取并返回指定长度的字符串
     * @param str 要处理的源字符串
     * @param maxLen 长度
     * @param flag 超长后是否在截取末尾添加省略号
     * @return
     */
    public static String subStr(String str,int maxLen,boolean flag){
        if(isNullOrEmpty(str)) return "";
        if(str.length()<=maxLen) return str;
        if(flag) return str.substring(0,maxLen-1)+"...";
        else return str.substring(0,maxLen);
    }

    /**
     * <p>方法名称: transcoding|描述: ISO编码转化为GBK编码</p>
     * @param fType 初始编码形式
     * @param tType 转换成的编码形式
     * @param Str 目标字符串
     * @return
     */
    public static String transcoding(String fType, String Str, String tType) {
        if (isNullOrEmpty(fType)||isNullOrEmpty(Str)||isNullOrEmpty(tType)) return null;
        String result = "";
        try {
            result = new String(Str.getBytes(fType), tType);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**二进制数组转化为16进制字符串
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b){
        String hs="";
        String stmp="";
        for (int n=0; n<b.length; n++){
            stmp=(java.lang.Integer.toHexString(b[n] & 0xFF));
            if (stmp.length()==1) hs=hs+"0"+stmp;
            else hs=hs+stmp;
        }
        return hs;
    }

    /**MD5加密
     * @param s 要加密的字符串
     * @return 加密后的字符串
     */
    public static String encodeToMD5(String s){
        MessageDigest md=null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(s.getBytes());
        byte[] bR=md.digest();
        for(int i=0;i<bR.length;i++){

        }
        return byte2hex(bR).toUpperCase();
    }
}