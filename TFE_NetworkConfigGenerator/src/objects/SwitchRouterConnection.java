package objects;

import java.util.ArrayList;

public class SwitchRouterConnection extends Connection{

	private ArrayList<Integer>subInterface = new ArrayList<>();

	public SwitchRouterConnection(Vlan vlan,int type,int compoID1,int compoID2,int connectionID,String name1,String name2,boolean isSubInterface){
		super(vlan, type, compoID1, compoID2, connectionID, name1, name2, false);
	}

	public void addSubInterface(int co){
		this.subInterface.add(co);
		this.setSubInterface(checkSubInterface());
	}
	public void removeSubInterface(int co){
		for(int i = 0; i< subInterface.size();i++){
			if (subInterface.get(i) == co){
				this.subInterface.remove(i);
				i = subInterface.size();
			}
		}
		this.setSubInterface(checkSubInterface());
	}
	public boolean checkSubInterface(){
		int vlanId = this.getVlanID();
		for(Integer co : this.subInterface){
			if(vlanId != co){
				System.out.println("TRUE");
				return true;
			}
		}
		System.out.println("FALSE");
		return false;		
	}
	public ArrayList<Integer> getSubinterface(){
		return this.subInterface;
	}

}
