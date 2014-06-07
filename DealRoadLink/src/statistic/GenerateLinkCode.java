package statistic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import base.Constant;
import base.GridInfo;

public class GenerateLinkCode {
	/**
	 * 
	 * 新建：2013年11月15日 
	 * 功能：读取北京路网数据（.xml），网格号、路链号、采样点经纬度，进行编码，将每个大网格划分为100*100的小网格。
	 * gridLinkCode2版本：存在10030编码，即index有等于100的情况
	 * gridLinkCode3版本：有index为100的做减1操作
	 * gridLinkCode2012版本：带有路链等级的版本
	 * gridLinkCode2012-五环内：五环内带路链等级版本
	 * @param args
	 */
	public static String inpth = "G:/其他/支持文件/GridLink2012-五环内.txt";
	public static String outpth = "G:/其他/支持文件/gridLinkCode2012-五环内";

	public static void generateLinkCode() throws NumberFormatException,
			IOException {
		Date startTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		DecimalFormat dFormat = new DecimalFormat("00");
		File infile = new File(inpth);
		BufferedReader br;
		String record = "";
		//存储各个网格号对应的输出writer
		Map<String, BufferedWriter> gridLinkCode = new HashMap<String, BufferedWriter>();
		br = new BufferedReader(new FileReader(infile));
		File outDir = new File(outpth);
		String filePath, writeline;
		BufferedWriter bWriter;
		@SuppressWarnings("unused")
		GridInfo[][] g = new GridReadToMemory().readGridToDArray();
		double x, y;
		if (!outDir.exists()) {
			outDir.mkdirs();
			System.out.println("新建路径成功！");
		}
		while ((record = br.readLine()) != null) {
			//GridLinkwithRoadKind2012.txt
			//0:ID;1:网格号;2：路链号：3：路链等级；4：一组坐标
			Set<String> linkcode = new HashSet<String>();
			String item[] = record.split(";");
			String gridNo = item[1];
			String linkNo = item[2];
			if (gridLinkCode.containsKey(gridNo)) {
				bWriter = gridLinkCode.get(gridNo);
			} else {
				filePath = outpth + "/" + gridNo + ".txt";
				File file = new File(filePath);
				bWriter = new BufferedWriter(new FileWriter(file));
				gridLinkCode.put(gridNo, bWriter);
			}
			if (Constant.mapGrid.containsKey(gridNo)) {
				x = Constant.mapGrid.get(gridNo).getGPSLD().getDoubleX();
				y = Constant.mapGrid.get(gridNo).getGPSLD().getDoubleY();
			} else {
				System.out.println("当前网格在网格map中不存在：" + gridNo);
				continue;
			}
			String point[] = item[4].split(",");
			String gps1[], gps2[], code;
			writeline = linkNo + ";" +item[3]+";";
			for (int i = 0; i < point.length - 1; i++) {
				gps1 = point[i].split(" ");
				gps2 = point[i + 1].split(" ");
				double o1_x = Double.valueOf(gps1[0]);
				double o1_y = Double.valueOf(gps1[1]);
				double o2_x = Double.valueOf(gps2[0]);
				double o2_y = Double.valueOf(gps2[1]);

				int indexx1 = (int) ((o1_x - x) * 1000000 / Constant.inner_inter_LON);// 经度的index,左下角为坐标原点
				int indexy1 = (int) ((o1_y - y) * 1000000 / Constant.inner_inter_ALT);// 纬度的index，
				int indexx2 = (int) ((o2_x - x) * 1000000 / Constant.inner_inter_LON);// 经度的index,左下角为坐标原点
				int indexy2 = (int) ((o2_y - y) * 1000000 / Constant.inner_inter_ALT);// 纬度的index，
				if(indexx1==100) --indexx1;
				if(indexy1==100) --indexy1;
				if(indexx2==100) --indexx2;
				if(indexy2==100) --indexy2;
				if(linkNo.compareTo("00389")==0)
				System.out.println("o1_x: "+o1_x+"\to1_y: "+o1_y+"\to2_x: "+o2_x+"\to2_y"+o2_y+"\t"+indexx1+"\tx: "+x);
				code = dFormat.format(indexx1)+dFormat.format(indexy1);
				if (!linkcode.contains(code)) {
					linkcode.add(code);
				}
				code = dFormat.format(indexx2)+dFormat.format(indexy2);
				if (!linkcode.contains(code)) {
					linkcode.add(code);
				}
				//判断路链采样点之间的index是不是也在区域内
				for (int m = indexx1; m <= indexx2; m++) {
					for (int n = indexy1; n <= indexy2; n++) {
						double mm = ((double) m * Constant.inner_inter_LON
								/ 1000000 + x);
						double mm1 = ((double) (m+1) * Constant.inner_inter_LON
								/ 1000000 + x);
//						System.out.print("mm: "+mm+"\tmm1: "+mm1+"\t");
						double nn = ((double) n * Constant.inner_inter_ALT
								/ 1000000 + y);
						double nn1 = ((double) (n+1) * Constant.inner_inter_ALT
								/ 1000000 + y);
//						System.out.println("nn: "+nn+"\tnn1: "+nn1+"\t");
						if (isLinedoubleersectRectangle(o1_x, o1_y, o2_x, o2_y,
								mm, nn, mm1, nn1)) {
							code = dFormat.format(m) + dFormat.format(n);
							if (!linkcode.contains(code)) {
								linkcode.add(code);
							}
						}
					}
				}
			}
			Iterator<String> itemcode = linkcode.iterator();
			while (itemcode.hasNext()) {
				writeline += itemcode.next() + ",";
			}
			writeline = writeline.substring(0,writeline.length()-1)+"\r\n";
			bWriter.write(writeline);
			linkcode.clear();
		}
		br.close();
		for (String gridno : gridLinkCode.keySet()) {
			gridLinkCode.get(gridno).close();
		}
		g = null;
		Date endTime = new Date();
		System.out.println("开始时间:" + sdf.format(startTime));
		System.out.println("结束时间:" + sdf.format(endTime));
	}

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
	public static boolean isLinedoubleersectRectangle(double linePodoubleX1,
			double linePodoubleY1, double linePodoubleX2,
			double linePodoubleY2, double rectangleLeftTopX,
			double rectangleLeftTopY, double rectangleRightBottomX,
			double rectangleRightBottomY) {

		double lineHeight = linePodoubleY1 - linePodoubleY2;
		double lineWidth = linePodoubleX2 - linePodoubleX1;
		// 计算叉乘
		double c = linePodoubleX1 * linePodoubleY2 - linePodoubleX2
				* linePodoubleY1;

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
				double temp = rectangleLeftTopX;
				rectangleLeftTopX = rectangleRightBottomX;
				rectangleRightBottomX = temp;
			}
			if (rectangleLeftTopY < rectangleRightBottomY) {
				double temp = rectangleLeftTopY;
				rectangleLeftTopY = rectangleRightBottomY;
				rectangleRightBottomY = temp;
			}
			if ((linePodoubleX1 < rectangleLeftTopX && linePodoubleX2 < rectangleLeftTopX)
					|| (linePodoubleX1 > rectangleRightBottomX && linePodoubleX2 > rectangleRightBottomX)
					|| (linePodoubleY1 > rectangleLeftTopY && linePodoubleY2 > rectangleLeftTopY)
					|| (linePodoubleY1 < rectangleRightBottomY && linePodoubleY2 < rectangleRightBottomY)) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	public static void main(String args[]) {
		new GenerateLinkCode();
		try {
			GenerateLinkCode.generateLinkCode();
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
}
