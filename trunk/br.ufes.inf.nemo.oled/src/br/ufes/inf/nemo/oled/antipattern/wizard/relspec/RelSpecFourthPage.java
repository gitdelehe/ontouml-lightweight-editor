package br.ufes.inf.nemo.oled.antipattern.wizard.relspec;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

import br.ufes.inf.nemo.antipattern.relspec.RelSpecOccurrence;

/**
 * @author Tiago Sales
 * @author John Guerson
 *
 */

public class RelSpecFourthPage extends RelSpecPage {

	//GUI
	public Button btnSpecificSource;
	public Button btnSpecificBoth;
	public Button btnSpecificTarget;
	public Button btnGeneralSource;
	public Button btnGeneralBoth;
	public Button btnGeneralTarget;
	
	/**
	 * Create the wizard.
	 */
	public RelSpecFourthPage(RelSpecOccurrence relSpec) 
	{
		super(relSpec);	
			
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		
		StyledText styledText = new StyledText(container, SWT.WRAP);
		styledText.setMarginColor(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		styledText.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		styledText.setText(	"For which ends would like to create subtypes?");

		styledText.setEditable(false);
		styledText.setBounds(10, 10, 554, 16);
		
		btnSpecificSource = new Button(container, SWT.RADIO);
		btnSpecificSource.setBounds(10, 104, 449, 16);
		btnSpecificSource.setText(relSpec.getParser().getStringRepresentation(relSpec.getSpecificSource())+" (source end of "+relSpec.getParser().getStringRepresentation(relSpec.getSpecific())+")");
		btnSpecificSource.setSelection(true);
		
		btnSpecificTarget = new Button(container, SWT.RADIO);
		btnSpecificTarget.setText(relSpec.getParser().getStringRepresentation(relSpec.getSpecificTarget())+" (target end of "+relSpec.getParser().getStringRepresentation(relSpec.getSpecific())+")");
		btnSpecificTarget.setBounds(10, 126, 449, 16);
		
		btnSpecificBoth = new Button(container, SWT.RADIO);
		btnSpecificBoth.setText(relSpec.getParser().getStringRepresentation(relSpec.getSpecificSource())+" and "+relSpec.getParser().getStringRepresentation(relSpec.getSpecificTarget())+" (both ends of "+relSpec.getParser().getStringRepresentation(relSpec.getSpecific())+")");
		btnSpecificBoth.setBounds(10, 148, 449, 16);
		
		btnGeneralSource = new Button(container, SWT.RADIO);
		btnGeneralSource.setBounds(10, 38, 449, 16);
		btnGeneralSource.setText(relSpec.getParser().getStringRepresentation(relSpec.getGeneralSource())+" (source end of "+relSpec.getParser().getStringRepresentation(relSpec.getGeneral())+")");
		
		btnGeneralTarget = new Button(container, SWT.RADIO);
		btnGeneralTarget.setText(relSpec.getParser().getStringRepresentation(relSpec.getGeneralTarget())+" (target end of "+relSpec.getParser().getStringRepresentation(relSpec.getGeneral())+")");
		btnGeneralTarget.setBounds(10, 60, 449, 16);
		
		btnGeneralBoth = new Button(container, SWT.RADIO);
		btnGeneralBoth.setText(relSpec.getParser().getStringRepresentation(relSpec.getGeneralSource())+" and "+relSpec.getParser().getStringRepresentation(relSpec.getGeneralTarget())+" (both ends of "+relSpec.getParser().getStringRepresentation(relSpec.getGeneral())+")");
		btnGeneralBoth.setBounds(10, 82, 449, 16);
		
		
	}
	
	@Override
	public IWizardPage getNextPage() {
		
		if(btnGeneralSource.getSelection() || btnGeneralBoth.getSelection()) {
			// Action =====================
			//TODO: specialize <GeneralSourceEnd>
			//TODO: <General> redefines <Specific>, context newGeneralSourceSubtype
		}
		if(btnGeneralTarget.getSelection() || btnGeneralBoth.getSelection()) {
			// Action =====================
			//TODO: specialize <GeneralTargetEnd>
			if(!btnGeneralBoth.getSelection()){
			//TODO: <General> redefines <Specific>, context newGeneralTargetSubtype
			}
		}
		if(btnSpecificSource.getSelection() || btnSpecificBoth.getSelection()) {
			// Action =====================
			//TODO: specialize <SpecificSourceEnd>
			//TODO: <Specific> redefines <General>, context newSpecificSourceSubtype
		}
		if(btnSpecificTarget.getSelection() || btnSpecificBoth.getSelection()) {
			// Action =====================
			//TODO: specialize <SpecificTargetEnd>
			if(!btnSpecificBoth.getSelection()){
			//TODO: <Specific> redefines <General>, context newSpecificTargetSubtype
			}
		}
		
		return getRelSpecWizard().getFinishing();
		
	}
}
