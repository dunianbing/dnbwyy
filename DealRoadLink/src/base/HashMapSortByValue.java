package base;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HashMapSortByValue {

	public static void main(String args[]){
	}
	
	//对hashmap按照value进行排序<String,Integer>
	public List<Map.Entry<String, Integer>>  sortByValue(Map<String, Integer> map) {
		
		List<Map.Entry<String, Integer>> infoIds = new ArrayList<Map.Entry<String, Integer>>(
				map.entrySet());
//		System.out.println("--------------排序前--------------");
//		for (int i = 0; i < infoIds.size(); i++) {
//			String id = infoIds.get(i).toString();
//			System.out.println(id);
//		}
		// 排序
		Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1,
					Map.Entry<String, Integer> o2) {
				return (o1.getValue() - o2.getValue());
			}
		});
//		System.out.println("--------------排序后--------------");
//		for (int i = 0; i < infoIds.size(); i++) {
//			Entry<String, Integer> ent = infoIds.get(i);
//			System.out.println(i+"="+ent.getKey() + "=" + ent.getValue());
//
//		}
		return infoIds;
	}
}
