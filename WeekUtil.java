package com.imstlife.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeekUtil {
		//获取周一
		 private static final int FIRST_DAY = Calendar.MONDAY;
	 
		public static List<String> getWeekdays() {
	    	List<String> list = new ArrayList<String>();
	    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd E");
	        Calendar calendar = Calendar.getInstance();
	        while (calendar.get(Calendar.DAY_OF_WEEK) != FIRST_DAY) {
	            calendar.add(Calendar.DATE, -1);
	        }
	        for (int i = 0; i < 7; i++) {
	        	String str = dateFormat.format(calendar.getTime());
	        	str=str.replace("星期", "");
	        	System.out.println(str);
	        	list.add(str);
	        	calendar.add(Calendar.DATE, 1);
	        }
	        return list;
	    }
	 
		/**
		 * 
		 * 作用：获取日期
		 * 参数：@param parrten 希望获取的日期格式 
		 * 参数：@param count 天数 1周为7 一月为30 一年为12
		 * 参数：@param type 类型 1为天数 2位月份
		 * 返回值：@return List<String>
		 * 时间：2017年12月8日下午2:17:20
		 */
		public static List<String> getInDays(String parrten,Integer type,Integer count){
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat(parrten);
			List<String> list= new ArrayList<String>();
			for(int i=0;i<count;i++){
				if(type==1){
						calendar.add(Calendar.DATE, -1);
						list.add(dateFormat.format(calendar.getTime()));
				}else if(type==2){
					if(calendar.get(Calendar.MONTH)==0){
						list.add("12");
					}else{
						list.add(calendar.get(Calendar.MONTH)+"");
					}
					calendar.add(Calendar.MONTH, -1);
				}
			}
			Collections.reverse(list);
			return list;
		}
		public static String[] getDays(String str1,int length,int type){
			if(type==1){
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat dateFormat = new SimpleDateFormat(str1);
				String[] str = new String[length];
			    for (int i = 0; i < length; i++) {
		        	String str2 = dateFormat.format(calendar.getTime());
		        	str[str.length-1-i]=str2+"000000";
		        	calendar.add(Calendar.DATE, -1);
		        }
				return str;
			}else{
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat dateFormat = new SimpleDateFormat(str1);
				String[] str = new String[length];
			    for (int i = 0; i < length; i++) {
		        	String str2 = dateFormat.format(calendar.getTime());
		        	str[str.length-i-1]=calendar.get(Calendar.YEAR)+str2+"00000000";
		        	calendar.add(Calendar.MONTH, -1);
		        }
				return str;
			}
		}
		/**
		 * 根据传入的参数日期得到这一周的周一和周末的日期
		 * @param date 
		 * @return
		 */
		public static List<Date> getStartAndEndWeek(Date date){
			List<Date> dates = new ArrayList<>();
			Date startDate=null;
			Date endDate=null;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
			//由于weekDay=1是周日...weekDay=7是周六，所以应该处理下，，按我们的习惯
			switch(--weekDay){
			case 0://周末
				calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 6);  
				startDate = calendar.getTime();  
			    endDate = date;
			    break;
			case 1://周一
				calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 6);  
				endDate = calendar.getTime();  
			    startDate = date;
			    break;
			case 2:
				calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 5);  
				endDate = calendar.getTime();  
				calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) -6);
			    startDate = calendar.getTime();
			    break;  
			case 3:
				calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 4);  
				endDate = calendar.getTime();  
				calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) -6);
			    startDate = calendar.getTime();
			    break;  
			case 4:
				calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 3);  
				endDate = calendar.getTime();  
				calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) -6);
			    startDate = calendar.getTime();
			    break;  
			case 5:
				calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 2);  
				endDate = calendar.getTime();  
				calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) -6);
			    startDate = calendar.getTime();
			    break;  
			case 6:
				calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);  
				endDate = calendar.getTime();  
				calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) -6);
			    startDate = calendar.getTime();
			    break;  
				
			}
			dates.add(startDate);
			dates.add(endDate);
			return dates;
		}
		public static Map<String,String> getWeekList(Date dBegin, Date dEnd){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Map<String,String> map = new HashMap<String,String>();
			List<Date> lDate = new ArrayList<>();
			lDate.add(dBegin);
			Calendar calBegin = Calendar.getInstance();
			// 使用给定的 Date 设置此 Calendar 的时间
			calBegin.setTime(dBegin);
			Calendar calEnd = Calendar.getInstance();
			// 使用给定的 Date 设置此 Calendar 的时间
			calEnd.setTime(dEnd);
			// 测试此日期是否在指定日期之后
			while (dEnd.after(calBegin.getTime())) {
				// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
				calBegin.add(Calendar.DAY_OF_MONTH, 1);
				lDate.add(calBegin.getTime());
			}
			
			map.put("mon", sdf.format(lDate.get(0)));
			map.put("tues", sdf.format(lDate.get(1)));
			map.put("wed", sdf.format(lDate.get(2)));
			map.put("thur", sdf.format(lDate.get(3)));
			map.put("fri", sdf.format(lDate.get(4)));
			map.put("sat", sdf.format(lDate.get(5)));
			map.put("sun", sdf.format(lDate.get(6)));
			return map;
		}

	public static void main(String[] args) throws ParseException {
		String[] dateList = getDays("yyyMMdd",7,1);
		String startTime = "";
		String endTime = "";
		for (int i = 0; i <dateList.length; i++) {
			startTime=dateList[i];
			endTime=dateList[i].substring(0, 8)+"235959";
			System.out.println(startTime);
			System.out.println(endTime);
		}
		/*
		 * List<String> days = WeekUtil.getInDays("yyyyMMdd", 1, 7+1); int start
		 * =Integer.parseInt(days.get(0)) ; int end
		 * =Integer.parseInt(days.get(7)) ;
		 * System.out.println(getInDays("yyyyMMdd", 1, 8));
		 * System.out.println(start+"\t"+end);
		 */
		// System.out.println(WeekUtil.getDays("YYYYMMdd",180, 1).length);
		// Date date = new Date();

		/*DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		Date date = dateFormat1.parse("2018-04-12");

		List<Date> startAndEndWeek = getStartAndEndWeek(date);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (Date date2 : startAndEndWeek) {
			String time = sdf.format(date2.getTime());
			System.out.println(time);
		}
		
		Map<String,String> map = getWeekList(startAndEndWeek.get(0), startAndEndWeek.get(1));
		for (String key : map.keySet()) {
			String value = map.get(key);
			System.out.println(key + "---" + value);
		}*/
		
		}
		
}
