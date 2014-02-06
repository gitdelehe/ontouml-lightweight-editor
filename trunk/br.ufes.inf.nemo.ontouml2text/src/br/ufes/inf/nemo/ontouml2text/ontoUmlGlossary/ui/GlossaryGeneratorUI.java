package br.ufes.inf.nemo.ontouml2text.ontoUmlGlossary.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import br.ufes.inf.nemo.common.ontoumlparser.OntoUMLParser;
import br.ufes.inf.nemo.ontouml2text.ontoUmlGlossary.OntoUmlGlossary;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JComboBox;

public class GlossaryGeneratorUI extends JFrame {
	
	private OntoUmlGlossary ontoUmlGlossary;
	private OntoUMLParser parser;
	private JPanel contentPane;
	private JComboBox<String> cmbLanguage;
	private JTextField outputFileName;
	private JTextField edtOutputDirectory;
	private JCheckBox chkAnalyseDescriptiveConsistency;
	private JButton btnSelectOutputDirectory;
	private JButton btnGenerateGlossary;
	private static final long serialVersionUID = 1L;
	
	private boolean doGlossaryGeneration;
	private JButton btnCancel;

	public GlossaryGeneratorUI(OntoUMLParser parser){
		this();		
		this.parser = parser;
	}
	
	/**
	 * Create the frame.
	 */
	public GlossaryGeneratorUI() {
		
		setResizable(false);
		setType(Type.UTILITY);
		setTitle("OntoUML Glossary Generator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 401, 275);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblNewLabel = new JLabel("Output File Name");
		
		outputFileName = new JTextField();
		outputFileName.setColumns(10);
		
		JLabel lblOutputDirectory = new JLabel("Output Directory");
		
		edtOutputDirectory = new JTextField();
		edtOutputDirectory.setColumns(10);
		
		btnSelectOutputDirectory = new JButton("...");
		
		chkAnalyseDescriptiveConsistency = new JCheckBox("Analyse Descriptive Consistency");
		chkAnalyseDescriptiveConsistency.setSelected(true);
		
		btnGenerateGlossary = new JButton("Generate Glossary");
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		cmbLanguage = new JComboBox<String>();
		cmbLanguage.addItem("PortuguÍs - BR");
		cmbLanguage.setSelectedIndex(0);
		
		JLabel lblLanguage = new JLabel("Target Language");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(edtOutputDirectory, Alignment.LEADING)
								.addComponent(outputFileName, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 319, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addComponent(btnSelectOutputDirectory, GroupLayout.PREFERRED_SIZE, 30, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel)
								.addComponent(lblOutputDirectory)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(chkAnalyseDescriptiveConsistency)
									.addGap(154)))
							.addPreferredGap(ComponentPlacement.RELATED, 32, GroupLayout.PREFERRED_SIZE)))
					.addGap(8))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(92)
					.addComponent(btnGenerateGlossary)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnCancel)
					.addContainerGap(101, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(cmbLanguage, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(367, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblLanguage)
					.addContainerGap(328, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblLanguage)
					.addGap(9)
					.addComponent(cmbLanguage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(outputFileName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblOutputDirectory)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(edtOutputDirectory, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSelectOutputDirectory))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(chkAnalyseDescriptiveConsistency)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancel)
						.addComponent(btnGenerateGlossary))
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
		
		setLocationRelativeTo(null);
		
		btnSelectOutputDirectory.setActionCommand("outputSelection");
		btnGenerateGlossary.setActionCommand("generateGlossary");
		
		btnSelectOutputDirectory.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				  JFileChooser chooser = new JFileChooser();
			      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	
			      try {
			            int code = chooser.showOpenDialog(contentPane);
			            if (code == JFileChooser.APPROVE_OPTION) {
			               File selectedFile = chooser.getSelectedFile();
			               edtOutputDirectory.setText(selectedFile.getAbsolutePath());
			            }
			      } catch (Exception f) {
			         f.printStackTrace();
			      }	
			}
			
		});
		
		btnGenerateGlossary.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {

				if(outputFileName.getText().equals("") || edtOutputDirectory.getText().equals(""))
					this.showErrorMessage(this,"There is no output file name or output directory. Please, complete the fields.","Output Name Error",JOptionPane.ERROR_MESSAGE,null);	
				
				else{					
					if(cmbLanguage.getSelectedIndex() == 0){ // Brazilian Portuguese
						ontoUmlGlossary = new OntoUmlGlossary(OntoUmlGlossary.PT_BR, parser, outputFileName.getText(), edtOutputDirectory.getText()); 
					}
					
					if(chkAnalyseDescriptiveConsistency.isSelected()){
						SwingUtilities.invokeLater(new Runnable() {			
							public void run() {
								GlossaryGeneratorAnalisysUI analisys = new GlossaryGeneratorAnalisysUI(ontoUmlGlossary, ontoUmlGlossary.verifiyDescriptionConsistency());
								analisys.setVisible(true);	
							}
						});		
					}else{
						ontoUmlGlossary.modelToText();
					}
				}
			}

			private void showErrorMessage(ActionListener actionListener,
					String message, String title, int errorMessage,
					Object object) {
				JOptionPane.showMessageDialog(contentPane, message, title, 0);
				
			}
			
		});
		
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
	
	public String getOutputDirectory(){
		return edtOutputDirectory.getText();
	}
	
	public boolean doDescriptiveConsistency(){
		return chkAnalyseDescriptiveConsistency.isSelected();
	}
	
	public boolean doGlossaryGeneration(){
		return doGlossaryGeneration;
	}
}
