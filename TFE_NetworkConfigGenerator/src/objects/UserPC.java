package objects;

import java.awt.Toolkit;

import packSystem.HardwaresListS;
import packSystem.SubnetUtils;

public class UserPC extends Hardware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String ImageFile = "src/pc.png";
	private String gateway = "0.0.0.0";
	private boolean isLinked = false;
	private String IP = "0.0.0.0";
	public UserPC(SubnetUtils network,int code) {
		super(network,network.getInfo().getLowAddress(),Toolkit.getDefaultToolkit().getImage(ImageFile),code,HardwaresListS.USER_PC,"U"+(code+1));
	}
	public String getGateway() {
		return this.gateway;
	}
	public void setGateway(String gateway){
		this.gateway = gateway;
	}
	public boolean isLinked() {
		return isLinked;
	}
	public void setLinked(boolean isLinked) {
		this.isLinked = isLinked;
	}
	public String getIP(){
		return this.IP ;
	}	
	public boolean setIp(String ip){
		SubnetUtils sub = this.getSubnet();
		if (sub.getInfo().isInRange(ip)){
			this.IP = ip;
			return true;
		}
		return false;
	}
}
