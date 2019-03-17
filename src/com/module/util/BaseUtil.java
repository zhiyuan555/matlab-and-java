package com.module.util;

public class BaseUtil {
	
	public static double calculateRainP(double rainEffec) {
		double result = Math.pow(Math.E, (0.076*rainEffec - 3.675))/(1 + Math.pow(Math.E, (0.076*rainEffec - 3.675)));
		return result;
	}
}
