package br.ufes.inf.nemo.antipattern.wizard.wholeover;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

import br.ufes.inf.nemo.antipattern.wholeover.WholeOverOccurrence;

/**
 * @author Tiago Sales
 * @author John Guerson
 *
 */

public class WholeOverFirstPage extends WholeOverPage {

	public Button btnYes;
	public Button btnNo;

	public WholeOverFirstPage(WholeOverOccurrence wholeOver) 
	{
		super(wholeOver, "firstPage");
		setDescription("Whole: "+wholeOver.getParser().getStringRepresentation(wholeOver.getWhole()));
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		
		StyledText styledText = new StyledText(container, SWT.WRAP | SWT.H_SCROLL);
		styledText.setMarginColor(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		styledText.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		String text="To start the analysis, lets focus on the part types.\n\n" +
					"Is it possible for an object to be a part of wholes through relations";
		
		
		for (int i = 0; i < wholeOver.getAllPartEnds().size(); i++) {
			text+=" "+wholeOver.getParser().getStringRepresentation(wholeOver.getAllPartEnds().get(i).getAssociation())+
				  " ( as instance of "+wholeOver.getParser().getStringRepresentation(wholeOver.getAllPartEnds().get(i).getType())+")";
			if(i==wholeOver.getAllPartEnds().size()-2)
				text+=" and";
			else if (i<wholeOver.getAllPartEnds().size()-2)
				text+=",";
				
		}
		text+=" at the same time?";

		styledText.setText(text);
		styledText.setEditable(false);
		styledText.setBounds(10, 10, 554, 123);
		
		btnYes = new Button(container, SWT.RADIO);
		btnYes.setText("Yes");
		btnYes.setBounds(10, 139, 449, 16);
		
		btnNo = new Button(container, SWT.RADIO);
		btnNo.setText("No");
		btnNo.setBounds(10, 161, 449, 16);
	}
	
	@Override
	public IWizardPage getNextPage() {
		
		if(btnYes.getSelection()) {
			getWizard().getPage("thirdPage");
		}
		if(btnNo.getSelection()) {
			getWizard().getPage("secondPage");
		}
		
		return super.getNextPage();
	}
}
