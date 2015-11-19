package com.mat.utils;

import java.util.Date;

public class DateUtil {

	public static boolean isTheSameDate(Date a, Date b) {
		if (Math.abs(a.getTime() - b.getTime()) < 59999)
			return true;
		return false;
	}
}
