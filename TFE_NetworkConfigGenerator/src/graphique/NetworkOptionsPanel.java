package graphique;

import java.awt.Checkbox;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import controller.SubnetUtils;
import objects.Network;
import objects.Vlan;
import packSystem.HeadsTable;

public class NetworkOptionsPanel extends JFrame implements ActionListener{
	private int height = 900;
	private int width = 300;
	private Network network;

	protected ArrayList<VlansPanels> vlansPanels = new ArrayList<VlansPanels>();
	private HeadsTable headName = new HeadsTable("Name",100);
	private HeadsTable headVlanNum = new HeadsTable("Nums",50);
	private HeadsTable headVlanIp = new HeadsTable("Subnet",125);
	private HeadsTable headDelete = new HeadsTable("Delete",50);
	private HeadsTable headSelect = new HeadsTable("Select",50);
	protected JTextField newName = new JTextField();
	protected JTextField newVlanNum = new JTextField();
	protected JTextField newVlanIp = new JTextField();
	protected JTextField newNum = new JTextField();

	protected JPanel f = new JPanel();

	private JButton addVlan = new JButton("Add"); 
	private JButton saveVlan = new JButton("Save"); 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public NetworkOptionsPanel(Network network){
		this.network = network;

		this.vlansPanels = new ArrayList<>(vlansPanels);
		for(Vlan v : network.getVlans().values()){
			VlansPanels vp = new VlansPanels(v, v.getNum());
			vlansPanels.add(vp);
		}
		f.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		f.add(makeHeads(), gbc);
		gbc.gridy++;

		f.add(interfacePanels(), gbc);
		gbc.gridy++;
		f.add(newPanel(vlansPanels.size()),gbc);
		gbc.gridy++;
		f.add(saveVlan ,gbc);	
		addVlan.addActionListener(this);
		saveVlan.addActionListener(this);

		this.add(f);
		this.pack();
		this.setTitle("Network Options");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(false);

	}
	private JPanel interfacePanels() {
		JPanel interfacesPanel = new JPanel();
		interfacesPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		int compteur = 1;
		for(VlansPanels vp : vlansPanels){
			JTextField num = new JTextField(compteur+ ".");
			num.setEditable(false);
			interfacesPanel.add(num,gbc);
			gbc.gridx++;
			interfacesPanel.add(vp.getSelectedBox(),gbc);
			gbc.gridx++;
			interfacesPanel.add(vp.getName(), gbc);
			gbc.gridx++;
			interfacesPanel.add(vp.getNum(), gbc);
			gbc.gridx++;
			interfacesPanel.add(vp.getIp(), gbc);
			gbc.gridx++;
			interfacesPanel.add(vp.getDeleteBox(), gbc);
			gbc.gridy++;
			gbc.gridx=0;
			compteur++;
		}
		return interfacesPanel;
	}
	private JPanel makeHeads() {
		JPanel headsPanel = new JPanel();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;

		JTextField empty = new JTextField();
		empty.setPreferredSize(new Dimension(10, 25));
		empty.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		empty.setEditable(false);
		headsPanel.add(empty,gbc);
		gbc.gridx++;
		headsPanel.add(headSelect,gbc);
		gbc.gridx++;
		headsPanel.add(headName,gbc);
		gbc.gridx++;
		headsPanel.add(headVlanNum, gbc);
		gbc.gridx++;
		headsPanel.add(headVlanIp,gbc);
		gbc.gridx++;
		headsPanel.add(headDelete,gbc);
		return headsPanel;
	}
	private JPanel newPanel(int compteur) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		JPanel newPanel = new JPanel();
		newNum.setEditable(false);
		newNum.setPreferredSize(new Dimension(15, 25));
		newName.setPreferredSize(new Dimension(100, 25));
		newVlanNum.setPreferredSize(new Dimension(50, 25));
		newVlanIp.setPreferredSize(new Dimension(125, 25));
		JTextField empty = new JTextField();
		empty.setPreferredSize(new Dimension(50, 25));
		empty.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		empty.setEditable(false);
		newNum.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		newName.setText("VLAN "+network.getVlans().size());
		newVlanNum.setText("0");
		newVlanIp.setText("0.0.0.0/0");
		gbc.gridx= 0;
		gbc.gridy = gbc.gridy + compteur;
		newPanel.add(newNum,gbc);
		gbc.gridx++;
		newPanel.add(empty,gbc);
		gbc.gridx++;
		newPanel.add(newName,gbc);
		gbc.gridx++;
		newPanel.add(newVlanNum,gbc);
		gbc.gridx++;
		newPanel.add(newVlanIp,gbc);
		gbc.gridx++;
		newPanel.add(addVlan ,gbc);	
		return newPanel;
	}
	class VlansPanels{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private JCheckBox selected = new JCheckBox();
		private JTextField name = new JTextField();
		private JTextField vlanNum = new JTextField();
		private JTextField ip = new JTextField();
		private JCheckBox deleted = new JCheckBox();

		private int oldNum ;
		public  VlansPanels(Vlan vlan, int num) {
			this.oldNum = vlan.getNum();
			JTextField t = new JTextField((num)+".");
			ip.setText(vlan.getSubnetwork().getInfo().getCidrSignature());
			name.setText(vlan.getName());
			vlanNum.setText(""+vlan.getNum());

			t.setPreferredSize(new Dimension(15, 25));
			name.setPreferredSize(new Dimension(100, 25));
			vlanNum.setPreferredSize(new Dimension(50, 25));
			ip.setPreferredSize(new Dimension(125, 25));
			selected.setPreferredSize(new Dimension(50,25));
			deleted.setPreferredSize(new Dimension(50,25));
		}		
		public JTextField getNum() {
			return this.vlanNum;
		}
		public int getOldNum() {
			return this.oldNum;
		}
		public JTextField getName(){
			return this.name;
		}
		public JCheckBox getDeleteBox(){
			return this.deleted;
		}
		public JCheckBox getSelectedBox(){
			return this.selected;
		}
		public JTextField getIp() {
			return this.ip;
		}
		public void setOldNum(String text) {
			this.oldNum = Integer.parseInt(text);			
		}	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		boolean error = false;
		if (e.getSource() == addVlan){
			try{
				int num = Integer.parseInt(this.newVlanNum.getText());  
				if((network.getVlans().get(num)) != null){		
					error = true;
					throw new NumberFormatException("Vlan " + num + " is already existing");
				}
				try{
					Vlan newVlan = new Vlan(new SubnetUtils(this.newVlanIp.getText()), num, this.newName.getText());
					network.addVlan(newVlan);
					VlansPanels vp = new VlansPanels(newVlan, newVlan.getNum());
					vlansPanels.add(vp);
				}
				catch(Exception e1) {
					error = true;
					packSystem.Messages.showErrorMessage(e1.getMessage());
				}
			}
			catch(NumberFormatException nfe){
				error = true;
				packSystem.Messages.showErrorMessage("Error Vlan Number, please try to fix this" +"\n"+ nfe.getMessage());
			}
			if(!error){
				JPanel test = new JPanel();

				test.setLayout(new GridBagLayout());
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.gridx = 1;
				gbc.gridy = 0;
				gbc.gridheight = 1;
				gbc.gridwidth = 1;
				test.add(makeHeads(), gbc);
				gbc.gridy++;

				test.add(interfacePanels(), gbc);
				gbc.gridy++;
				test.add(newPanel(vlansPanels.size()),gbc);
				gbc.gridy++;
				test.add(saveVlan ,gbc);	
				this.remove(f);				
				f= test;
				this.add(f);
				this.invalidate();
				this.validate();
				this.repaint();
				this.pack();
			}
		}		
		if (e.getSource() == saveVlan){
			error = false;
			int index = 0;
			ArrayList<VlansPanels> save = new ArrayList<VlansPanels>();
			for (VlansPanels vp : this.vlansPanels){
				
				if (vp.getDeleteBox().isSelected()){
					if(packSystem.Messages.confirm("" + vp.getName().getText() + " and all connections will be deleted ")){
						network.removeVlan(vp.getOldNum());						
					}
				}
				else {
					save.add(vp);
					network.getVlans().get(vp.getOldNum()).setName(vp.getName().getText());
					String cidr1 = network.getVlans().get(vp.getOldNum()).getSubnetwork().getInfo().getCidrSignature();
					String cidr2 = vp.getIp().getText();
					if (!cidr1.equals(cidr2)){
						System.out.println("1 : " + cidr1 +" ====  " +cidr2);
						ArrayList<Integer> coList = network.getVlans().get(vp.getOldNum()).getConnectionsList();
						SubnetUtils newSubnet = new SubnetUtils(vp.getIp().getText());
						int newCapacity = newSubnet.getInfo().getAddressCount();
						if(newCapacity < coList.size()*2){
							error = true;
							packSystem.Messages.showErrorMessage("Error Vlan Subnet, no enough IP available" +"\n" + "IP available : " + newCapacity +"\n" + "IP needed : " + coList.size());
						}
						else {
							if (network.newSubnetwork(vp.getOldNum(),newSubnet)){
								//TODO GET ALL CO 
								network.changingVlan(coList.size()*2, network.getConnections(coList), vp.getOldNum());						
							}						
						}
					}
					try{
						int num = Integer.parseInt(vp.getNum().getText());
						if((num != vp.getOldNum()) && (network.getVlans().get(num)) != null){
							error = true;
							throw new NumberFormatException("Vlan " + num + " is already existing");
						}
						else{
							network.getVlans().get(vp.getOldNum()).setNum(Integer.parseInt(vp.getNum().getText()));
							vp.setOldNum(vp.getNum().getText());
						}
					}
					catch(NumberFormatException nfe){
						error = true;
						packSystem.Messages.showErrorMessage("Error Vlan Number, please try to fix this" +"\n"+ nfe.getMessage());
					}	
				}
				index++;
			}
			if(!error){
				packSystem.Messages.showMessage("Saved successfully", "Confirmation");	
				JPanel test = new JPanel();
				vlansPanels = save;
				test.setLayout(new GridBagLayout());
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.gridx = 1;
				gbc.gridy = 0;
				gbc.gridheight = 1;
				gbc.gridwidth = 1;
				test.add(makeHeads(), gbc);
				gbc.gridy++;

				test.add(interfacePanels(), gbc);
				gbc.gridy++;
				test.add(newPanel(vlansPanels.size()),gbc);
				gbc.gridy++;
				test.add(saveVlan ,gbc);	
				this.remove(f);				
				f= test;
				this.add(f);
				this.invalidate();
				this.validate();
				this.repaint();
				this.pack();
			}
		}
	}	
	public int getSelectedVlan() {
		for(VlansPanels vp : vlansPanels){
			if(vp.getSelectedBox().isSelected()){
				return Integer.parseInt(vp.getNum().getText());

			}
		}
		return 0;// equals Global VLAN
	}

}
