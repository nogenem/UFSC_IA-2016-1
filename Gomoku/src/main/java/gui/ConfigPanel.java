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

/**
 * Classe reponsável por criar o painel de configurações do jogo.
 * 
 * @author Gilney N. Mathias
 */
@SuppressWarnings("serial")
public class ConfigPanel extends JPanel implements ItemListener {
	
	private JComboBox<String> gameMode;//Multiplayer, Single Player
	private JComboBox<String> whomBegins;//IA, User
	
	/**
	 * Construtor da classe e também responsável pela criação dos 
	 *  componentes do painel.
	 */
	public ConfigPanel() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
	    c.gridy = GridBagConstraints.RELATIVE;
	    c.gridwidth = 1;
	    c.gridheight = 1;
	    c.insets = new Insets(2, 2, 2, 2);
	    c.anchor = GridBagConstraints.EAST;
	    
	    add(new JLabel("Modo de jogo:"), c);
	    add(new JLabel("Quem come\u00E7a?:"), c);
	    
	    c.gridx = 1;
	    c.gridy = 0;
	    c.weightx = 1.0;
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.anchor = GridBagConstraints.CENTER;
	    
		this.gameMode = new JComboBox<String>();
		this.gameMode.setModel(new DefaultComboBoxModel<String>(new String[] {"Multiplayer", "Single Player"}));
		this.gameMode.addItemListener(this);
		add(this.gameMode, c);
		
		c.gridx = 1;
	    c.gridy = GridBagConstraints.RELATIVE;
	    
		this.whomBegins = new JComboBox<String>();
		this.whomBegins.setModel(new DefaultComboBoxModel<String>(new String[] {"IA", "User"}));
		this.whomBegins.setSelectedIndex(0);
		this.whomBegins.setEnabled(false);
		add(this.whomBegins, c);
		
		this.gameMode.setSelectedIndex(1);//1
	}
	
	/**
	 * Habilita ou desabilita a comboBox para escolher se a IA começa jogando
	 *  dependendo do tipo de jogo escolhido pelo usuário.
	 */
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
	public String getGameMode(){
		return (String) this.gameMode.getSelectedItem();
	}
	
	public String getWhomBegins(){
		return (String) this.whomBegins.getSelectedItem();
	}
}
