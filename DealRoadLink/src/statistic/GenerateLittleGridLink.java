package statistic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class GenerateLittleGridLink {
	/**
	 * 
	 * 新建：2013年11月19日
	 * 功能：读取由GenerateLinkCode类处理产生的gridlink3文件，统计每个大网格下100*100小网格对应的路链号有哪些。
	 * 读取每个大网格内的路链对应哪些小网格，得到每个大网格下每个小网格对应哪些路链号
	 * 读入文件格式：文件名：网格号.txt
	 * 			0:路链号（分号分隔）
	 * 			1：路链等级
	 * 			2：小网格编码（一组，逗号分隔）
	 * 输出文件格式：文件名：网格号.txt
	 * 			0：小网格编码（分号分隔）
	 * 			1：路链号（一组，逗号分隔）
	 * @param args
	 */
	public static String inpth = "G:/其他/支持文件/gridLinkCode2012-五环内";
	public static String outpth = "G:/其他/支持文件/linkingrid2012-五环内";

	public static void generateLittlegridlink() throws NumberFormatException,
			IOException {
		Date startTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		
		File outDir = new File(outpth);
		String filePath, writeline="";
		BufferedWriter bWriter;
		if (!outDir.exists()) {
			outDir.mkdirs();
			System.out.println("新建路径成功！");
		}
		File infile = new File(inpth);
		if (!infile.exists()) {
			System.out.print("地址不正确！");
			return;
		}
		for (File afile : infile.listFiles()) {
			Map<String, List<String>> gridlinkmap = new TreeMap<String, List<String>>();
			String filename = afile.getName();// filename为网格号
			filePath = outpth + "/" + filename ;
			File file = new File(filePath);
			bWriter = new BufferedWriter(new FileWriter(file));
			BufferedReader br;
			String record = "";
			br = new BufferedReader(new FileReader(afile));
			List<String> tList;
			while ((record = br.readLine()) != null) {
				String item[] = record.split(";");
				String linkNo = item[0];
				String codes[]=item[2].split(",");
				for(int k =0;k<codes.length;k++){
					if(gridlinkmap.containsKey(codes[k])){
						tList = gridlinkmap.get(codes[k]);
						
					}else {
						tList = new ArrayList<String>();
					}
					tList.add(linkNo);
					gridlinkmap.put(codes[k], tList);
				}
			}
			br.close();
			for(String code:gridlinkmap.keySet()){
				tList = gridlinkmap.get(code);
				writeline +=code+";";
				for(int m =0;m<tList.size();m++){
					writeline+=tList.get(m)+",";
				}
				bWriter.write(writeline.substring(0,writeline.length()-1)+"\r\n");
				writeline="";
			}
			gridlinkmap.clear();
			bWriter.close();
		}
		Date endTime = new Date();
		System.out.println("开始时间:" + sdf.format(startTime));
		System.out.println("结束时间:" + sdf.format(endTime));
	}

	public static void main(String args[]){
		try {
			GenerateLittleGridLink.generateLittlegridlink();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
