package test;

import java.text.DecimalFormat;

public class GenerateTimePeriod {
	public static String beginTime="0000";//时分格式
	public static String endTime="2350";
	public static int interval_time=5;//间隔时间，单位为分钟
	
	static String[] produceArray(String beginTime, String endTime, int interval) {
		DecimalFormat sd = new DecimalFormat("00");
		int tempEnd;
		int beginHour = Integer.valueOf(beginTime.substring(0, 2));
		int beginMin = Integer.valueOf(beginTime.substring(2, 4));
		int endHour = Integer.valueOf(endTime.substring(0, 2));
		int endMin = Integer.valueOf(endTime.substring(2, 4));
		int interval_min = endHour * 60 + endMin - beginHour * 60 - beginMin;
		int size = interval_min / interval;
		if (interval_min % interval != 0) {
			++size;
		}
		String[] array = new String[size + 1];// 多一个存储 最后一个边界值

		array[0] = beginTime;
		for (int i = 1; i < size; i++) {
			tempEnd = beginHour * 60 + beginMin + interval * i;
			array[i] = sd.format(tempEnd / 60) + sd.format(tempEnd % 60);
//			System.out.print(array[i]+"*");
		}
//		System.out.println();
		array[size] = endTime;
//		for(int i=0;i<array.length;i++){
//			System.out.print(array[i]+"*");
//		}
//		System.out.println();
		return array;
	}
}
