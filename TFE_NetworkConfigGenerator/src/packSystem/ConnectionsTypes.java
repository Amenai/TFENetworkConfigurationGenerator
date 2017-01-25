package packSystem;

import java.awt.Color;

public class ConnectionsTypes {

	public static final int GIGABIT = MouseTypes.MOUSE_GIGABITS;
	public static final int SERIAL = MouseTypes.MOUSE_SERIAL;
	public static final int ETHERNET = MouseTypes.MOUSE_ETHERNET;
	public static final String[]list = { "GIGABIT", "SERIAL","ETHERNET"};

	public static Color getColor(int types){
		switch(types) {
		case GIGABIT: return Color.GREEN ;
		case SERIAL: return Color.BLUE;
		case ETHERNET: return Color.RED;

		}
		System.out.println("DEFAULT COLOR PICKED");
		return Color.BLACK;
	}
}
