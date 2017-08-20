package packSystem;

public class HardwaresListS {

	public static final int DEFAUT = 0;
	public static final int ROUTER = 1;
	public static final int USER_PC = 2;
	public static final int SWITCH = 3;
	public static final String CONF1 = "Current configuration : xx bytes \n ! \n version 12.4 \n no service timestamps log datetime msec"
			+ "\n no service timestamps debug datetime msec \n no service password-encryption \n!\n";
	public static final String CONF2 = "! \n !\n!\n";
	public static final String CONF3 = "! \n !\n!\n ! \n !\n!\nno ip cef\nno ipv6 cef \n !\n!\n !\n !\n!\n !\n !\n!\n ! \n !\n!\n !\n !\n!\n !";
	public static final String CONF4 = "!\n ip classless\n!\nip flow-export version 9\n!\n!\n!\nno cdp run\n!\n!\n!\n!\n!\nline con 0\n!\nline aux 0\n!\nline vty 0 4\n login\n!\n!\n!\nend\n";
}
