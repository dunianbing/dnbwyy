package statistic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class CDataCarID {

	/**统计2011年JYJ的CData数据，每个车辆号有多少条数据
	 * @param args
	 */
	public static String inPth_cdata_dir="J:/Data2011/cdata6/20110601";
	public static void main(String[] args) throws NumberFormatException, IOException{
		Date startTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		File cdata_dir_file = new File(inPth_cdata_dir);
		File cdata_files[] = cdata_dir_file.listFiles();
		BufferedReader br ;
		String []itemCData = new String [4];
		String record = "",vehicleID;
		int temp;
		StringTokenizer st ;
		Map<String,Integer> map = new HashMap<String,Integer>();
		for (File file:cdata_files){
			br = new BufferedReader(new FileReader(file));
			while ((record = br.readLine()) != null) {
				st = new StringTokenizer(record,",");
				if(st.countTokens()==13){
					for(int i=0;i<3;i++){//第三列是车辆号
						itemCData[i]=st.nextToken();
					}
				}else{
					continue;
				}
				vehicleID = itemCData[2];
				if (map.containsKey(vehicleID)) {
					temp = map.get(vehicleID);
					map.put(vehicleID, ++temp);
				} else {
					map.put(vehicleID, 0);//map的key是车辆号，value是车辆数
				}
			}
		}
		System.out.println(cdata_dir_file.getName()+":");
		int j =0; String s;
		Set<String> key = map.keySet();
	        for (Iterator<String> it = key.iterator(); it.hasNext();) {
	            s = (String) it.next();
//	            System.out.println((++j)+"--->车辆号:"+s+"数据量："+map.get(s));
	            System.out.println((++j)+","+s+","+map.get(s));
	        }
		Date endTime = new Date();
		System.out.println("开始时间:" + sdf.format(startTime));
		System.out.println("结束时间:" + sdf.format(endTime));
	}
}
