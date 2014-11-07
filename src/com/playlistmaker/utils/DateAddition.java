package com.playlistmaker.utils;

import java.util.Calendar;

public class DateAddition {
	
	public static String getDateAddition(){
		Calendar cal = Calendar.getInstance();
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH)+1;
		int year = cal.get(Calendar.YEAR);
		
		String dayOfMonthStr = String.valueOf(dayOfMonth) + "_";
		String monthStr = String.valueOf(month) + "_";
		String yearStr = String.valueOf(year);
		
		return dayOfMonthStr + monthStr + yearStr;
	}
}
