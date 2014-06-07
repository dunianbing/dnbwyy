package basedeal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RoadLinkPruning {
	/*
	 * 获得五环内的路链（大致的）
	 * Input：路链中点坐标文件MidPointOfLink2012.txt
	 * Output:五环内的路链中点坐标文件MidPointOfLink2012-五环内.txt
	 * Assist:百度地图选取的五环范围
	 * 		左上：116.209661 40.045082
	 * 		右上：116.553459 40.045082
	 * 		左下：116.209661 39.766571
	 * 		右下：116.553459 39.766571
	 * 根据这个范围，保留下来的数据是原来的42%
	 * */
	public static void pruningMidPoint(String inPth ,String outPth)throws IOException{
		String record="";
		String [] items ,cordinates;
		double x,y;
		File linkfile = new File(inPth);
		BufferedReader bReader = new BufferedReader(new FileReader(linkfile));
		FileWriter fWriter = new FileWriter(new File(outPth));
		int selectedCount = 0, allCount = 0;
		
		//读文件开始过滤
		while((record = bReader.readLine())!= null){//ID;网格号;路链号;路链等级;路链中点坐标
			++allCount;
			items = record.split(";");
			
			cordinates = items[4].split(" ");
			x = Double.valueOf(cordinates[0]);
			y = Double.valueOf(cordinates[1]);
			if(x > 116.209661 && x < 116.553459 && y > 39.766571 && y <40.045082){
				fWriter.write(record+"\r\n");
				++selectedCount;
			}
		}
		bReader.close();
		fWriter.close();
		System.out.println("数据保留比例： "+selectedCount/(double)allCount);
	}

	
	public static void main(String args[]) throws IOException {
		//抽取MidPointOfLink2012五环内的数据
//		String inPth = "G:/其他/支持文件/MidPointOfLink2012.txt";
//		String outPth = "G:/其他/支持文件/MidPointOfLink2012-五环内.txt";
//		pruningMidPoint(inPth, outPth);
		//抽取GridLink2012五环内的数据
		String inPth1 = "G:/其他/支持文件/MidPointOfLink2012-五环内.txt";
		String inPth2 = "G:/其他/支持文件/GridLink2012.txt";
		String out = "G:/其他/支持文件/GridLink2012-五环内.txt";
		pruningGridLink(inPth1,inPth2,out);
	}
	public static void pruningGridLink(String inPth1, String inPth2,String outPth) throws IOException{
		//读取五环内的中点数据，作为标准参考，对于GridLink中那些不在五环内中点数据集的数据舍弃
		String record="";
		String [] items ;
		String gridID,linkID;
		int allCount=0, targetCount = 0;
		File linkfile = new File(inPth1);
		BufferedReader bReader = new BufferedReader(new FileReader(linkfile));
		HashMap<String,HashSet<String>> targetLink = new HashMap<String,HashSet<String>>();
		while((record = bReader.readLine())!= null){
			items = record.split(";");
			gridID = items[1];
			linkID = items[2];
			if(!targetLink.containsKey(gridID)){
				HashSet<String> newSet = new HashSet<String>();
				newSet.add(linkID);
				targetLink.put(gridID, newSet);
			}else{
				HashSet<String> thisSet = targetLink.get(gridID);
				thisSet.add(linkID);
				targetLink.put(gridID, thisSet);
			}
		}
		bReader.close();
		//读取GridLink数据，并每行依次判断，是否舍弃
		File linkfile2 = new File(inPth2);
		BufferedReader bReader2 = new BufferedReader(new FileReader(linkfile2));
		FileWriter fWriter = new FileWriter(new File(outPth));
		while((record = bReader2.readLine())!= null){
			++allCount;
			items = record.split(";");
			gridID = items[1];
			linkID = items[2];
			if(targetLink.containsKey(gridID)){
				HashSet<String> thisSet = targetLink.get(gridID);
				if(thisSet.contains(linkID)){
					fWriter.write(record+"\r\n");
					++targetCount;
				}
			}
		}
		bReader2.close();
		fWriter.close();
		System.out.println("数据保留比例： "+targetCount/(double)allCount);
	}
}
