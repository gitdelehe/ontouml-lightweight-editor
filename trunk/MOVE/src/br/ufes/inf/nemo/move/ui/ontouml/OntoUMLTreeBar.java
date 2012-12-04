package br.ufes.inf.nemo.move.ui.ontouml;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 * @author John Guerson
 */

public class OntoUMLTreeBar extends JPanel {
	
	private static final long serialVersionUID = -115797584019893402L;
	
	public JTextField textPath;	
	public JButton btnOpen;
	public JButton btnVerify;
	public JButton btnShowUnique;
	
	public JButton btnCompleteSelect;
	public JPopupMenu popupCompleteSelect;	
	
	public JMenuItem menuItemDefault;
	public JMenuItem menuItemAllAncestors;
	public JMenuItem menuItemSortalAncestors;
	public JMenuItem menuItemAllDescendants;
	
	public OntoUMLTreeBar() 
	{		
		textPath = new JTextField();
		textPath.setBackground(Color.WHITE);
		textPath.setEditable(false);
		textPath.setColumns(10);
		
		setPreferredSize(new Dimension(360, 80));
		
		btnOpen = new JButton("");
		btnOpen.setToolTipText("Open OntoUML Model");
		btnOpen.setIcon(new ImageIcon(OntoUMLTreeBar.class.getResource("/resources/br/ufes/inf/nemo/move/open-16x16.png")));
		
		btnVerify = new JButton("");
		btnVerify.setToolTipText("Verify All Model Sintactically");
		btnVerify.setIcon(new ImageIcon(OntoUMLTreeBar.class.getResource("/resources/br/ufes/inf/nemo/move/check-16x16.png")));
		
		btnShowUnique = new JButton("");
		btnShowUnique.setToolTipText("Show Aliases");
		btnShowUnique.setIcon(new ImageIcon(OntoUMLTreeBar.class.getResource("/resources/br/ufes/inf/nemo/move/visible-16x16.png")));
		
		btnCompleteSelect = new JButton("");
		btnCompleteSelect.setIcon(new ImageIcon(OntoUMLTreeBar.class.getResource("/resources/br/ufes/inf/nemo/move/selection-16x16.png")));
		btnCompleteSelect.setToolTipText("Complete Selections");
		btnCompleteSelect.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent event) 
			{
				popupCompleteSelect.show(btnCompleteSelect, 10, 10);
		   	    btnCompleteSelect.repaint();
		   	    btnCompleteSelect.validate();
			}
		});
		
		popupCompleteSelect = new JPopupMenu();
		
		menuItemDefault = new JMenuItem("Complete selection with mandatory dependencies");
		menuItemAllAncestors = new JMenuItem("Complete selection with all ancestors");
		menuItemAllDescendants = new JMenuItem("Complete selection with all descendants");
		menuItemSortalAncestors = new JMenuItem("Complete selection with ancestors until a SubstanceSortal");		
		
		popupCompleteSelect.add(menuItemDefault);
		popupCompleteSelect.add(menuItemAllAncestors);
		popupCompleteSelect.add(menuItemSortalAncestors);
		popupCompleteSelect.add(menuItemAllDescendants);
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(textPath, GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnOpen, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnVerify, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnShowUnique, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCompleteSelect, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(textPath, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnVerify, GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
						.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnOpen, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
						.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(btnCompleteSelect, GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
								.addComponent(btnShowUnique, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))))
					.addContainerGap())
		);
		setLayout(groupLayout);
							
	}	
}
