package Criteria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import base.Constant;
import base.HashMapSortByValue;
import statistic.CDataGrid;

public class DivideGrid {
	
	public static void main(String args[]){
		Map <String,Integer> map = new DivideGrid().divideGrid();
		int i = 0;
		for (String key : map.keySet()) {
			   System.out.println("ID="+(++i)+" and key= "+ key + " and value= " + map.get(key));
		}
	}
	
	public Map <String,Integer> divideGrid(){
		CDataGrid cdg = new CDataGrid();
		Map <String,Integer> map1 = cdg.readGridRecordNumToMap();
		System.out.println(map1.size());
		List<Map.Entry<String, Integer>> infoIds = new HashMapSortByValue().sortByValue(map1);
		System.out.println("allGridSum:"+Constant.allGridSum);
		int avergeSum = Constant.allGridSum/Constant.partitionNum;
		System.out.println("avergeSum:"+avergeSum);
		Map <String,Integer> mapDivide = new HashMap <String,Integer>();
		List<String> gridList = new ArrayList<String>();
		String key;int count=0,sum=0,temp;
		for (int i = 0; i < infoIds.size(); i++) {
			Entry<String, Integer> ent = infoIds.get(i);
			key = ent.getKey();
			temp = ent.getValue();
			sum += temp;
			if(sum< avergeSum){
				gridList.add(key);
				System.out.println("key:"+key+"\ttemp:"+temp+"\tsum:"+sum);
			}
			else if(sum>=avergeSum &&(((float)sum)/avergeSum) <1.2){
				System.out.println(i);
				System.out.println("key:"+key+"\ttemp:"+temp+"\tsum:"+sum);
				System.out.println("gridList:"+gridList.size());
				gridList.add(key);
				for(int m=0;m<gridList.size();m++){mapDivide.put(gridList.get(m), count);}
				++count;
				gridList.clear();
				sum = 0;
			}
			else {
				System.out.println("gridList:"+(gridList.size()));
				for(int m =0;m<gridList.size();m++){mapDivide.put(gridList.get(m), count);}
				++count;
				sum = infoIds.get(i).getValue();
				gridList.clear();
				gridList.add(key);
			}
		}
		System.out.println("gridList:"+gridList.size());
		for(int m=0;m<gridList.size();m++){mapDivide.put(gridList.get(m), count);}
		return mapDivide;
	}
	
}
