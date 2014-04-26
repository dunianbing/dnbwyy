package base;

public class GPS {
	private String gpsx;
	private String gpsy;
	public GPS(String gpsx, String gpsy) {
		this.gpsx = gpsx;
		this.gpsy = gpsy;
	}
	public GPS() {
		this.gpsx = "null";
		this.gpsy = "null";
	}
	public String getGpsx() {
		return gpsx;
	}
	public void setGpsx(String gpsx) {
		this.gpsx = gpsx;
	}
	public String getGpsy() {
		return gpsy;
	}
	public void setGpsy(String gpsy) {
		this.gpsy = gpsy;
	}
	
}
