package br.ufes.inf.nemo.antipattern.wizard.undefphase;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import RefOntoUML.Classifier;
import br.ufes.inf.nemo.antipattern.undefphase.UndefPhaseOccurrence;
import br.ufes.inf.nemo.common.ontoumlparser.OntoUMLNameHelper;

public class ModeTable {

	private UndefPhaseOccurrence up;
	private Table table;

	public ModeTable (Composite parent, int args, UndefPhaseOccurrence up) 
	{		
		table = new Table(parent, args);
		this.up = up;
		table.setHeaderVisible(true);		
		table.setLinesVisible(true);
				
		String columnName1 = "Mode Name";
		TableColumn tableColumn1 = new TableColumn(table, SWT.CENTER);
		tableColumn1.setWidth(100);
		tableColumn1.setText(columnName1);
		
		String columnName3 = "Multiplicity (Mode End)";
		TableColumn tableColumn3 = new TableColumn(table, SWT.CENTER);
		tableColumn3.setWidth(180);
		tableColumn3.setText(columnName3);
		
		String columnName2 = "Characterized Phase";
		TableColumn tableColumn2 = new TableColumn(table, SWT.CENTER);
		tableColumn2.setWidth(160);
		tableColumn2.setText(columnName2);

	}		

	/*
	 
	  TableItem item = items[i]; 
      TableEditor editor = new TableEditor(table); 
      
      CCombo combo = new CCombo(table, SWT.NONE); 
      combo.setText("Combo " + i); 
      combo.add("item 1"); 
      combo.add("item 2"); 
      
      editor.grabHorizontal = true; 
      editor.setEditor(combo, item, 0); 
      
      item.setData("ComboEditor", editor); 
      editor = new TableEditor(table); 
      Text text = new Text(table, SWT.NONE); 
      text.setText("Text " + i); 
      editor.grabHorizontal = true; 
      editor.setEditor(text, item, 1); 
      item.setData("TextEditor", editor); 
      editor = new TableEditor(table); 
      Button button = new Button(table, SWT.CHECK); 
      button.setText("" + i); 
      button.pack(); 
      editor.minimumWidth = button.getSize().x; 
      editor.horizontalAlignment = SWT.LEFT; 
      editor.setEditor(button, item, 2); 
      item.setData("ButtonEditor", editor); 
	 
	 */
	
	public void addNewMode(String name, String mult, int selectedPhase)
	{
		TableItem item = new TableItem(table, SWT.NONE);
	
		item.setText(0,name);
		item.setText(1,mult);
				
		//phase
		TableEditor editor = new TableEditor(table);
		Combo combo = new Combo(table, SWT.READ_ONLY);
		for(Classifier c: up.getPhases())
		{
			combo.add(OntoUMLNameHelper.getTypeAndName(c, true, true));
		}		
		combo.select(selectedPhase);
		combo.pack();
		editor.grabHorizontal = true;
		editor.horizontalAlignment = SWT.CENTER;
		editor.setEditor(combo, item, 2);
		item.setData("ComboEditor", editor);
					
		getTable().select(getTable().getItemCount()-1);
	}
	
	public void removeLine()
	{
		if(getTable().getSelectionIndex()>=0) {	
			for (int index : getTable().getSelectionIndices()) {
				TableEditor editor = (TableEditor) table.getItem(index).getData("ComboEditor");
				editor.getEditor().dispose();
				editor.dispose();
				table.remove(index);
			}
			
//			table.pack();
			table.update();
//			table.setRedraw(true);
//			table.redraw();
		}
	}
	
	/*
	
	TableEditor editor = (TableEditor) item.getData("ComboEditor"); 
                                    editor.getEditor().dispose(); 
                                    editor.dispose(); 
                                    editor = (TableEditor) item.getData("TextEditor"); 
                                    editor.getEditor().dispose(); 
                                    editor.dispose(); 
                                    editor = (TableEditor) item.getData("ButtonEditor"); 
                                    editor.getEditor().dispose(); 
                                    editor.dispose(); 
                                    table.remove(table.indexOf(item)); 
	
	*/
	public Table getTable() {
		return table;
	}

	public HashMap<String,String> getValues()
	{
		HashMap<String,String> map = new HashMap<String,String>();
		for (TableItem ti : table.getItems()){	
			String name = ti.getText(0);
			String cardinality = ti.getText(1);			
			if (name!= null && !name.isEmpty() && cardinality!=null && !cardinality.isEmpty()){
					map.put(name,cardinality);	
			}
		}
		return map;
	}
	
	public Classifier getPhase(int index)
	{		
		return up.getPhases().get(index);
	}
	
	public ArrayList<Classifier> getPhases()
	{
		ArrayList<Classifier> map = new ArrayList<Classifier>();
		for (TableItem ti : table.getItems()){			
			Classifier c = getPhase(((Combo)ti.getData("2")).getSelectionIndex());			
			if(c!=null)
				map.add(c);				
		}
		return map;
	}	
}