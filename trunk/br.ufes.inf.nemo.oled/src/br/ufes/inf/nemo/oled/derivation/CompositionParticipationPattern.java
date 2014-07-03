package br.ufes.inf.nemo.oled.derivation;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;
import javax.swing.JRadioButton;
import javax.swing.DefaultComboBoxModel;

import br.ufes.inf.nemo.oled.DiagramManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

public class CompositionParticipationPattern extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField lbl_base;
	private JTextField lbl_whole;
	private JTextField lbl_derived;
	JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Member Of");
	JComboBox cmb_whole = new JComboBox();
	ButtonGroup bt = new ButtonGroup();
	JRadioButton rdbtnNewRadioButton = new JRadioButton("Component Of");
	DiagramManager dm;
	JComboBox cmb_base = new JComboBox();
	Point2D.Double location;
	/**
	 * Launch the application.
	 */

	/**
	 * Create the dialog.
	 * @param dman 
	 */
	public CompositionParticipationPattern(DiagramManager dman) {
		dm=dman;
		setResizable(false);
		setTitle("Derivation by Participation - Part-Whole\r\n");
		setIconImage(Toolkit.getDefaultToolkit().getImage(CompositionParticipationPattern.class.getResource("/resources/icons/x16/sitemap.png")));
		setBounds(100, 100, 407, 493);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		JLabel lblNewLabel = new JLabel("Base Type");
		JLabel lblNewLabel_1 = new JLabel("Whole");
		JLabel lblNewLabel_3 = new JLabel("Derived Type");
		lbl_base = new JTextField();
		lbl_base.setText("Base");
		lbl_base.setColumns(10);
		lbl_whole = new JTextField();
		lbl_whole.setText("Whole");
		lbl_whole.setColumns(10);
		lbl_derived = new JTextField();
		lbl_derived.setText("Part");
		lbl_derived.setColumns(10);
		
		
		cmb_base.setModel(new DefaultComboBoxModel(new String[] {"Kind", "Collective", "Quantity", "Subkind", "Role", "Phase", "Category", "RoleMixin", "Mixin"}));
		
		
		cmb_whole.setModel(new DefaultComboBoxModel(new String[] {"Collective"}));
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon(CompositionParticipationPattern.class.getResource("/resources/figures/derivationbyparticipartion_comp.PNG")));
		
		
		rdbtnNewRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnNewRadioButton.isSelected()){
					cmb_whole.setModel(new DefaultComboBoxModel(new String[] {"Kind","Subkind","Role","Phase"}));
				}
			}
		});
		bt.add(rdbtnNewRadioButton);
		bt.add(rdbtnNewRadioButton_1);
		rdbtnNewRadioButton.setBackground(Color.WHITE);
		
		
		rdbtnNewRadioButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(rdbtnNewRadioButton_1.isSelected()){
					cmb_whole.setModel(new DefaultComboBoxModel(new String[] {"Collective"}));
				}
			}
		});
		rdbtnNewRadioButton_1.setSelected(true);
		rdbtnNewRadioButton_1.setBackground(Color.WHITE);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel_3)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(lbl_derived, Alignment.LEADING)
										.addComponent(lblNewLabel_1, Alignment.LEADING)
										.addComponent(lblNewLabel, Alignment.LEADING)
										.addComponent(lbl_base, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
										.addComponent(lbl_whole, Alignment.LEADING)
										.addComponent(rdbtnNewRadioButton_1, Alignment.LEADING)
										.addComponent(rdbtnNewRadioButton, Alignment.LEADING))
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(cmb_base, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(cmb_whole, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(20)
							.addComponent(lblNewLabel_2)))
					.addContainerGap(29, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(18)
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lbl_base, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(cmb_base, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_1)
					.addGap(5)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lbl_whole, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(cmb_whole, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_3)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lbl_derived, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(rdbtnNewRadioButton)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(rdbtnNewRadioButton_1)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(lblNewLabel_2)
					.addContainerGap())
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(Color.WHITE);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						DerivedTypesOperations.createParticipationComp(dm, lbl_base.getText(), lbl_whole.getText(), lbl_derived.getText(), cmb_base.getSelectedItem().toString(), cmb_whole.getSelectedItem().toString(), location);
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	public void setPosition(Point2D.Double point) {
		// TODO Auto-generated method stub
		location=point;
	}
}
