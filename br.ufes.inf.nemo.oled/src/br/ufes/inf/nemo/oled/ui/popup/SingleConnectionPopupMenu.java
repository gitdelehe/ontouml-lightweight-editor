package br.ufes.inf.nemo.oled.ui.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import br.ufes.inf.nemo.oled.draw.Connection;
import br.ufes.inf.nemo.oled.umldraw.structure.AssociationElement;
import br.ufes.inf.nemo.oled.util.AppCommandListener;
import br.ufes.inf.nemo.oled.util.ApplicationResources;
import br.ufes.inf.nemo.oled.util.IconLoader;

public class SingleConnectionPopupMenu extends JPopupMenu implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Set<AppCommandListener> commandListeners = new HashSet<AppCommandListener>();
	private Connection con;
	final JMenuItem showRolesItem;
	final JMenuItem showNameItem;
	final JMenuItem showMultiplicitiesItem;
	final JMenuItem showStereotypeItem;
	
	public SingleConnectionPopupMenu()
	{		
		JMenuItem propertyItem = createMenuItem(this, "editproperties");
		propertyItem.setAccelerator(KeyStroke.getKeyStroke("F9"));		
		
		addSeparator();
		
		createMenuItem(this, "resetpoints");
		//if (conn.isRectilinear()) {
			createMenuItem(this, "recttodirect");
		//} else {
			createMenuItem(this, "directtorect");
		//}
		//addNavigabilityMenu(this, (UmlConnection) conn);
		addSeparator();
		
		JMenu visibilityMenu = new JMenu(ApplicationResources.getInstance().getString("submenu.visibility.name"));
		add(visibilityMenu);
			
		showRolesItem = createCheckBoxMenuItem(visibilityMenu, "visibility.showroles");
		showRolesItem.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if (con instanceof AssociationElement) ((AssociationElement)con).setShowRoles(showRolesItem.isSelected());
			}
		});
		
		showMultiplicitiesItem = createCheckBoxMenuItem(visibilityMenu, "visibility.showmultiplicities");
		showMultiplicitiesItem.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if (con instanceof AssociationElement) ((AssociationElement)con).setShowMultiplicities(showMultiplicitiesItem.isSelected());
			}
		});
		
		showNameItem = createCheckBoxMenuItem(visibilityMenu, "visibility.showname");
		showNameItem.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if (con instanceof AssociationElement) ((AssociationElement)con).setShowName(showNameItem.isSelected());
			}
		});
			
		showStereotypeItem = createCheckBoxMenuItem(visibilityMenu, "visibility.showstereotype");
		showStereotypeItem.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if (con instanceof AssociationElement) ((AssociationElement)con).setShowOntoUmlStereotype(showStereotypeItem.isSelected());
			}
		});
		
		addSeparator();
		createMenuItem(this, "delete");
		//addEditConnectionPropertiesMenu(menu, (UmlConnection) conn);				
	}
	
	public void setConnection(Connection con)
	{
		this.con = con;
		if (con instanceof AssociationElement){
			showMultiplicitiesItem.setSelected(((AssociationElement)con).showMultiplicities());
			showRolesItem.setSelected(((AssociationElement)con).showRoles());
			showNameItem.setSelected(((AssociationElement)con).showName());
			showStereotypeItem.setSelected(((AssociationElement)con).showOntoUmlStereotype());
		}
	}		
	
	/**
	 * Adds the navigability setting menu.
	 * 
	 * @param menu
	 *            the parent menu
	 * @param conn
	 *            the connection
	 */
	//private void addNavigabilityMenu(JPopupMenu menu, UmlConnection conn) {
	//	Relationship relationship = (Relationship) conn.getClassifier();
	//	if (relationship == null)
	//		return; // e.g. NoteConnection has no relation
		//JMenu submenu = null;
		// TODO Uncomment me if used
		/*
		 * if (relationship.canSetElement1Navigability() ||
		 * relationship.canSetElement2Navigability()) { submenu = new
		 * JMenu(ApplicationResources.getInstance().getString(
		 * "submenu.navigableto.name")); menu.add(submenu); } if
		 * (relationship.canSetElement1Navigability()) { JCheckBoxMenuItem
		 * nav2Elem1 = createCheckBoxMenuItem(submenu, "navigabletosource");
		 * nav2Elem1.setSelected(relationship.isNavigableToElement1()); } if
		 * (relationship.canSetElement2Navigability()) { JCheckBoxMenuItem
		 * nav2Elem2 = createCheckBoxMenuItem(submenu, "navigabletotarget");
		 * nav2Elem2.setSelected(relationship.isNavigableToElement2()); }
		 */
	//}
	
	/**
	 * Adds the specified AppCommandListener.
	 * 
	 * @param l
	 *            the AppCommandListener to add
	 */
	public void addAppCommandListener(AppCommandListener l) {
		commandListeners.add(l);
	}
	
	/**
	 * Generic helper method to construct a menu according to the resource
	 * strings.
	 * 
	 * @param menu
	 *            the menu to create the item in
	 * @param name
	 *            the menu item name
	 * @return the JMenuItem
	 */
	public JMenuItem createMenuItem(JComponent menu, String name) {
		String prefix = "menuitem." + name;
		JMenuItem menuitem = new JMenuItem(getResourceString(prefix + ".name"));

		// Command
		String actionCommand = getResourceString(prefix + ".command");
		menuitem.setActionCommand(actionCommand);
		menuitem.addActionListener(this);

		// icon
		String iconType = getResourceString(prefix + ".icon");
		if (iconType != null) {
			menuitem.setIcon(IconLoader.getInstance().getIcon(iconType));
		}
		menu.add(menuitem);
		return menuitem;
	}
	
	/**
	 * Returns the specified resource as a String object.
	 * 
	 * @param property
	 *            the property name
	 * @return the property value or null if not found
	 */
	private String getResourceString(String property) {
		return ApplicationResources.getInstance().getString(property);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent e) {
		for (AppCommandListener l : commandListeners) {
			l.handleCommand(e.getActionCommand());
		}
	}
	
	/**
	 * Helper method to construct a check box menu item according to resource
	 * strings.
	 * 
	 * @param menu
	 *            the parent menu
	 * @param name
	 *            the menuitem name
	 * @return the menu item
	 */
	private JCheckBoxMenuItem createCheckBoxMenuItem(JComponent menu,
			String name) {
		String prefix = "menuitem." + name;
		JCheckBoxMenuItem menuitem = new JCheckBoxMenuItem(
				getResourceString(prefix + ".name"));

		// Command
		String actionCommand = getResourceString(prefix + ".command");
		menuitem.setActionCommand(actionCommand);
		menuitem.addActionListener(this);
		menu.add(menuitem);
		return menuitem;
	}

}
