package base;

import java.text.DecimalFormat;

public class GPSInfo {
	/**当经纬度变量设为float时，不能完整表示经纬度小数点之后的数字
	 * @param args
	 * 
	 */
	
	private String gpsX ;//经度
	private String gpsY ;//纬度
	
	public GPSInfo(double x,double y){
		DecimalFormat dFormat = new DecimalFormat("#.000000");
		this.gpsX = dFormat.format(x);
		this.gpsY = dFormat.format(y);
	}
	public GPSInfo(String x,String y){
		this.gpsX = x;
		this.gpsY = y;
	}
	public GPSInfo(){
		this.gpsX = null;
		this.gpsY = null;
	}
	
	public String getX (){
		return this.gpsX;
	}
	public double getDoubleX (){
		return Double.valueOf(this.gpsX);
	}
	public void setX (String x){
		this.gpsX = x;
	}
	public void setX (double x){
		DecimalFormat dFormat = new DecimalFormat("#.000000");
		this.gpsX = dFormat.format(x);
	}
	public String getY (){
		return this.gpsY;
	}
	public double getDoubleY (){
		return Double.valueOf(this.gpsY);
	}
	public void setY (String y){
		this.gpsY = y;
	}
	public void setY (double y){
		DecimalFormat dFormat = new DecimalFormat("#.000000");
		this.gpsY = dFormat.format(y);
	}
	public void printGPS(){
		System.out.print("GPS--->X:"+this.gpsX+"--->Y:"+this.getY());
	}
}
