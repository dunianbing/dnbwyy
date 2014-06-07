package statistic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import base.Constant;
import base.GPSInfo;
import base.GridInfo;

public class CDataGrid {
	/**
	 * 统计JYJ的CData数据，每个网格的数据量，网格采用北京2.5万接图表
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String args[]) throws NumberFormatException, IOException{
		CDataGrid cdg = new CDataGrid();
//		cdg.gridRecordNumByDay();
		cdg.readGridRecordNumToMap();
	}
	/*
	 * 统计2011年JYJ公司的CData数据，读取每一天的数据，统计每个网格内的数据条数，网格使用北京2.5万接图表，共213个网格
	 * */
	public void gridRecordNumByDay() throws NumberFormatException,
			IOException {
		Date startTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		File cdata_dir_file = new File(Constant.inPth_cdata);
		File cdata_date[] = cdata_dir_file.listFiles();
		String date;
		BufferedReader br;
		GridInfo[] allGrid = new GridInfo[213];
		allGrid = new GridReadToMemory().readGridToArray();
//		System.out.println("allGrid--->1:"+allGrid[0].getGridID()+"\tallGrid--->213:"+allGrid[212].getGridID());
		GPSInfo ginfo;
		String[] itemCData = new String[8];
		String record = "", gridID;
		int tempCount;
		StringTokenizer st;
		double gpsX = .7d, gpsY = .7d;
		Map<String, Integer> map = new TreeMap<String, Integer>();
		File out = new File(Constant.Pth_gridStatistic);//输出文件地址
		if(!out.exists())
			out.mkdirs();
		for(File oneDir:cdata_date){
			date = oneDir.getName();
			if(date.compareTo(Constant.statistic_beginDate)>=0&&date.compareTo(Constant.statistic_endDate)<=0){
				File cdata_files[] = oneDir.listFiles();
				for (File file : cdata_files) {
					br = new BufferedReader(new FileReader(file));
					while ((record = br.readLine()) != null) {
						st = new StringTokenizer(record, ",");
						if (st.countTokens() == 13) {
							for (int i = 0; i < 8; i++) {// 第三列是车辆号.5-8列是两个经纬度
								itemCData[i] = st.nextToken();
							}
						} else {
							continue;
						}
						gpsX = Double.valueOf(itemCData[6]) / 3600 / 1024;
						gpsY = Double.valueOf(itemCData[7]) / 3600 / 1024;
						ginfo = new GPSInfo(gpsX, gpsY);
						for (int k = 0; k < 213; k++) {
							if (allGrid[k].containGPS(ginfo)) {
								gridID = allGrid[k].getGridID();
//								System.out.println("gridID："+gridID);
								if (map.containsKey(gridID)) {
									tempCount = map.get(gridID);
									map.put(gridID, ++tempCount);
								} else {
									map.put(gridID, 1);
								}
							}
						}
					}
					br.close();
				}
				//写文件
				System.out.println("date:"+date);
				BufferedWriter bw = new BufferedWriter(new FileWriter(out.getAbsolutePath()+"/"+date+".txt")); 
				int j = 0,k=0,temp,allSum=0;
				String s;
				Set<String> key = map.keySet();
				for (Iterator<String> it = key.iterator(); it.hasNext();) {
					s = (String) it.next();
					temp = map.get(s);
					allSum +=temp;
					System.out.println((++k) + "," + s + "," + temp);// (++j)+"--->网格号:"+s+"数据量："+map.get(s);
					bw.write((++j) + "," + s + "," + temp+"\r\n");
				}
				bw.write("allSum:"+allSum);
				bw.close();
				map.clear();
				System.out.println("numbers of record:"+allSum);
			}
		}
		
		Date endTime = new Date();
		System.out.println("开始时间:" + sdf.format(startTime));
		System.out.println("结束时间:" + sdf.format(endTime));
	}
	/*
	 * 计算统计的日期范围内，各个网格内每天平均的数据条数，独到Map中
	 * */
	public Map<String,Integer> readGridRecordNumToMap() {
		Map<String,Integer> map = new HashMap<String,Integer>();
		Map<String,List<Integer>> mapList = new HashMap<String,List<Integer>>();
		File cdataGridCount = new File(Constant.Pth_gridStatistic);//输入文件地址
		String date,record;
		String item[];
		BufferedReader br = null;
		List<Integer> temp;
		for(File oneFile:cdataGridCount.listFiles()){
			date = oneFile.getName().substring(0,8);
			if(date.compareTo(Constant.statistic_beginDate)>=0&&date.compareTo(Constant.statistic_endDate)<=0){
				try {
					br = new BufferedReader(new FileReader(oneFile));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				try {
					while((record = br.readLine())!=null){
						item = record.split(",");
						if(item.length<3)
							continue;
						if(mapList.containsKey(item[1])){
							temp = mapList.get(item[1]);
							temp.add(Integer.valueOf(item[2]));
							mapList.put(item[1], temp);
						}else{
							temp = new ArrayList<Integer>();
							temp.add(Integer.valueOf(item[2]));
							mapList.put(item[1], temp);
						}
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		String s;int sum=0,average,allSum = 0;
		Set<String> key = mapList.keySet();
		for (Iterator<String> it = key.iterator(); it.hasNext();) {
			s = (String) it.next();
			temp = mapList.get(s);
			for (int k =0;k<temp.size();k++){sum +=temp.get(k); }
			average = sum/temp.size();
			allSum+= average;
			if(!map.containsKey(s)){
				map.put(s, average);
//				System.out.println("GridId:"+s+"\t"+average);
			}
			sum = 0;average = 0;
		}
		Constant.allGridSum = allSum;
		return map;
	}
	
}
