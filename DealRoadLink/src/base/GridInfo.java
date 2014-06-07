package base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GridInfo {
	private String GridID; 
	private GPSInfo leftDown ;
	private GPSInfo rightUp ;
	private int recordSum = 0;
	public GridInfo(String id,GPSInfo ld,GPSInfo ru){
		this.GridID = id;
		this.leftDown = ld;
		this.rightUp = ru;
	}
	public GridInfo(String id,GPSInfo ld,GPSInfo ru,int sum){
		this.GridID = id;
		this.leftDown = ld;
		this.rightUp = ru;
		this.recordSum = sum;
	}
	public GridInfo(){
		this.GridID = null;
		this.leftDown = null;
		this.rightUp = null;
		this.recordSum = 0;
	}
	public String getGridID(){
		return this.GridID;
	}
	public void setGridID(String id){
		this.GridID = id;
	}
	public GPSInfo getGPSLD(){
		return this.leftDown;
	}
	public void setGPSLD(GPSInfo ld){
		this.leftDown = ld;
	}
	public GPSInfo getGPSRU(){
		return this.rightUp;
	}
	public void setGPSRU(GPSInfo ru){
		this.rightUp = ru;
	}
	public int getRecordSum(){
		return this.recordSum;
	}
	public void setRecordSum(int sum){
		this.recordSum = sum;
	}
	
	public boolean containGPS(GPSInfo a){
		if(a.getDoubleX()>=this.leftDown.getDoubleX()&&a.getDoubleY()>this.leftDown.getDoubleY())
			if(a.getDoubleX()<this.rightUp.getDoubleX()&&a.getDoubleY()<=this.rightUp.getDoubleY())
				return true;
		return false;
	}
	public void printGridInfo(){
		System.out.print("GridID:"+this.GridID+"\t");
		this.leftDown.printGPS();
		System.out.print("\t");
		this.rightUp.printGPS();
		System.out.println();
	}
	
	/*将符合日期范围内的网格内数据条数读入map<String,List<String>>中，key为网格号GridID，value为"20120601_数量"*/
	public void readGridStatic() throws IOException {
		BufferedReader brSta = null;
		String record,date;
		String item[];
		File dir_gridStatistic[]= new File(Constant.Pth_gridStatistic).listFiles();
		List<String> tempList ;
		for(File file:dir_gridStatistic){
			date =file.getName().substring(0,8);
			if(date.compareTo(Constant.statistic_beginDate)>=0&&date.compareTo(Constant.statistic_endDate)<=0){
				brSta = new BufferedReader(new FileReader(
					file));
				while((record=brSta.readLine())!=null){
					item = record.split(",");
					if(item.length==3){//item[1]:GridID;item[2]:recordSum
						if(Constant.mapRecordSum.containsKey(item[1])){
							tempList = Constant.mapRecordSum.get(item[1]);
						}else{
							tempList = new ArrayList<String>();
						}
						tempList.add(date+"_"+item[2]);
						Constant.mapRecordSum.put(item[1], tempList);
					}
				}
				brSta.close();
			}
		}
	}
	
	/*对List<String>进行排序*/
	public void SortList(List<String> list){
		ComparatorUser com = new ComparatorUser();
		Collections.sort(list,com);
	}
}
class ComparatorUser implements Comparator<Object>{
	public int compare(Object arg0,Object arg1){
		String value1[] =((String)arg0).split("_");
		String value2[] =((String)arg1).split("_");
		return value1[0].compareTo(value2[0]);
	}
} 
