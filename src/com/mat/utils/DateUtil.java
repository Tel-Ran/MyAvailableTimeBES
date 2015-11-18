package com.mat.utils;

import java.util.Date;

public class DateUtil {
	
	public static boolean idTheSameDate(Date a, Date b) {
		if (a.getTime() - b.getTime() < 59999)
			return true;
		return false;
	}
}
