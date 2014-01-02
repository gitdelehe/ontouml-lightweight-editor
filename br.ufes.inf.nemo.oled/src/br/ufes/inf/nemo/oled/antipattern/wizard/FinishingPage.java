package br.ufes.inf.nemo.oled.antipattern.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.wb.swt.layout.grouplayout.GroupLayout;
import org.eclipse.wb.swt.layout.grouplayout.LayoutStyle;

public class FinishingPage extends WizardPage {

	public Label statusLabel;	
	public List modifiedList;			
	public List addedList;			
	public List removedList;	
	public StyledText rulesText;
	
	/**
	 * Create the wizard.
	 */
	public FinishingPage() {
		super("Finishing Page");
		setTitle("Finishing Page");
		setDescription("");
	}

	public void showStatus (boolean value)
	{
		statusLabel.setVisible(value);
	}
	
	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		
		Label introLabel = new Label(container, SWT.NONE);
		introLabel.setText("Congratulations! You successfully verified the antipattern.");
		
		Label modifiedLabel = new Label(container, SWT.NONE);
		modifiedLabel.setText("Modified elements:");
		
		Label addedLabel = new Label(container, SWT.NONE);
		addedLabel.setText("Added elements:");
		
		Label removedLabel = new Label(container, SWT.NONE);
		removedLabel.setText("Removed Elements:");
		
		modifiedList = new List(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
				
		addedList = new List(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		
		removedList = new List(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		
		Label rulesLabel = new Label(container, SWT.NONE);
		rulesLabel.setText("Added OCL rules:");
		
		rulesText = new StyledText(container, SWT.BORDER);
		
		statusLabel = new Label(container, SWT.NONE);
		statusLabel.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		statusLabel.setAlignment(SWT.CENTER);
		statusLabel.setText("This antipattern did not characterized an error.");
				
		GroupLayout gl_container = new GroupLayout(container);
		gl_container.setHorizontalGroup(
			gl_container.createParallelGroup(GroupLayout.LEADING)
				.add(gl_container.createSequentialGroup()
					.add(gl_container.createParallelGroup(GroupLayout.LEADING)
						.add(gl_container.createSequentialGroup()
							.add(10)
							.add(introLabel, GroupLayout.PREFERRED_SIZE, 552, GroupLayout.PREFERRED_SIZE))
						.add(gl_container.createSequentialGroup()
							.addContainerGap()
							.add(gl_container.createParallelGroup(GroupLayout.LEADING)
								.add(gl_container.createSequentialGroup()
									.add(gl_container.createParallelGroup(GroupLayout.LEADING, false)
										.add(modifiedLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.add(modifiedList, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.RELATED)
									.add(gl_container.createParallelGroup(GroupLayout.LEADING, false)
										.add(GroupLayout.TRAILING, addedLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.add(GroupLayout.TRAILING, addedList, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.UNRELATED)
									.add(gl_container.createParallelGroup(GroupLayout.TRAILING, false)
										.add(removedLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.add(removedList, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)))
								.add(gl_container.createParallelGroup(GroupLayout.TRAILING)
									.add(GroupLayout.LEADING, rulesLabel, GroupLayout.PREFERRED_SIZE, 551, GroupLayout.PREFERRED_SIZE)
									.add(GroupLayout.LEADING, rulesText, GroupLayout.PREFERRED_SIZE, 551, GroupLayout.PREFERRED_SIZE))
								.add(statusLabel, GroupLayout.PREFERRED_SIZE, 551, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_container.setVerticalGroup(
			gl_container.createParallelGroup(GroupLayout.LEADING)
				.add(gl_container.createSequentialGroup()
					.add(10)
					.add(introLabel)
					.add(18)
					.add(gl_container.createParallelGroup(GroupLayout.BASELINE)
						.add(modifiedLabel)
						.add(removedLabel)
						.add(addedLabel))
					.addPreferredGap(LayoutStyle.RELATED)
					.add(gl_container.createParallelGroup(GroupLayout.BASELINE, false)
						.add(modifiedList, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
						.add(addedList, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
						.add(removedList, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(LayoutStyle.RELATED)
					.add(rulesLabel)
					.addPreferredGap(LayoutStyle.RELATED)
					.add(rulesText, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.RELATED)
					.add(statusLabel)
					.addContainerGap())
		);
		gl_container.linkSize(new Control[] {modifiedList, addedList, removedList}, GroupLayout.VERTICAL);
		container.setLayout(gl_container);
	}

}
