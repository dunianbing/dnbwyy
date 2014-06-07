package statistic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import base.Constant;
import base.GPSInfo;
import base.GridInfo;

public class GridReadToMemory {
	/**
	 * 读取北京市的网格，共213个，网格ID是6位
	 * 
	 * @param args
	 * @throws IOException 
	 * 
	 */
	public static void main(String[] args) throws IOException {
		Date startTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		GridReadToMemory grt = new GridReadToMemory();
		GridInfo [][] g = grt.readGridToDArray();//将网格读到二维数组GridInfo[20][17]
		grt.printGridDArray(g);
		// new GridReadToMemory().readGridToArray();//将网格读到一维数组GridInfo[213]中
		GPSInfo g605672 = Constant.mapGrid.get("605672").getGPSLD();
		GPSInfo g615602 = Constant.mapGrid.get("615602").getGPSLD();
		GPSInfo g605673 = Constant.mapGrid.get("605673").getGPSLD();
		GPSInfo g615603 = Constant.mapGrid.get("615603").getGPSLD();
		System.out.print("\r\ng605672: ");
		g605672.printGPS();
		System.out.print("\r\ng605673: ");
		g605673.printGPS();
		System.out.print("\r\ng615602: ");
		g615602.printGPS();
		System.out.print("\r\ng615603: ");
		g615603.printGPS();
		System.out.println();
		Date endTime = new Date();
		System.out.println("开始时间:" + sdf.format(startTime));
		System.out.println("结束时间:" + sdf.format(endTime));
	}

	/* read 213 grids to array[213], sequence as readed files */
	public GridInfo[] readGridToArray() {
		String record;
		String gridID;
		String item[] = new String[5];// 存储带有经纬度的每行
		GridInfo ginfo;
		GridInfo[] allGrid = new GridInfo[213];
		GPSInfo ld, ru;
		int i = 0;
		try {
			BufferedReader brMid = new BufferedReader(new FileReader(
					Constant.mid_grid_bj));
			BufferedReader brMif = new BufferedReader(new FileReader(
					Constant.mif_grid_bj));
			while ((record = brMif.readLine()) != null) {
				if (record.startsWith("Rect")) {
					gridID = brMid.readLine().substring(1, 7);
					item = record.split(" ");
					ld = new GPSInfo(new Double(item[1]), new Double(item[2]));
					ru = new GPSInfo(new Double(item[3]), new Double(item[4]));
					ginfo = new GridInfo(gridID, ld, ru);
					allGrid[i] = ginfo;
					++i;
				} else {
					continue;
				}
			}
			brMid.close();
			brMif.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return allGrid;
	}

	/*
	 * 读取网格信息，保存到二维数组中，20*17=340个元素，其中213个有对应的GridInfo，其他为空指针null
	 * 并且将网格信息保存到了map<GridID,GridInfo>中，可以根据GridID获取网格信息
	 */
	public GridInfo[][] readGridToDArray() {
		GridInfo[][] dArrayGrid = new GridInfo[20][17];
		String record;
		String gridID;
		String item[] = new String[5];// 存储带有经纬度的每行
		GridInfo ginfo;
		GPSInfo ld, ru;
		int i = 0, rowNo, columnNo;
		try {
			BufferedReader brMid = new BufferedReader(new FileReader(
					Constant.mid_grid_bj));
			BufferedReader brMif = new BufferedReader(new FileReader(
					Constant.mif_grid_bj));
			while ((record = brMif.readLine()) != null) {
				if (record.startsWith("Rect")) {
					gridID = brMid.readLine().substring(1, 7);
					item = record.split(" ");
					ld = new GPSInfo(new Double(item[1]), new Double(item[2]));// leftdown
																				// GPS
																				// point
					ru = new GPSInfo(new Double(item[3]), new Double(item[4]));// rightup
																				// GPS
																				// point
					ginfo = new GridInfo(gridID, ld, ru);
					rowNo = returnAltNo((int) (new Double(item[2]) * 1000000));
					columnNo = returnLonNo((int) (new Double(item[1]) * 1000000));
					dArrayGrid[rowNo][columnNo] = ginfo;
//					System.out.println("No:" + (++i) + "\trowNo:" + rowNo
//							+ "\tcolumnNo:" + columnNo);
					Constant.mapGrid.put(gridID, ginfo);
				} else {
					continue;
				}
			}
			brMid.close();
			brMif.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dArrayGrid;
	}

	int returnLonNo(int leftdown_LON) {
		int temp = (int) (((leftdown_LON - Constant.longitude_MIN)) / Constant.interval_LON);
		// System.out.println("LON: "+temp+"\tleftdown_LON: "+leftdown_LON);
		return temp;
	}

	int returnAltNo(int leftdown_LAT) {
		int temp = (int) (((leftdown_LAT - Constant.latitude_MIN)) / Constant.interval_LAT);
		// System.out.print("LAT: "+temp+"\tleftdown_LAT: "+leftdown_LAT+"\t");
		return temp;
	}

	public void printGridDArray(GridInfo[][] g) throws IOException {
		Map <String,Integer> map = new CDataGrid().readGridRecordNumToMap();
		String gridID;
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 17; j++) {
				if (g[i][j] == null){
					System.out.print("\t");
				}else {
					gridID = g[i][j].getGridID();
					System.out.print(gridID+":"+map.get(gridID)+"\t");
					System.out.print(map.get(gridID)+"\t");
				}
			}
			System.out.println();
		}
	}

	/* print infos of all grids */
	public void printGridArray(GridInfo[] gArray) {

		for (int j = 0; j < 213; j++) {
			System.out.print((j + 1) + "--->");
			gArray[j].printGridInfo();
		}
	}
}
