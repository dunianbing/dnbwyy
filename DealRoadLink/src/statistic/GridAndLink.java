package statistic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import base.Constant;
import base.Genetic;

public class GridAndLink {

	private String topoPth = "F://Topo.csv";
	private DecimalFormat linkRex = new DecimalFormat("00000");
	Map<String, Map<String, Integer>> meshMap;

	/*
	 * 根据Topo文件，统计了北京路网的网格数和路链数 10年地图：42网格，网格内路链统计见结果文件MeshandLink2010.txt
	 */
	public void countMeshLink() {
		meshMap = new HashMap<String, Map<String, Integer>>();
		Map<String, Integer> linkMap;
		int a;
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(topoPth));
			String line, meshNo, linkNo;
			bReader.readLine();
			while ((line = bReader.readLine()) != null) {
				meshNo = Genetic.splitString(line, 1, 2);
				linkNo = linkRex.format(Integer.valueOf(Genetic.splitString(
						line, 2, 3)));
				if (meshMap.containsKey(meshNo)) {
					linkMap = meshMap.get(meshNo);
					if (linkMap.containsKey(linkNo)) {
						a = (int) (linkMap.get(linkNo)) + 1;
						linkMap.put(linkNo, a);
					} else {
						linkMap.put(linkNo, 1);
					}
					meshMap.put(meshNo, linkMap);
				} else {
					linkMap = new HashMap<String, Integer>();
					linkMap.put(linkNo, 1);
					meshMap.put(meshNo, linkMap);
				}
			}
			bReader.close();
			new Genetic();
			System.out.println("网格号共有： " + meshMap.size());
			Genetic.printDoubleMapInteger(meshMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void compare42Mesh() {
		int i=0,sum = 0,tempsum;
		Map<String, Integer> grid213mapMap = new CDataGrid()
				.readGridRecordNumToMap();
		for (String key : meshMap.keySet()) {
			tempsum = meshMap.get(key).size();
			sum += tempsum;
			System.out.println((++i)+"\t网格号："+key+"\t路链数："+tempsum+"\t数据条数："+grid213mapMap.get(key));
		}
		System.out.println("一天数据总条数："+Constant.allGridSum+"\t42网格数据总条数："+sum+"\t42网格的数据覆盖率："+((float)sum/(float)Constant.allGridSum));
	}

	public static void main(String[] args) {
		GridAndLink mal = new GridAndLink();
		mal.countMeshLink();//统计42网格路链数
		mal.compare42Mesh();
	}
}
