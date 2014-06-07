package base;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.hbase.HBaseConfiguration;

public class Genetic {
	public static String mainTablename = "test";
	public static String statisTableName = "testStat";
	public static String eventTableName = "eventTable";
	public static String mobileTableName = "mobile";
	public static String detectorTableName = "detector";
	public static double distance = 500.0;
	public static int gpslength = 9;
	// public static Configuration conf = HBaseConfiguration.create();

	private static SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss");

	/**
	 * <p>
	 * 判断线段是否在矩形内
	 * <p>
	 * 先看线段所在直线是否与矩形相交， 如果不相交则返回false， 如果相交，
	 * 则看线段的两个点是否在矩形的同一边（即两点的x(y)坐标都比矩形的小x(y)坐标小，或者大）, 若在同一边则返回false， 否则就是相交的情况。
	 * </p>
	 * 
	 * @param linePointX1
	 *            线段起始点x坐标
	 * @param linePointY1
	 *            线段起始点y坐标
	 * @param linePointX2
	 *            线段结束点x坐标
	 * @param linePointY2
	 *            线段结束点y坐标
	 * @param rectangleLeftTopX
	 *            矩形左上点x坐标
	 * @param rectangleLeftTopY
	 *            矩形左上点y坐标
	 * @param rectangleRightBottomX
	 *            矩形右下点x坐标
	 * @param rectangleRightBottomY
	 *            矩形右下点y坐标
	 * @return 是否相交
	 */
	public static boolean isLineIntersectRectangle(int linePointX1,
			int linePointY1, int linePointX2, int linePointY2,
			int rectangleLeftTopX, int rectangleLeftTopY,
			int rectangleRightBottomX, int rectangleRightBottomY) {

		int lineHeight = linePointY1 - linePointY2;
		int lineWidth = linePointX2 - linePointX1;
		// 计算叉乘
		int c = linePointX1 * linePointY2 - linePointX2 * linePointY1;

		if ((lineHeight * rectangleLeftTopX + lineWidth * rectangleLeftTopY + c >= 0 && lineHeight
				* rectangleRightBottomX + lineWidth * rectangleRightBottomY + c <= 0)
				|| (lineHeight * rectangleLeftTopX + lineWidth
						* rectangleLeftTopY + c <= 0 && lineHeight
						* rectangleRightBottomX + lineWidth
						* rectangleRightBottomY + c >= 0)
				|| (lineHeight * rectangleLeftTopX + lineWidth
						* rectangleRightBottomY + c >= 0 && lineHeight
						* rectangleRightBottomX + lineWidth * rectangleLeftTopY
						+ c <= 0)
				|| (lineHeight * rectangleLeftTopX + lineWidth
						* rectangleRightBottomY + c <= 0 && lineHeight
						* rectangleRightBottomX + lineWidth * rectangleLeftTopY
						+ c >= 0)) {
			if (rectangleLeftTopX > rectangleRightBottomX) {
				int temp = rectangleLeftTopX;
				rectangleLeftTopX = rectangleRightBottomX;
				rectangleRightBottomX = temp;
			}
			if (rectangleLeftTopY < rectangleRightBottomY) {
				int temp = rectangleLeftTopY;
				rectangleLeftTopY = rectangleRightBottomY;
				rectangleRightBottomY = temp;
			}
			if ((linePointX1 < rectangleLeftTopX && linePointX2 < rectangleLeftTopX)
					|| (linePointX1 > rectangleRightBottomX && linePointX2 > rectangleRightBottomX)
					|| (linePointY1 > rectangleLeftTopY && linePointY2 > rectangleLeftTopY)
					|| (linePointY1 < rectangleRightBottomY && linePointY2 < rectangleRightBottomY)) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	public static String splitString(String s, int i, int j) {
		int i_index = -1, j_index = -1;
		for (int k = 0; k < i; k++) {
			i_index = s.indexOf(",", i_index + 1);
		}
		for (int k = 0; k < j; k++) {
			j_index = s.indexOf(",", j_index + 1);
		}
		s = s.substring(i_index + 1, j_index);
		// System.out.println("between"+ i+" and"+ j +"is "+s);
		return s;
	}

	public static String subString(String s, int i) {// sub after NO.i ","
		int i_index = -1;
		for (int k = 0; k < i; k++) {
			i_index = s.indexOf(",", i_index + 1);
		}
		s = s.substring(i_index + 1, s.length());
		// System.out.println("after"+ i+"is "+s);
		return s;
	}

	static String[] produceArray(String beginTime, String endTime, int interval) {// 0530,2300,5
		DecimalFormat sd = new DecimalFormat("00");// 20130605012000,
		int tempEnd;
		int beginHour = Integer.valueOf(beginTime.substring(8, 10));
		int beginMin = Integer.valueOf(beginTime.substring(10, 12));
		int endHour = Integer.valueOf(endTime.substring(8, 10));
		int endMin = Integer.valueOf(endTime.substring(10, 12));
		int interval_min = endHour * 60 + endMin - beginHour * 60 - beginMin;
		int size = interval_min / interval;
		if (interval_min % interval != 0) {
			++size;
		}
		String[] array = new String[size + 1];
		array[0] = beginTime;
		for (int i = 1; i < size; i++) {
			tempEnd = beginHour * 60 + beginMin + interval * i;
			array[i] = sd.format(tempEnd / 60) + sd.format(tempEnd % 60);
			System.out.print(array[i] + "*");
		}
		System.out.println();
		array[size] = endTime;
		return array;
	}

	// 转换标准时间到10字符的Unix时间
	public static String toUnixTime(String local) {
		String unix = "";
		try {
			unix = SDF.parse(local).getTime() / 1000 + "";
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return unix;
	}

	// 转换10字符的Unix时间为14字符的标准时间
	public static String toLocalTime(String unix) {
		Long timestamp = Long.parseLong(unix) * 1000;
		String date = SDF.format(new Date(timestamp));
		return date;
	}

	public static void printMapString(Map<String, Object> map) {
		for (String key : map.keySet()) {
			System.out.println("level 1 -> key = " + key + " and value= "
					+ map.get(key));
		}
	}

	public static void printDoubleMapString(Map<String, Map<String, Object>> map) {
		for (String key : map.keySet()) {
			System.out.println("level 2 -> key = " + key);
			printMapString(map.get(key));
		}
	}

	public static void printDoubleMapInteger(
			Map<String, Map<String, Integer>> map) {
		Map<String, Integer> innerMap;
		for (String key : map.keySet()) {
			System.out.print("网格号：" + " " + key);
			innerMap = map.get(key);
			System.out.println(";对应的路链号个数为： " + " " + innerMap.size());
			// for (String innerkey : innerMap.keySet()) {
			// System.out.println("\t路链号："+" " + innerkey+ ";个数为："+" "
			// + innerMap.get(innerkey));
			// }
		}
	}
}
