package basedeal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class StatisticRoadKind {

	/*统计每年北京路链每个等级有多少条路链
	 * 
	等级代码	内容
	1	高速公路
	2	快速路
	3	国道
	4	主要地方道
	5	一般县道
	6	市镇村道
	8	其他道路
	11	快速路和高速路辅路
	 */
	public static void main(String args[]) throws IOException{
		String inPth = "G:/其他/支持文件/MidPointOfLinkwithRoadKind2010.txt";
		String outPth = "G:/其他/支持文件/NumOfRoadKind2010-20140606.txt";
		String record="";
		String [] items ;
		
		File linkfile = new File(inPth);
		BufferedReader bReader = new BufferedReader(new FileReader(linkfile));
		File wFile = new File(outPth);
		FileWriter fWriter = new FileWriter(new File(outPth));
		Map<String, Integer> kindMap = new TreeMap<String, Integer>();
		Map<String, String> kindMeaningMap = new TreeMap<String, String>();
		int tempCount, allCount = 0;
		//初始化路链含义
		kindMeaningMap.put("01", "高速公路");
		kindMeaningMap.put("02", "快速路");
		kindMeaningMap.put("03", "国道");
		kindMeaningMap.put("04", "主要地方道");
		kindMeaningMap.put("05", "一般县道");
		kindMeaningMap.put("06", "市镇村道");
		kindMeaningMap.put("08", "其他道路");
		kindMeaningMap.put("11", "快速路和高速路辅路");
		//读文件开始统计
		while((record = bReader.readLine())!= null){
			++allCount;
			items = record.split(";");
			System.out.println("items[3]: "+items[3]);
			if(!kindMap.containsKey(items[3])){
				kindMap.put(items[3], 1);
			}else {
				tempCount = kindMap.get(items[3]);
				kindMap.put(items[3], ++tempCount);
			}
			
		}
		bReader.close();
		
		for(String key :kindMap.keySet()){
			int oneNum = kindMap.get(key);
			System.out.println(key+"\t"+kindMeaningMap.get(key)+"\t"+oneNum+"\t"+oneNum/(float)allCount);
			fWriter.write(key+"\t"+kindMeaningMap.get(key)+"\t"+oneNum+"\t"+oneNum/(float)allCount+"\r\n");
		}
		fWriter.write("路链总数： "+allCount+"\r\n");
		fWriter.close();
	}
}
