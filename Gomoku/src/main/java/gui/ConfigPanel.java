package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConfigPanel extends JPanel implements ItemListener {
	
	private JComboBox<String> gameType;
	private JComboBox<String> whomBegins;
	
	public ConfigPanel() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
	    c.gridy = GridBagConstraints.RELATIVE;
	    c.gridwidth = 1;
	    c.gridheight = 1;
	    c.insets = new Insets(2, 2, 2, 2);
	    c.anchor = GridBagConstraints.EAST;
	    
	    add(new JLabel("Tipo de jogo:"), c);
	    add(new JLabel("Quem come\u00E7a?:"), c);
	    
	    c.gridx = 1;
	    c.gridy = 0;
	    c.weightx = 1.0;
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.anchor = GridBagConstraints.CENTER;
	    
		this.gameType = new JComboBox<String>();
		this.gameType.setModel(new DefaultComboBoxModel<String>(new String[] {"Multiplayer", "Single Player"}));
		this.gameType.addItemListener(this);
		add(this.gameType, c);
		
		c.gridx = 1;
	    c.gridy = GridBagConstraints.RELATIVE;
	    
		this.whomBegins = new JComboBox<String>();
		this.whomBegins.setModel(new DefaultComboBoxModel<String>(new String[] {"IA", "User"}));
		this.whomBegins.setSelectedIndex(0);
		add(this.whomBegins, c);
		
		this.gameType.setSelectedIndex(0);//1
	}
	
	public void itemStateChanged(ItemEvent event) {
		if (event.getStateChange() == ItemEvent.SELECTED) {
			if(event.getItem().toString() == "Multiplayer"){
				this.whomBegins.setEnabled(false);
			}else{
				this.whomBegins.setEnabled(true);
			}
		}
	}
	
	// ------- Getters and Setters ------- \\
	public String getGameType(){
		return (String) this.gameType.getSelectedItem();
	}
	
	public String getWhomBegins(){
		return (String) this.whomBegins.getSelectedItem();
	}
}
