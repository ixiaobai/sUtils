package org.bridge;

import java.math.BigDecimal;

public class BigDecimalUtil {
	/**
	 *<p>方法名称：round | 方法描述：将传入的对象转化为BigDecimal然后四舍五入保留小数 </p>
	 * @param ob 要传的对象
	 * @param num 保留的小数位数
	 * @return
	 */
	public static String getRound(Object ob,int num){
		String zero ="0";
		for(int i = 0 ; i < num ; i++){
			if(i==0){
				zero += ".0";
			}else{
				zero += "0";
			}
		}
		return new BigDecimal(StringUtil.isNullOrEmpty(String.valueOf(ob))?zero:String.valueOf(ob)).setScale(num,BigDecimal.ROUND_HALF_UP).toPlainString();
	}
	/**
	 *<p>方法名称：getRatio | 方法描述：获得占比比例 </p>
	 * @param ob1 除数
	 * @param ob2 被除数
	 * @param num 保留小数位数
	 * @return
	 */
	public static String getRatio(Object ob1,Object ob2,int num){
		BigDecimal l1 =new BigDecimal(StringUtil.isNullOrEmpty(String.valueOf(ob1))?"0":String.valueOf(ob1)).multiply(new BigDecimal("100"));
		BigDecimal l2 =new BigDecimal(StringUtil.isNullOrEmpty(String.valueOf(ob2))?"0":String.valueOf(ob2));
		if(0==l2.compareTo(BigDecimal.ZERO)){
			String zero ="0";
			for(int i = 0 ; i < num ; i++){
				if(i==0){
					zero += ".0";
				}else{
					zero += "0";
				}
			}
			return zero;
		}else{
			return l1.divide(l2,num,BigDecimal.ROUND_HALF_UP).toPlainString();
		}
	}
	/**
	 *<p>方法名称：getSameRatio | 方法描述：获得同比比例 </p>
	 * @param ob1 除数
	 * @param ob2 被除数
	 * @param num 保留小数位数
	 * @return
	 */
	public static String getSameRatio(Object ob1,Object ob2,int num){
		BigDecimal l1 =new BigDecimal(StringUtil.isNullOrEmpty(String.valueOf(ob1))?"0":String.valueOf(ob1)).multiply(new BigDecimal("100"));
		BigDecimal l2 =new BigDecimal(StringUtil.isNullOrEmpty(String.valueOf(ob2))?"0":String.valueOf(ob2));
		
		if(0==l2.compareTo(BigDecimal.ZERO)){
			String zero ="0";
			for(int i = 0 ; i < num ; i++){
				if(i==0){
					zero += ".0";
				}else{
					zero += "0";
				}
			}
			return zero;
		}else{
			
			return l1.divide(l2,num,BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("100")).toPlainString();
		}
	}
	/**
	 *<p>方法名称：getAdd | 方法描述：加法 </p>
	 * @param ob1 
	 * @param ob2 
	 * @return
	 */
	public static String getAdd(Object ob1,Object ob2){
		BigDecimal l1 =new BigDecimal(StringUtil.isNullOrEmpty(String.valueOf(ob1))?"0":String.valueOf(ob1));
		BigDecimal l2 =new BigDecimal(StringUtil.isNullOrEmpty(String.valueOf(ob2))?"0":String.valueOf(ob2));
		return l1.add(l2).toPlainString();
	}
	/**
	 *<p>方法名称：getSubtract | 方法描述：减法 </p>
	 * @param ob1 减数
	 * @param ob2 被减数
	 * @return
	 */
	public static String getSubtract(Object ob1,Object ob2){
		BigDecimal l1 =new BigDecimal(StringUtil.isNullOrEmpty(String.valueOf(ob1))?"0":String.valueOf(ob1));
		BigDecimal l2 =new BigDecimal(StringUtil.isNullOrEmpty(String.valueOf(ob2))?"0":String.valueOf(ob2));
		return l1.subtract(l2).toPlainString();
	}
	/**
	 *<p>方法名称：getMultiply | 方法描述：乘法 </p>
	 * @param ob1 
	 * @param ob2 
	 * @return
	 */
	public static String getMultiply(Object ob1,Object ob2){
		BigDecimal l1 =new BigDecimal(StringUtil.isNullOrEmpty(String.valueOf(ob1))?"0":String.valueOf(ob1));
		BigDecimal l2 =new BigDecimal(StringUtil.isNullOrEmpty(String.valueOf(ob2))?"0":String.valueOf(ob2));
		return l1.multiply(l2).toPlainString();
	}

	/**
	 *<p>方法名称：getDivide | 方法描述：除法 </p>
	 * @param ob1 除数
	 * @param ob2 被除数
	 * @param num 保留小数位数
	 * @return
	 */
	public static String getDivide(Object ob1,Object ob2,int num){
		BigDecimal l1 =new BigDecimal(StringUtil.isNullOrEmpty(String.valueOf(ob1))?"0":String.valueOf(ob1));
		BigDecimal l2 =new BigDecimal(StringUtil.isNullOrEmpty(String.valueOf(ob2))?"0":String.valueOf(ob2));
		if(0==l2.compareTo(BigDecimal.ZERO)){
			String zero ="0";
			for(int i = 0 ; i < num ; i++){
				if(i==0){
					zero += ".0";
				}else{
					zero += "0";
				}
			}
			return zero;
		}else{
			return l1.divide(l2,num,BigDecimal.ROUND_HALF_UP).toPlainString();
		}
	}
}
