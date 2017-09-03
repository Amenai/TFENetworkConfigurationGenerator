package ListsSystem;

public class HardwaresListS {

	public static final int DEFAUT = 0;
	public static final int ROUTER = 1;
	public static final int USER_PC = 2;
	public static final int SWITCH = 3;
	public static final String CONF1 = "no service timestamps log datetime msec"
			+ "\r\nno service timestamps debug datetime msec \r\nno service password-encryption \r\n!\r\n";
	public static final String CONF2 = "! \r\n";
	public static final String CONF3 = "! \r\nno ip cef\r\nno ipv6 cef \r\n!\r\n";
	public static final String CONF4 = "!\r\nip classless\r\n!\r\nip flow-export version 9\r\n!no cdp run\r\n!line con 0\r\n!\r\nline aux 0\r\n!\r\nline vty 0 4\r\nlogin\r\n!\r\n!\r\nend";
	public static final String CONFIGU1 = "no service timestamps log datetime msec \r\nno service timestamps debug datetime msec\r\nno service password-encryption\r\n!\r\n";
}
