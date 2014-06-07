package statistic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class CDataHour {

	/**统计JYJ的CData数据，每个小时有多少条数据
	 * @param args
	 */
	public static String inPth_cdata_dir="J:/Data2011/cdata6/20110603";
	public static void main(String[] args) throws NumberFormatException, IOException{
		Date startTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		File cdata_dir_file = new File(inPth_cdata_dir);
		File cdata_files[] = cdata_dir_file.listFiles();
		BufferedReader br ;
		String []itemCData = new String [4];
		int hour_count[] = new int[24];
		String record = "";
		int hour;
		StringTokenizer st ;
		for (int i =0;i<24;i++){
			hour_count[i]=0;
//			System.out.print(hour_count[i]+"*");
		}
		for (File file:cdata_files){
			br = new BufferedReader(new FileReader(file));
			while ((record = br.readLine()) != null) {
				st = new StringTokenizer(record,",");
				if(st.countTokens()==13){
					for(int i=0;i<4;i++){
						itemCData[i]=st.nextToken();
					}
				}else{
					continue;
				}
				hour = Integer.valueOf(itemCData[3].substring(8,10));
				++hour_count[hour];
			}
			br.close();
		}
		System.out.println(cdata_dir_file.getName()+":");
		for (int i =0;i<24;i++){
			System.out.println("hour "+(i+1)+":"+hour_count[i]);
		}
		Date endTime = new Date();
		System.out.println("开始时间:" + sdf.format(startTime));
		System.out.println("结束时间:" + sdf.format(endTime));
	}
}
