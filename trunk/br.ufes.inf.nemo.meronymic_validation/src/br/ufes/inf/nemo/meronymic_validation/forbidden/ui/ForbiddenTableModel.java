package br.ufes.inf.nemo.meronymic_validation.forbidden.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

import RefOntoUML.Meronymic;
import br.ufes.inf.nemo.common.ontoumlparser.OntoUMLNameHelper;
import br.ufes.inf.nemo.meronymic_validation.forbidden.ForbiddenMemberOf;
import br.ufes.inf.nemo.meronymic_validation.forbidden.ForbiddenMeronymic;

public class ForbiddenTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = -1928067858298060225L;
	
	private ArrayList<ForbiddenMeronymic<? extends Meronymic>> forbiddenMeronymic = new ArrayList<>();
	private static final String[] 	COLUMNS = {"Whole","Part","Name","Stereotype","Location","Description","Fix"};
	private static final Class<?>[] COLUMN_TYPES = new Class<?>[] {String.class, String.class, String.class, String.class, String.class, String.class, JButton.class};

	public ForbiddenTableModel() { 
	}

	public ForbiddenTableModel(List<ForbiddenMeronymic<?>> existingDataList) {
		forbiddenMeronymic.addAll(existingDataList);
	}  
	
	@Override
	public int getRowCount() {
		return forbiddenMeronymic.size();
	}

	@Override
	public int getColumnCount() {
		return COLUMNS.length;
	}
	@Override
	public String getColumnName(int col) {
		return COLUMNS[col];
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
	   return COLUMN_TYPES[columnIndex];
	}
	
	public void addRow(ForbiddenMeronymic<? extends Meronymic> forbidden) {
		forbiddenMeronymic.add(forbidden);
		fireTableDataChanged();
	}
	
	public ForbiddenMeronymic<?> getRow(int i) {
		return forbiddenMeronymic.get(i);
	}
	
	public void addRows(List<ForbiddenMeronymic<? extends Meronymic>> list) {
		forbiddenMeronymic.addAll(list);
		fireTableDataChanged();
	}
	
	public ForbiddenMeronymic<?> removeRow(int row) {
		   ForbiddenMeronymic<?> removed = forbiddenMeronymic.remove(row);
		   fireTableDataChanged();
		   return removed;
	}
	
	public void clear() {
		forbiddenMeronymic.clear();
		fireTableDataChanged();
	}
	
//	@Override
//	public void setValueAt(Object value, int row, int col) {
//		ForbiddenMeronymic<?> forbidden = forbiddenMeronymic.get(row);
//        
//		switch (col) {
//		case 6:
//			forbidden.setAllowed((boolean) value);
//			break;
//
//		default:
//			break;
//		}
//		
//		fireTableCellUpdated(row, col);
//    }
	
	@Override
	public Object getValueAt(int row, int col) {
		ForbiddenMeronymic<?> forbidden = forbiddenMeronymic.get(row);
		   switch (col) {
		    case 0:
		    	return OntoUMLNameHelper.getName(forbidden.getWhole(), false, false);
		    case 1:
		    	return OntoUMLNameHelper.getName(forbidden.getPart(), false, false);
		    case 2:
		    	return OntoUMLNameHelper.getName(forbidden.getMeronymic(), false, false);
		    case 3:
		    	return OntoUMLNameHelper.getTypeName(forbidden.getMeronymic(), false);
		    case 4:
		    	return OntoUMLNameHelper.getPath(forbidden.getMeronymic());
		    case 5:
		    	return forbidden.getDescription();
		    case 6:
		    	return createFixMeButton(row);
		    default:
		    	return null;
		   }
	}

	private JButton createFixMeButton(final int row) {
		JButton button = new JButton("FixMe"+row);
        
		button.addActionListener(new ActionListener() {
        
			public void actionPerformed(ActionEvent arg0) {
                FixIntranstiveMemberOfFrame frame = new FixIntranstiveMemberOfFrame((ForbiddenMemberOf) forbiddenMeronymic.get(row));
                frame.setAlwaysOnTop(true);
                frame.setVisible(true);
                
            }
        });
		
        return button;
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}
	

	
	
}
