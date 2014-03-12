package br.ufes.inf.nemo.antipattern.wizard.freerole;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import RefOntoUML.Property;
import br.ufes.inf.nemo.antipattern.freerole.FreeRoleOccurrence;

public class FreeRoleIndependenceTable{

	private FreeRoleOccurrence freeRole;
	private Table table;
	
	@SuppressWarnings("unused")
	public FreeRoleIndependenceTable (Composite parent, int args, FreeRoleOccurrence freeRole) 
	{		
		table = new Table(parent, args);
		
		this.freeRole = freeRole;		
		
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		String columnName1 = "Type";
		TableColumn tableColumn1 = new TableColumn(table, SWT.CENTER);
		tableColumn1.setWidth(50);
		tableColumn1.setText(columnName1);
		
		String columnName2 = "Stereotype";
		TableColumn tableColumn2 = new TableColumn(table, SWT.CENTER);
		tableColumn2.setWidth(70);
		tableColumn2.setText(columnName2);
		
		String columnName3 = "Mult. Relator End";
		TableColumn tableColumn3 = new TableColumn(table, SWT.CENTER);
		tableColumn3.setWidth(110);
		tableColumn3.setText(columnName3);
		
		String columnName4 = "Mult. Mediated End";
		TableColumn tableColumn4 = new TableColumn(table, SWT.CENTER);
		tableColumn4.setWidth(110);
		tableColumn4.setText(columnName4);
		
		String columnName5 = "Create Material?";
		TableColumn tableColumn5 = new TableColumn(table, SWT.CENTER);
		tableColumn5.setWidth(90);
		tableColumn5.setText(columnName5);
		
		int tableWidth = 0;		
		for (TableColumn tc : table.getColumns()) {
			tableWidth+=tc.getWidth();
		}
		
		table.setSize(418, 117);
		
		fulfillLines();
	}
		
	public void fulfillLines(){
	
		for(Property p: freeRole.getDefiningRelatorEnds())
		{
			TableItem tableItem = new TableItem(table,SWT.NONE);	
						
			// Type
			TableEditor editor = new TableEditor(table);
		    editor.grabHorizontal = true;
			editor.horizontalAlignment = SWT.CENTER;
			tableItem.setText(0, p.getType().getName());
			
			// Stereotype
			editor = new TableEditor(table);
			Button checkButton = new Button(table, SWT.CHECK);
			checkButton.pack();
			editor.minimumWidth = checkButton.getSize().x;
			editor.horizontalAlignment = SWT.CENTER;
			editor.setEditor(checkButton, tableItem, 1);
			tableItem.setData("1", checkButton);
			
			// Mult. Relator End
			editor = new TableEditor(table);
			Combo combo = new Combo(table, SWT.NONE);
			combo.setItems(new String[] {"0..1", "1", "1..*", "0..*"});
			combo.select(2);
			combo.pack();
			editor.grabHorizontal = true;
			editor.horizontalAlignment = SWT.CENTER;
			editor.setEditor(combo, tableItem, 2);
			tableItem.setData("2", combo);
			
			// Mult. Mediated End
			editor = new TableEditor(table);
			combo = new Combo(table, SWT.NONE);
			combo.setItems(new String[] {"0..1", "1", "1..*", "0..*"});
			combo.select(2);	
			combo.pack();
			editor.grabHorizontal = true;
			editor.horizontalAlignment = SWT.CENTER;
			editor.setEditor(combo, tableItem, 3);
			tableItem.setData("3", combo);
			
			// Create Material?
			editor = new TableEditor(table);
			checkButton = new Button(table, SWT.CHECK);
			checkButton.pack();
			editor.minimumWidth = checkButton.getSize().x;
			editor.horizontalAlignment = SWT.CENTER;
			editor.setEditor(checkButton, tableItem, 4);
			tableItem.setData("4", checkButton);						
		}
	}
	
	public Table getTable() {
		return table;
	}
	
	public ArrayList<Boolean> getUse()
	{
		ArrayList<Boolean> result = new ArrayList<Boolean>();		
		for (TableItem ti : table.getItems()){	
			Button button = (Button) ti.getData("1");			
			result.add(button.getSelection());
		}
		return result;	
	}
	
	public ArrayList<String> getMultRoleEnd()
	{
		ArrayList<String> result = new ArrayList<String>();		
		for (TableItem ti : table.getItems()){	
			Combo combo = (Combo) ti.getData("4");
			result.add(combo.getText());
		}
		return result;	
	}
	
	public ArrayList<String> getMultRelatorEnd()
	{
		ArrayList<String> result = new ArrayList<String>();		
		for (TableItem ti : table.getItems()){	
			Combo combo = (Combo) ti.getData("5");
			result.add(combo.getText());
		}
		return result;	
	}
	
	public ArrayList<String> getRelatorNames()
	{
		ArrayList<String> result = new ArrayList<String>();		
		for (TableItem ti : table.getItems()){	
			Text text = (Text) ti.getData("3");			
			result.add(text.getText());
		}
		return result;	
	}
	
	public ArrayList<Boolean> getSpecialize()
	{
		ArrayList<Boolean> result = new ArrayList<Boolean>();		
		for (TableItem ti : table.getItems()){	
			Button button = (Button) ti.getData("2");			
			result.add(button.getSelection());
		}
		return result;	
	}	
}		 
