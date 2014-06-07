package statistic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CountGridLinkSum {
	/**
	 * 2013年11月15日
	 * 统计2010年的路网数据，有多少网格，多少路链，多少点
	 * 
	 * @param args
	 */
	public static String gridlinkpth = "F:/GridLink.txt";

	public static void main(String[] args) throws NumberFormatException,
			IOException {
		Date startTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		File file = new File(gridlinkpth);
		BufferedReader br;
		String record = "";
		Map<String, Map<String, Integer>> map = new HashMap<String, Map<String, Integer>>();
		Map<String, Integer> linkmap ;
		br = new BufferedReader(new FileReader(file));
		while ((record = br.readLine()) != null) {
			String item[] = record.split(";");
			String gridNo = item[1];
			String linkNo = item[2];
			String point[] = item[3].split(",");
			if(map.containsKey(gridNo)){
				linkmap = map.get(gridNo);
				if(linkmap.containsKey(linkNo)){
					System.out.println("文件出错！");
				}else{
					linkmap.put(linkNo, point.length);
				}
			}else {
				linkmap = new HashMap<String,Integer>();
				linkmap.put(linkNo, point.length);
			}
			map.put(gridNo, linkmap);
		}
		br.close();
		System.out.println("2010年北京市路网的网格数量："+map.size());
		for(Map.Entry<String, Map<String, Integer>> entry : map.entrySet()){
			String gridno = entry.getKey();
			Map<String, Integer> link_map= entry.getValue();
			int count = 0;
			for(String linkno:link_map.keySet()){
				count +=link_map.get(linkno);
				System.out.println("网格号："+gridno+"\t路链号："+linkno+"\t点的数量："+link_map.get(linkno));
			}
			System.out.println("##### 网格号："+gridno+"\t路链数："+link_map.size()+"\t点的总数："+count);
			count = 0;
		}
		Date endTime = new Date();
		System.out.println("开始时间:" + sdf.format(startTime));
		System.out.println("结束时间:" + sdf.format(endTime));
	}
}
