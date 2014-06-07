package basedeal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

public class DealRoadLink {

	/*北京市道路路链2012年，产生每条路链的中点（作为Voronoi图的中心）
	 * */
	public static void main(String args[]) throws IOException{
		String linkpth = "G:/其他/支持文件/GridLink2012-五环内.txt";
		String midPointPth = "G:/其他/支持文件/MidPointOfLink2012-五环内.txt";
		String record="";
		String [] items ,points;
		int allCount = 0;
		double minLon, alt_minLon, maxLon, alt_maxLon, midLon, midAlt;
		TreeMap<Double, Double> gpstreePerLink = new TreeMap<Double,Double>();
		File linkfile = new File(linkpth);
		BufferedReader bReader = new BufferedReader(new FileReader(linkfile));
		File wFile = new File(midPointPth);
		if(!wFile.getParentFile().exists()){
			wFile.mkdirs();
		}
		FileWriter fWriter = new FileWriter(new File(midPointPth));
		while((record = bReader.readLine())!= null){
			++allCount;
			items = record.split(";");//0:ID;1:gridID;2:linkID;3:GPS(不带路链等级的)
			points = items[4].split(",");//0:ID;1:gridID;2:linkID;3:RoadKind;4:GPS（带有路链等级的）
			for(String gps:points){
				double lon = Double.parseDouble(gps.split(" ")[0]);
				double alt = Double.parseDouble(gps.split(" ")[1]);
				gpstreePerLink.put(lon, alt);
			}
			//得到最小经度值对应点
			minLon = gpstreePerLink.firstKey();
			alt_minLon = gpstreePerLink.get(minLon);
			//得到最大经度值对应点			
			maxLon = gpstreePerLink.lastKey();
			alt_maxLon = gpstreePerLink.get(maxLon);
			//得到该路链的中点
			midLon = (minLon + maxLon)/2;
			midAlt = (alt_minLon +alt_maxLon)/2;
			fWriter.write(allCount+";"+items[1]+";"+items[2]+";"+items[3]+";"+midLon+" "+midAlt+"\r\n");
			//ID;网格号;路链号;路链等级;路链中点坐标
			gpstreePerLink.clear();
		}
		bReader.close();
		fWriter.close();
	}
}
