package br.ufes.inf.nemo.antipattern.wizard.binover;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import br.ufes.inf.nemo.antipattern.binover.BinOverAntipattern;
import br.ufes.inf.nemo.antipattern.binover.BinOverOccurrence;
import br.ufes.inf.nemo.antipattern.binover.BinOverOccurrence.BinaryPropertyValue;
import br.ufes.inf.nemo.common.ontoumlparser.OntoUMLNameHelper;

public class CyclicityPage extends BinOverPage {

	Button btnCyclic, btnAcyclic, btnNonCyclic;
	private Label lblIncompatibility;
	private Label lblCurrentValues;
	
	/**
	 * Create the wizard.
	 */
	public CyclicityPage(BinOverOccurrence binOver) {
		super("TransitivityPage", binOver);
		
		setTitle(BinOverAntipattern.getAntipatternInfo().getName()+" - Cyclicity");
		
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);

		setDescription("Binary Relation: "+OntoUMLNameHelper.getTypeAndName(binOver.getAssociation(), true, true)+
				   "\nCurrent Stereotype: "+getBinOverWizard().getCurrentStereotypeName(this));	
		
		StyledText styledText = new StyledText(container, SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
		styledText.setText(	"The last binary property we analyze in this antipattern is the Ciclicity."+
							"\r\n\r\n" +
							"Consider three distinct individuals: a, b and c. " +
							"If <"+OntoUMLNameHelper.getTypeAndName(binOver.getAssociation(), true, true)+"> connects a to b, and also b to c, " +
							"we can imply that the relation: ");
			
		styledText.setJustify(true);
		styledText.setBackground(container.getBackground());
		styledText.setBounds(10, 10, 754, 71);
				
		btnCyclic = new Button(container, SWT.RADIO);
		btnCyclic.setBounds(10, 87, 554, 16);
		btnCyclic.setText("Connects c to a (Cyclic)");
		
		btnAcyclic = new Button(container, SWT.RADIO);
		btnAcyclic.setText("DOES NOT connect c to a (Acyclic)");
		btnAcyclic.setBounds(10, 109, 554, 16);
		
		btnNonCyclic = new Button(container, SWT.RADIO);
		btnNonCyclic.setText("Nothing (Non-Cyclic)");
		btnNonCyclic.setBounds(10, 130, 554, 16);
		
		lblIncompatibility = new Label(container, SWT.WRAP);
		lblIncompatibility.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lblIncompatibility.setBounds(10, 152, 554, 16);
		lblIncompatibility.setText(	"(One or more options disable due to incompatibility with your previous answers.)");
		
		lblCurrentValues = new Label(container, SWT.WRAP | SWT.RIGHT);
		lblCurrentValues.setBounds(10, 256, 754, 15);
		lblCurrentValues.setText("Reflexivity = , Symmetry = , Transitivity =");
		
		setPageComplete(false);
		
		SelectionAdapter listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				if((btnCyclic.getSelection() || btnAcyclic.getSelection() || btnNonCyclic.getSelection()) && !isPageComplete())
					setPageComplete(true);
				else
					setPageComplete(false);
			}
		};
		
		btnCyclic.addSelectionListener(listener);
		btnAcyclic.addSelectionListener(listener);
		btnNonCyclic.addSelectionListener(listener);
	}
	
	public void updateUI(){
		
		BinaryPropertyValue reflexivityValue = getBinOverWizard().reflexivity,
							symmetryValue = getBinOverWizard().symmetry,
							transitivityValue = getBinOverWizard().transitivity;
		
		boolean cyclicAvailable = BinOverOccurrence.validCombination(reflexivityValue, symmetryValue, transitivityValue, BinaryPropertyValue.CYCLIC),
				acyclicAvailable = BinOverOccurrence.validCombination(reflexivityValue, symmetryValue, transitivityValue, BinaryPropertyValue.ACYCLIC),
				nonCyclicAvailable = BinOverOccurrence.validCombination(reflexivityValue, symmetryValue, transitivityValue, BinaryPropertyValue.NON_CYCLIC);
		
		if (!cyclicAvailable){
			btnCyclic.setEnabled(false);
			btnCyclic.setSelection(false);
		}
		else
			btnCyclic.setEnabled(true);
		
		if (!acyclicAvailable){
			btnAcyclic.setEnabled(false);
			btnAcyclic.setSelection(false);
		}
		else
			btnAcyclic.setEnabled(true);
		
		if (!nonCyclicAvailable){
			btnNonCyclic.setEnabled(false);
			btnNonCyclic.setSelection(false);
		}
		else
			btnNonCyclic.setEnabled(true);
	
		lblCurrentValues.setText("Reflexivity = "+getBinOverWizard().reflexivity+", Symmetry = "+getBinOverWizard().symmetry+", Transitivity = "+getBinOverWizard().transitivity);
		
		if( !cyclicAvailable || !acyclicAvailable || !nonCyclicAvailable ){
			lblIncompatibility.setVisible(true);
			lblCurrentValues.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		}
		else{
			lblIncompatibility.setVisible(false);
			lblCurrentValues.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		}
	}
	
	@Override
	public IWizardPage getNextPage(){

		if(!btnCyclic.getSelection() && !btnAcyclic.getSelection() && !btnNonCyclic.getSelection())
			return null;
		
		if(btnCyclic.getSelection())
			getBinOverWizard().cyclicity = BinaryPropertyValue.CYCLIC;
		else if(btnAcyclic.getSelection())
			getBinOverWizard().cyclicity = BinaryPropertyValue.ACYCLIC;
		else if(btnNonCyclic.getSelection())
			getBinOverWizard().cyclicity = BinaryPropertyValue.NON_CYCLIC;
		
		if (getBinOverWizard().possibleStereotypes(getBinOverWizard().cyclicity).contains(getBinOverWizard().getCurrentStereotype(this))){
			BinOverAction action = new BinOverAction(binOver);
			action.setBinaryProperty(getBinOverWizard().cyclicity);
			getBinOverWizard().replaceAction(4, action);
			return getBinOverWizard().getFinishing();
		}
		else
			return getBinOverWizard().getCyclicityChangePage(getBinOverWizard().cyclicity);
	}

}