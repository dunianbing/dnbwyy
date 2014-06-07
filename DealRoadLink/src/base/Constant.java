package base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constant {

	/**
	 * @param args
	 */
//	public static String inPth_cdata="F:/dnb_testData/CData";
//	public static String inPth_mr="F:/dnb_testData/MR";
	public static String Pth_gridStatistic="F:/CDataGridCount";
	
	public static String inPth_cdata="J:/Data2011/cdata6/";
	
//	public static String inPth_mr="I:/Data2011/MR6";
//	public static String inPth_tjam="I:/Data2011/Tjam6/TJamData";
//	
//	public static String outPth_Vehicleindex="F:/dnb_testData/Data2011_output/VehicleIndex/20110601_second";
//	public static String outPth_LinkIDindex="F:/dnb_testData/Data2011_output/LinkIDIndex/";
	
	public static String mid_grid_bj="G:/其他/支持文件/bj_mesh_graph/mid_bj.MID";
	public static String mif_grid_bj="G:/其他/支持文件/bj_mesh_graph/mif_bj.MIF";
	
//	public static double longitude_MAX = 117.500000d;
	public static int longitude_MIN = 115375000;
//	public static double latitude_MAX = 41.083380d;
	public static int latitude_MIN = 39416660;
	public static int interval_LON = 125000;
	public static int interval_LAT = 83336;
	public static double inner_inter_LON = 1250;
	public static double inner_inter_ALT = 833.36;
	
	public static Map<String,GridInfo> mapGrid = new HashMap<String,GridInfo>();
	public static Map<String,List<String>> mapRecordSum = new HashMap<String,List<String>>();
	
	public static int mr_record_num = 0;
	public static int mr_marked_num = 0;
	
	public static int tjam_record_num = 0;
	public static int tjam_marked_num = 0;
	
	public static String statistic_beginDate="20110601";
	public static String statistic_endDate="20110610";
	
	public static int allGridSum =0;
	public static int partitionNum = 7;

}
