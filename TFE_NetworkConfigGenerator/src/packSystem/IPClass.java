package packSystem;

public class IPClass {

	String address = "0.0.0.0";
	String mask ="/24";
	
	public static String getGatewayAddress(){
		return "192.168.0.1";
	}
	public static String getFirstOpenedAddress(){
		return "192.168.0.2";
	}
}
