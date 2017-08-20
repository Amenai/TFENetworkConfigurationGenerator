package objects;

import java.awt.Toolkit;

import controller.SubnetUtils;
import packSystem.HardwaresListS;

public class Router extends Hardware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String ImageFile = "src/router.png";
	private String password = "";
	private String secret = "";
	public Router(String addrs,int code) {		
		super(addrs, Toolkit.getDefaultToolkit().getImage(ImageFile),code,HardwaresListS.ROUTER,"R"+(code+1));
	}

	public void setSecret(String secret) {
		if(!secret.isEmpty()){
			System.out.println("MD5 : " + MD5(secret));
			this.secret = MD5(secret); 
		}
	}
	private String MD5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}
	public void setPassword(String password) {
		this.password = password;

	}
	public String getPassword(){
		return this.password;
	}
	public String getSecret(){
		return this.secret;
	}

}
