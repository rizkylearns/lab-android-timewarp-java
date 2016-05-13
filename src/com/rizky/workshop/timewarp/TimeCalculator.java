package com.rizky.workshop.timewarp;

public class TimeCalculator {
	
	public static long getMillisFromHours(long hours)
	{
		return getMillisFromMinutes(hours * 60);
	}
	
	public static long getMillisFromMinutes(long minutes)
	{
		return minutes * 60 * 1000;		
	}
}
