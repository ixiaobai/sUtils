package org.bridge;

import javax.servlet.http.HttpServletRequest;
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
    public static boolean isNullOrEmpty(String source){
        if(null == source || "".equals(source)) return true;
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

}