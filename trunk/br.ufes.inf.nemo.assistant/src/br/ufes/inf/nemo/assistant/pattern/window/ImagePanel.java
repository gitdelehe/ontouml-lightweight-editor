package br.ufes.inf.nemo.assistant.pattern.window;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	public enum PatternType {
		RelatorCreation,SubkindCreation
	}

	public ImagePanel(PatternType type){
		ImageIcon iconLogo = null; 

		if(type.equals(PatternType.RelatorCreation))
			iconLogo = new ImageIcon(getClass().getClassLoader().getResource("resource/RelatorCreation.png"));
		else if(type.equals(PatternType.SubkindCreation))
			iconLogo = new ImageIcon(getClass().getClassLoader().getResource("resource/SubkindCreation.png"));

		setLayout(null);

		Image img = iconLogo.getImage() ;  
		Image newimg = img.getScaledInstance( 445, 259,  java.awt.Image.SCALE_SMOOTH ) ;  
		iconLogo = new ImageIcon( newimg );

		JPanel imagePanel = new JPanel();
		imagePanel.setBackground(Color.WHITE);
		imagePanel.setBounds(0, 18, 445, 259);
		add(imagePanel);
		JLabel lblImg = new JLabel(iconLogo);
		imagePanel.add(lblImg);

		JLabel lblPatternStructure = new JLabel("Pattern Structure");
		lblPatternStructure.setBounds(3, 1, 166, 14);
		add(lblPatternStructure);
	}

}
