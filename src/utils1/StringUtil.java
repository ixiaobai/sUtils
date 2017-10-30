package utils1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @模块：[StringUtil]
* @描述: [字符串处理工具类]
* @作者: Bridge
* @日期: 2017-10-19 16:04
* <p>版权所有:(C)1998-2016 Bridge</p>
*/

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
		if(StringUtil.isNullOrEmpty(source) || null==oldString || 0==oldString.length()) {
			return source;
		}
		Pattern p = Pattern.compile(oldString, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(source);
		String ret=m.replaceAll(newString);
		return ret;
	}


	public static void main(String[] args) {
		/**********1、变量池***********/
		String str1[] = {"1","2","6","7"};
		String str2[] = {"3","4","5"};
		String s1 = null;
		String s2 = null;
		String s3 = null;
		/**********2、方法池***********/
		System.out.println("/**********1***********/");
		String str3[] = StringUtil.arrayInsertArray(str1, 2, str2);
		for(String s:str3) {
			System.out.print(s+"\t");
		}
		System.out.println();
		str3 = StringUtil.arrayInsertArray(str1, 10, str2);
		for(String s:str3) {
			System.out.print(s+"\t");
		}
		System.out.println();
		System.out.println("/**********2***********/");
		System.out.print(StringUtil.isNullOrEmpty(s1)+"\n");
		System.out.println("/**********3***********/");
		s1="a b c d ";
		s2=" ";
		s3=",";
		System.out.print(StringUtil.ignoreCaseReplace(s1, s2, s3)+"\n");
		
	}
}
