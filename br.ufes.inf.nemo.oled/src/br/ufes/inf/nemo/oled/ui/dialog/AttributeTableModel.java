package br.ufes.inf.nemo.oled.ui.dialog;

import java.text.ParseException;
import java.util.Collection;

import org.eclipse.emf.common.util.EList;

import RefOntoUML.Classifier;
import RefOntoUML.DataType;
import RefOntoUML.Property;
import RefOntoUML.impl.DataTypeImpl;
import br.ufes.inf.nemo.oled.util.ModelHelper;

/**
 * This class implements a BaseTableModel for class RefOntoUML.Proprties
 * 
 * @author Antognoni Albuquerque, John Guerson
 * 
 * @version 1.0
 */
public class AttributeTableModel extends BaseTableModel {
	
	private static final long serialVersionUID = 156864519388945910L;
	private EList<Property> attributes;
	public static boolean isPrimitive = true;
	
	public AttributeTableModel(Classifier owner)
	{
		super(new String[]{"Name", "Type", "Lower/Upper","Derived","Read Only", "Subsetted","Redefined"});
		
		if(owner instanceof DataTypeImpl) attributes = ((DataType) owner).getOwnedAttribute();
		else attributes = ((RefOntoUML.Class) owner).getOwnedAttribute();
	}

	public EList<Property> getEntries()
	{
		return attributes;
	}
	
	/**
	 * Adds an entry (item) to the model.
	 * @param entry the entry to add
	 */
	public void addEntry(Object entry)
	{
		int size = attributes.size();
		attributes.add((Property) entry);
		fireTableRowsInserted(size, size);
	}

	@Override
	public void moveUpEntry(int index) {
		attributes.move(index-1, index);
		fireTableRowsUpdated(index-1, index);
	}

	@Override
	public void moveDownEntry(int index) {
		attributes.move(index+1, index);
		fireTableRowsUpdated(index+1, index);
	}

	@Override
	public void removeEntryAt(int index) {
		attributes.remove(index);
		fireTableRowsDeleted(index, index);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void addEmptyEntry() {
		Property property = ModelHelper.getFactory().createProperty();
		DataType type = null;		
		if (isPrimitive) type = ModelHelper.getFactory().createPrimitiveType();		
		else type = ModelHelper.getFactory().createDataType();
		type.setName("");
		property.setType(type);
		property.setName("");
		ModelHelper.setMultiplicity(property, 1, 1);		
		addEntry(property);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(attributes.size() > 0)
		{
			Property prp = (Property)attributes.get(rowIndex);			
			switch(columnIndex) {
				case 0: return prp.getName();
				case 1: return prp.getType().getName();
				case 2: return prp.getLower()+".."+prp.getUpper();
				case 3: return prp.isIsDerived();				
				case 4: return prp.isIsReadOnly();
				case 5: return prp.getSubsettedProperty();
				case 6: return prp.getRedefinedProperty();
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<?> getColumnClass(int columnIndex) {
        if(attributes.size() > 0)
		{
        	switch(columnIndex) {
				case 0: return String.class;
				case 1: return String.class;
				case 2: return String.class;
				case 3: return Boolean.class;
				case 4: return Boolean.class;
				case 5: return EList.class;
				case 6: return EList.class;
			}
		}
		return Object.class;
    }
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		Property property = (Property) attributes.get(rowIndex);
		if(columnIndex == 0) {
			property.setName((String) value);
		} 
		if(columnIndex == 1){
			DataType type = (DataType) property.getType();
			type.setName((String) value);
		}
		if(columnIndex == 2){
			try {
				ModelHelper.setMultiplicityFromString(property, (String)value);
			} catch (ParseException e) {
				System.err.println(e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
		if(columnIndex == 3){
			property.setIsDerived((Boolean)value);
		}
		if(columnIndex == 4){
			property.setIsReadOnly((Boolean)value);
		}
		if(columnIndex == 5){
			property.getSubsettedProperty().addAll((Collection<Property>)value);
		}
		if(columnIndex == 6){
			property.getRedefinedProperty().addAll((Collection<Property>)value);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) 
	{ 
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getColumnName(int columnIndex) {
		return columns[columnIndex];
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getRowCount() { return attributes.size(); }

	/**
	 * {@inheritDoc}
	 */
	public int getColumnCount() { return columns.length; }

}
