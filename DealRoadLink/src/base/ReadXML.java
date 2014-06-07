package base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import statistic.CDataGrid;

public class ReadXML {

	public static Document getDoc(String xmlPth) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document doc = null;
		File f = new File(xmlPth);
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(f);

		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		doc.normalize();
		return doc;
	}

	/*
	 * 读由mid mif 文件生成的xml，读取北京市网格号路链号
	 */
	public static void dealXml(Document doc) {

		Map<String, Map<String, List<GPSInfo>>> meshMap = new TreeMap<String, Map<String, List<GPSInfo>>>();
		Map<String, List<GPSInfo>> linkMap;
		List<GPSInfo> list = null;
		String linkKind = "", points = "", linkID = "", meshNo = "", linkNo = "";
		String[] oneGPS;

		NodeList content = doc.getElementsByTagName("line");
		System.out.print("Length of nodelist--content:");
		System.out.println(content.getLength());
		for (int i = 0; i < content.getLength(); i++) {
			Element record = (Element) content.item(i);
			linkID = record.getElementsByTagName("ID").item(0).getFirstChild()
					.getNodeValue();// 11位路链号
			linkKind = record.getElementsByTagName("Kind").item(0)
					.getFirstChild().getNodeValue();
			points = record.getElementsByTagName("Points").item(0)
					.getFirstChild().getNodeValue();// 所有点的坐标，x1,y1 x2,y2
			// System.out.println("linkID:"+linkID+"\tpoints:"+points);
			meshNo = linkID.substring(0, 6);
			linkKind = linkKind.substring(0, 2);
			linkNo = linkID.substring(6, 11)+linkKind;//把路链级别暂存到路链号中
			GPSInfo gpsInfo;
			oneGPS = points.split(",");
			// System.out.println((++j)+"\tPoints:"+points);

			// 写map
			if (meshMap.containsKey(meshNo)) {// 已有网格号
				linkMap = meshMap.get(meshNo);
				if (linkMap.containsKey(linkNo)) {// 已有网格内路链号
//					 System.out.println((++j)+" wrong!!");
					list = linkMap.get(linkNo);
				} else {
					list = new ArrayList<GPSInfo>();
				}
			} else {// 无网格号
				linkMap = new HashMap<String, List<GPSInfo>>();
				list = new ArrayList<GPSInfo>();
			}

			for (String item : oneGPS) {
				gpsInfo = new GPSInfo();
				gpsInfo.setX(item.split(" ")[0]);
				gpsInfo.setY(item.split(" ")[1]);
				list.add(gpsInfo);
			}
			linkMap.put(linkNo, list);
			meshMap.put(meshNo, linkMap);
		}
		writePoints(meshMap);
//		compare42Mesh(meshMap);
	}

	public static void compare42Mesh(
			Map<String, Map<String, List<GPSInfo>>> meshMap) {
		int i = 0, sum = 0, tempsum, linkNum;
		Map<String, Integer> grid213mapMap = new CDataGrid()
				.readGridRecordNumToMap();
		for (String key : meshMap.keySet()) {// 看路链文件中涉及到的路链和网格，对应的数据量有多少
			linkNum = meshMap.get(key).size();
			if (!grid213mapMap.containsKey(key)) {
				System.out.println((++i) + "\t网格号：" + key + "\t路链数：" + linkNum
						+ "\t" + "该网格号包含路链但不在2.5万网格中");
				continue;
			}
			tempsum = grid213mapMap.get(key);
			sum += tempsum;
			System.out.println((++i) + "\t网格号：" + key + "\t路链数：" + linkNum
					+ "\t数据条数：" + tempsum + "\t每条路链平均数据条数" + (float) tempsum
					/ linkNum);
		}
		System.out.println("一天数据总条数：" + Constant.allGridSum + "\t路链网格数据总条数："
				+ sum + "\t数据覆盖率："
				+ ((float) sum / (float) Constant.allGridSum));
	}

	public static void writePoints(
			Map<String, Map<String, List<GPSInfo>>> meshMap) {
		String outpth = "G:/其他/支持文件/GridLinkwithRoadKind2010.txt";
		Map<String, List<GPSInfo>> linkMap;
		List<GPSInfo> alinkPoints;
		PrintWriter pw = null;
		String writeline = "";
		int count = 0;
		try {
			pw = new PrintWriter(new File(outpth));
			// 写文件
			for (String meshno : meshMap.keySet()) {
				linkMap = meshMap.get(meshno);
				for (String linkno : linkMap.keySet()) {
					alinkPoints = linkMap.get(linkno);
					GPSInfo point = null;
					String points = "";
					if (alinkPoints == null || alinkPoints.size() < 1)
						continue;
					for (int i = 0; i < alinkPoints.size() - 1; i++) {
						point = alinkPoints.get(i);
						points = points + point.getX() + " " + point.getY()
								+ ",";
					}
					point = alinkPoints.get(alinkPoints.size() - 1);
					points = points + point.getX() + " " + point.getY();//最后一个点单独处理
					writeline = (++count) + ";" + meshno + ";" + linkno.substring(0,5)+";"+linkno.substring(5,7) + ";"
							+ points + "\r\n";
					pw.write(writeline);
				}
			}

			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String xmlPth = "G:/其他/支持文件/2010_map.xml";
		new ReadXML();
		ReadXML.dealXml(ReadXML.getDoc(xmlPth));// 读取由mid mif文件得到的路链
	}
}
