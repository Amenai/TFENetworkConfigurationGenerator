package graphique;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import ListsSystem.ConnectionsTypes;
import ListsSystem.HardwaresListS;
import controller.SubnetUtils;
import objects.Connection;
import objects.Network;
import objects.Router;
import objects.UserPC;
import objects.Vlan;
import packSystem.HeadsTable;

public class RouterConfigurationGUI implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton Save = new JButton("Save");
	private JButton Generate = new JButton("Generate"); 
	private JLabel hostname = new JLabel("Hostname");
	private JLabel secret = new JLabel("Secret");
	private JTextField secretEdit = new JTextField();
	private JLabel password = new JLabel("Password");
	private JTextField passwordEdit = new JTextField();
	private JTextField hostnameEdit = new JTextField();
	private ArrayList<InterfacePanel> panels = new ArrayList<InterfacePanel>();
	private JFrame frame = new JFrame();
	private InterfacePanel p;
	private HeadsTable headNum = new HeadsTable("Num",50);
	private HeadsTable headName = new HeadsTable("Name",100);
	private HeadsTable headIP = new HeadsTable("IP",125);
	private HeadsTable headMasque = new HeadsTable("Vlan",100);
	private HeadsTable headType = new HeadsTable("Type",100);
	private HeadsTable headDelete = new HeadsTable("Delete",50);

	public RouterConfigurationGUI(Network n,Router hardRouter) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {			
				frame = new JFrame(hardRouter.getHostname());
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setLayout(new GridBagLayout());      
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.gridx = 0;
				gbc.gridy = 0;
				gbc.gridheight = 1;
				gbc.gridwidth = 1;
				frame.add(hostname, gbc);
				JPanel host = new JPanel();
				host.add(hostname);
				host.add(hostnameEdit);
				hostname.setPreferredSize(new Dimension(100, 25));
				hostnameEdit.setPreferredSize(new Dimension(100, 25));
				hostnameEdit.setText(hardRouter.getHostname());				
				frame.add(host, gbc);
				gbc.gridy++;
				JPanel secretP = new JPanel();
				secretP.add(secret);
				secretP.add(secretEdit);
				secret.setPreferredSize(new Dimension(100, 25));
				secretEdit.setPreferredSize(new Dimension(100, 25));
				String sec = (hardRouter).getSecret();
				if(!sec.isEmpty()){
					sec = "******";
				}
				secretEdit.setText(sec);				
				frame.add(secretP, gbc);
				gbc.gridy++;
				JPanel passwordP = new JPanel();
				passwordP.add(password);
				passwordP.add(passwordEdit);
				password.setPreferredSize(new Dimension(100, 25));
				passwordEdit.setPreferredSize(new Dimension(100, 25));
				passwordEdit.setText((hardRouter).getPassword());				
				frame.add(passwordP, gbc);
				gbc.gridy++;				
				if(!(n.getConnections(hardRouter.getConnection()).isEmpty())){
					JPanel heads = new JPanel();
					heads.setLayout(new GridBagLayout());
					GridBagConstraints gribC = new GridBagConstraints();
					gribC.gridx = 0;
					gribC.gridy = 0;
					heads.add(headNum, gribC);
					gribC.gridx++;
					heads.add(headName, gribC);
					gribC.gridx++;
					heads.add(headIP, gribC);
					gribC.gridx++;
					heads.add(headMasque, gribC);
					gribC.gridx++;
					heads.add(headType, gribC);
					gribC.gridx++;
					heads.add(headDelete, gribC);
					frame.add(heads, gbc);
					gbc.gridy++;
					int i = 1;	
					for( Connection c : n.getConnections(hardRouter.getConnection())){
						p = new  InterfacePanel(c,i,hardRouter,n);
						panels.add(p);
						frame.add(p,gbc);
						gbc.gridy++;
						i++;
					}
					gbc.gridy++;
				}
				else {
					JLabel l = new JLabel("No connection found");
					l.setPreferredSize(new Dimension(140, 25));
					frame.add(l,gbc);
					gbc.gridy++;
				}

				frame.add(new JSeparator(), gbc);



				JPanel panel = new JPanel(new GridLayout(1,3));				
				panel.add(Save);			
				Save.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						int index = 0;
						boolean noError = true;
						hardRouter.setHostname(hostnameEdit.getText());

						if (!(secretEdit.getText().equals("******"))){
							(hardRouter).setSecret(secretEdit.getText());
						}
						(hardRouter).setPassword(passwordEdit.getText());


						for( Connection c : n.getConnectionsof(hardRouter.getID())){
							InterfacePanel p =  panels.get(index);
							if(p.getDeleteBox()){
								if(packSystem.Messages.confirm("" + p.getIp() + " will be deleted ")){
									n.removeConnection(c);
								}
							}
							else{
								if(checkConnectionChange(p,c)){
									if(c.getVlanID() != p.getVlan()){
										ArrayList<Connection> co= new ArrayList<>();
										co.add(c);
										n.changingVlan(2, co, p.getVlan());
									}else {
										if(!(c.setCompoIP(p.getIp(),hardRouter.getID()))){
											packSystem.Messages.showErrorMessage("Problème IP");
											noError = false;
										}
										else {
											if(n.getHardware(c.getSecondCompo()).getType() == HardwaresListS.USER_PC){
												((UserPC)n.getHardware(c.getSecondCompo())).setGateway(p.getIp());
											}
										}
									}
									c.setCompoName(hardRouter.getID(), p.getIntName());
									c.setType(p.getType());									
								}
								else{
									System.out.println("NO change");
								}
							}
							index++;
						}
						//FIN CONNECTIONS

						if(noError){
							packSystem.Messages.showMessage("Saved successfully", "Confirmation");
							frame.dispose();
							RouterConfigurationGUI gui = new RouterConfigurationGUI(n, hardRouter);
						}
					}

					private boolean checkConnectionChange(InterfacePanel intP, Connection old) {	

						if (old.getType() == intP.getType()){
							if (old.getVlanID() == intP.getVlan()){
								if(old.getFirstCompo() == hardRouter.getID()){
									if(old.getCompoIP1() == intP.getIp()){
										return false;
									}
								}
								else {
									if(old.getCompoIP2() == intP.getIp()){
										return false;										
									}
								}
							}							
						}			
						return true;
					}
				});
				Generate.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						String path = packSystem.Messages.savingFile(JFileChooser.OPEN_DIALOG).getPath();
						String conf = n.printConfig(hardRouter.getID());
						try (PrintStream out = new PrintStream(new FileOutputStream(path))) {
							out.print(conf);
							packSystem.Messages.showMessage("Generating completed", "Confirmation");;
						} catch (FileNotFoundException ex) {
							ex.printStackTrace();
						}						
					}
				});
				panel.add(Generate);
				frame.add(panel, gbc); 
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setResizable(false);
				frame.setVisible(true);
			}

		});
	}

	class InterfacePanel extends JPanel{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JComboBox<?> ip ;
		private int num ;
		private JComboBox<?> typeCombo ;
		private JComboBox<?> vlan ;
		private JTextField intName = new JTextField();
		private JCheckBox deleted = new JCheckBox();
		public  InterfacePanel(Connection c, int num, Router draggy, Network n) {
			this.num = c.getConnectionID();
			JTextField t = new JTextField((num)+".");	
			t.setPreferredSize(new Dimension(15, 25));
			this.add(t);
			t.setEditable(false);			
			if (c.getFirstCompo() == draggy.getID()){
				ip = getComboIp(c.getSubnetwork(),c.getCompoIP1());
				intName.setText(c.getCompoName(c.getFirstCompo()));
				ip.setSelectedItem(c.getCompoIP1());
			}
			else {
				ip = getComboIp(c.getSubnetwork(),c.getCompoIP2());
				intName.setText(c.getCompoName(c.getSecondCompo()));
				ip.setSelectedItem(c.getCompoIP2());
			}		
			intName.setPreferredSize(new Dimension(100, 25));
			ip.setPreferredSize(new Dimension(125, 25));
			this.add(intName);			
			this.add(ip);
			vlan = getComboVlan(n,n.getVlans().get(c.getVlanID()).getName());
			vlan.setSelectedItem(n.getVlans().get(c.getVlanID()).getName());
			this.add(vlan);

			this.typeCombo = createComboColorPanel(c.getType());

			this.add(typeCombo);
			this.add(deleted);
		}

		private JComboBox<?> getComboIp(SubnetUtils subnetwork, String ip) {
			ArrayList<String> freeIp = subnetwork.getInfo().getAllFreeAddress();
			freeIp.add(ip);
			Collections.sort(freeIp);
			String [] ips = freeIp.toArray(new String[0]);
			JComboBox<?> cb = new JComboBox<Object>(ips);
			return cb;
		}
		private JComboBox<?> getComboVlan(Network n, String vlan) {
			HashMap<Integer, Vlan> vlan2 = n.getVlans();
			JComboBox cb = new JComboBox();
			for(Entry<Integer, Vlan> v : vlan2.entrySet()){
				Item item = new Item(v.getKey(),v.getValue().getName());
				cb.addItem(item);
				if(v.getValue().getName() == vlan){
					cb.setSelectedItem(item);
				}
			}
			return  cb;
		}
		private JComboBox<?> createComboColorPanel(int type){

			JComboBox<?> cb = new JComboBox<Object>(ConnectionsTypes.list);		
			switch(type){ 
			case ConnectionsTypes.GIGABIT: cb.setSelectedIndex(0); break;
			case ConnectionsTypes.SERIAL: cb.setSelectedIndex(1); break;
			case ConnectionsTypes.ETHERNET: cb.setSelectedIndex(2); break;
			}
			return cb;
		}	
		public int getNum() {		
			return this.num;
		}

		protected String getIp(){
			return (String)this.ip.getSelectedItem();
		}
		protected int getType(){
			return this.typeCombo.getSelectedIndex();
		}
		public String getIntName(){
			return this.intName.getText();
		}
		public boolean getDeleteBox(){
			return this.deleted.isSelected();
		}
		public int getVlan(){
			return ((Item)(this.vlan.getSelectedItem())).getId();
		}
	}
	class Item
	{
		private int key;
		private String vlanName;

		public Item(int key, String name)
		{
			this.key = key;
			this.vlanName = name;
		}

		public int getId()
		{
			return key;
		}

		public String getDescription()
		{
			return vlanName;
		}

		public String toString()
		{
			return vlanName;
		}
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {

	}
}

