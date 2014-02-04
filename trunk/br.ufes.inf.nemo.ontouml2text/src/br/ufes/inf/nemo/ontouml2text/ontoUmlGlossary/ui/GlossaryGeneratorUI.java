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
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import br.ufes.inf.nemo.common.ontoumlparser.OntoUMLParser;
import br.ufes.inf.nemo.ontouml2text.ontoUmlGlossary.OntoUmlGlossary;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GlossaryGeneratorUI extends JFrame {

	private OntoUMLParser parser;
	private JPanel contentPane;
	private JTextField outputFileName;
	private JTextField edtOutputDirectory;
	private JCheckBox chkAnalyseDescriptiveConsistency;
	private JButton btnSelectOutputDirectory;
	private JButton btnGenerateGlossary;
	private static final long serialVersionUID = 1L;
	
	private boolean doGlossaryGeneration;

	public GlossaryGeneratorUI(OntoUMLParser parser)
	{
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
		setBounds(100, 100, 450, 227);
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
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(chkAnalyseDescriptiveConsistency)
								.addContainerGap())
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(lblNewLabel)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
												.addComponent(edtOutputDirectory, Alignment.LEADING)
												.addComponent(outputFileName, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE))
											.addPreferredGap(ComponentPlacement.RELATED)
											.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addComponent(btnSelectOutputDirectory, 0, 0, Short.MAX_VALUE))))
									.addContainerGap())
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblOutputDirectory)
									.addContainerGap(333, Short.MAX_VALUE))))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(btnGenerateGlossary)
							.addContainerGap())))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(outputFileName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblOutputDirectory)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(edtOutputDirectory, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSelectOutputDirectory))
					.addGap(18)
					.addComponent(chkAnalyseDescriptiveConsistency)
					.addPreferredGap(ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
					.addComponent(btnGenerateGlossary)
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

			@Override
			public void actionPerformed(ActionEvent e) {
				
				//GlossaryGeneratorAnalisysUI gga = new GlossaryGeneratorAnalisysUI();
				

				if(outputFileName.getText().equals(""))
					this.showErrorMessage(this,"There is no output name.","Output Name Error",JOptionPane.ERROR_MESSAGE,null);	
				
				else{				
					OntoUmlGlossary.xmiToText(parser, outputFileName.getText());
					//System.out.println("tamanho " + OntoUmlGlossary.getConceptsWithoutDesc().size());
					dispose();
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
