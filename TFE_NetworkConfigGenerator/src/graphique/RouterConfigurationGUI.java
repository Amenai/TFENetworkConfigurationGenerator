package graphique;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.RepaintManager;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import controller.SubnetUtils;
import objects.Connection;
import objects.Hardware;
import objects.Network;
import objects.Router;
import objects.UserPC;
import packSystem.ConnectionsTypes;
import packSystem.HardwaresListS;
import packSystem.HeadsTable;
import packSystem.Messages;

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
	private HeadsTable headIP = new HeadsTable("IP",100);
	private HeadsTable headMasque = new HeadsTable("Masque",100);
	private HeadsTable headType = new HeadsTable("Type",100);
	private HeadsTable headDelete = new HeadsTable("Delete",50);

	public RouterConfigurationGUI(Network n,Router draggy) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Turn off double buffering
				RepaintManager.currentManager(null).setDoubleBufferingEnabled(false);
				frame = new JFrame(draggy.getHostname());
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
				hostnameEdit.setText(draggy.getHostname());				
				frame.add(host, gbc);
				gbc.gridy++;
				JPanel secretP = new JPanel();
				secretP.add(secret);
				secretP.add(secretEdit);
				secret.setPreferredSize(new Dimension(100, 25));
				secretEdit.setPreferredSize(new Dimension(100, 25));
				String sec = (draggy).getSecret();
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
				passwordEdit.setText((draggy).getPassword());				
				frame.add(passwordP, gbc);
				gbc.gridy++;				
				if(!(n.getConnections(draggy.getConnection()).isEmpty())){
					//TODO
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
					for( Connection c : n.getConnections(draggy.getConnection())){    
						p = new  InterfacePanel(c,i,draggy,n);
						panels.add(p);
						frame.add(p,gbc);
						gbc.gridy++;
						i++;
					}
				}
				gbc.gridy++;
				frame.add(new JSeparator(), gbc);



				JPanel panel = new JPanel(new GridLayout(1,3));				
				panel.add(Save);			
				Save.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						int index = 0;
						boolean noError = true;
						draggy.setHostname(hostnameEdit.getText());

						if (!(secretEdit.getText().equals("******"))){
							(draggy).setSecret(secretEdit.getText());
						}
						(draggy).setPassword(passwordEdit.getText());
						for( Connection c : n.getConnectionsof(draggy.getID())){
							InterfacePanel p =  panels.get(index);
							if(p.getDeleteBox()){
								if(packSystem.Messages.confirm("" + p.getIp() + " va être supprimée, êtes vous sûr ? ")){
									n.removeConnection(c);
								}
							}
							else{
								//TODO if mask change : check IP in MASK
								if(checkConnectionChange(c,n)){
									if(!(c.setCompoIP(p.getIp(),draggy.getID()))){
										packSystem.Messages.showErrorMessage("Problème IP");
										noError = false;
									}
									else {
										if(n.getHardware(c.getSecondCompo()).getType() == HardwaresListS.USER_PC){
											((UserPC)n.getHardware(c.getSecondCompo())).setGateway(p.getIp());
										}
									}	

									c.setType(p.getType());	
									c.setCompoName(draggy.getID(),n.getInterfaceName(draggy.getID(), p.getType()));

								}
							}
							index++;
						}



						if(noError){
							packSystem.Messages.showMessage("Sauvegarde réussie", "Confirmation");
						}
						frame.dispose();
						RouterConfigurationGUI gui = new RouterConfigurationGUI(n, draggy);
					}

					private boolean checkConnectionChange(Connection co, Network n) {
						Connection old = n.getAllConnections().get(co.getConnectionID());						
						if (old.getCompoIP1() == co.getCompoIP1()){
							if(old.getCompoIP2() == co.getCompoIP2()){
								if (old.getType() == co.getType()){
									if (old.getSubnetwork() == co.getSubnetwork()){
										System.out.println("NO CHANGE");
										return true;
									}
									else{System.out.println("Subnetwork : " +old.getSubnetwork().toString() + "/" + co.getSubnetwork().toString());}
								}
								else{System.out.println("Type :" + old.getType() +"/" + co.getType());}
							}
							else{System.out.println("IP2 :"+ old.getCompoIP2() + "/"+ co.getCompoIP2());}
						}
						else{System.out.println("IP1 :"+ old.getCompoIP1() + "/"+ co.getCompoIP1());}
						return false;
					}
				});
				/*	Generate.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						/*String path = Messages.savingFile(JFileChooser.OPEN_DIALOG).getPath();
						String conf = n.printConfig(draggy.getID());
						System.out.println("GENERATING");
						try (PrintStream out = new PrintStream(new FileOutputStream(path))) {
							out.print(conf);
							packSystem.Messages.showMessage("Génération Terminée", "Confirmation");;
						} catch (FileNotFoundException ex) {
							ex.printStackTrace();
						}
						boolean confirm = packSystem.Messages.confirm("Etes vous sur de vouloir supprimer l'interface ? ");
					}
				});*/
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
		private JComboBox<?> combo ;
		private JTextField mask = new JTextField();
		private JTextField name = new JTextField();
		private JCheckBox deleted = new JCheckBox();
		public  InterfacePanel(Connection c, int num, Router draggy, Network n) {
			this.num = num;
			JTextField t = new JTextField((this.num)+".");	
			t.setPreferredSize(new Dimension(15, 25));
			this.add(t);
			t.setEditable(false);			
			if (c.getFirstCompo() == draggy.getID()){
				ip = getComboIp(c.getSubnetwork(),c.getCompoIP1());
				name.setText(c.getCompoName(c.getFirstCompo()));
				ip.setSelectedItem(c.getCompoIP1());
			}
			else {
				ip = getComboIp(c.getSubnetwork(),c.getCompoIP2());
				name.setText(c.getCompoName(c.getSecondCompo()));
				ip.setSelectedItem(c.getCompoIP2());
			}		
			name.setPreferredSize(new Dimension(100, 25));
			ip.setPreferredSize(new Dimension(100, 25));
			mask.setPreferredSize(new Dimension(100, 25));
			this.add(name);			
			this.add(ip);
			this.add(mask);
			mask.setText(c.getSubnetwork().getInfo().getNetmask());
			combo = createComboColorPanel(c.getType());

			this.add(combo);
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
			return this.combo.getSelectedIndex();
		}
		public String getName(){
			return this.name.getText();
		}
		public boolean getDeleteBox(){
			return this.deleted.isSelected();
		}
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
}

