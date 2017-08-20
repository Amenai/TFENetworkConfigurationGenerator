package objects;

import java.awt.Toolkit;
import java.util.HashMap;

import controller.SubnetUtils;
import packSystem.HardwaresListS;

public class Switch extends Hardware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String ImageFile = "src/switch.png";
	private HashMap<Integer,Vlan> vlansList  = new HashMap<Integer,Vlan>();

	public Switch(String addrs,int code) {		
		super(addrs, Toolkit.getDefaultToolkit().getImage(ImageFile),code,HardwaresListS.SWITCH,"S"+(code+1));
	}
	public void setVlans(Vlan vlan){
		//
	}
	public Vlan getVlans(int i){
		return this.vlansList.get(i);
	}
}
class Vlan {
	public Vlan() {
		
	}
}
