package objects;

public class Connection {

	private int compo1;
	private int compo2;

	public Connection(int compo1,int compo2){
		this.compo1 = compo1;
		this.compo2 = compo2;
	}
	
	public int getFirstCompo() {
		return this.compo1;
	}

	public int getSecondCompo() {
		return this.compo2;
	}

}
