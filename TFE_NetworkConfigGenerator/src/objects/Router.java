package objects;

import java.awt.Toolkit;

import org.json.simple.JSONObject;

import ListsSystem.HardwaresListS;
import controller.SubnetUtils;

public class Router extends Hardware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String ImageFile = "/resources/router.png";
	private String password = "";
	private String secret = "";
	public Router(int code,String hostname) {
		super(ImageFile,code,HardwaresListS.ROUTER,hostname);
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
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getJSONObject() {
		JSONObject obj = new JSONObject();   
		obj.put("type", this.getType());
		obj.put("secret",this.getSecret());
		obj.put("password",this.getPassword());	
		obj.put("id",this.getID());
		obj.put("hostname",this.getHostname());
		obj.put("positionX",this.getLocation().x);
		obj.put("positionY",this.getLocation().y);
		return obj;
	}
	@Override
	public Hardware fromJSONObject(JSONObject obj) {
		return this;
	
	}
}
