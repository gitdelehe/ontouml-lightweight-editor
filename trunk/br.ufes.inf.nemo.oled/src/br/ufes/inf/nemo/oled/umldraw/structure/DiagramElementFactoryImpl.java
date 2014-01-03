/**
 * Copyright 2007 Wei-ju Wu
 *
 * This file is part of TinyUML.
 *
 * TinyUML is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * TinyUML is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TinyUML; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package br.ufes.inf.nemo.oled.umldraw.structure;

import java.util.HashMap;
import java.util.Map;

import RefOntoUML.AggregationKind;
import RefOntoUML.Association;
import RefOntoUML.Category;
import RefOntoUML.Characterization;
import RefOntoUML.Collective;
import RefOntoUML.DataType;
import RefOntoUML.Derivation;
import RefOntoUML.FormalAssociation;
import RefOntoUML.Generalization;
import RefOntoUML.Kind;
import RefOntoUML.MaterialAssociation;
import RefOntoUML.Mediation;
import RefOntoUML.Meronymic;
import RefOntoUML.Mixin;
import RefOntoUML.Mode;
import RefOntoUML.Phase;
import RefOntoUML.Property;
import RefOntoUML.Quantity;
import RefOntoUML.RefOntoUMLFactory;
import RefOntoUML.Relator;
import RefOntoUML.Role;
import RefOntoUML.RoleMixin;
import RefOntoUML.SubKind;
import RefOntoUML.componentOf;
import RefOntoUML.memberOf;
import RefOntoUML.subCollectionOf;
import RefOntoUML.subQuantityOf;
import RefOntoUML.impl.AssociationImpl;
import RefOntoUML.impl.CharacterizationImpl;
import RefOntoUML.impl.DataTypeImpl;
import RefOntoUML.impl.DerivationImpl;
import RefOntoUML.impl.DirectedBinaryAssociationImpl;
import RefOntoUML.impl.FormalAssociationImpl;
import RefOntoUML.impl.MaterialAssociationImpl;
import RefOntoUML.impl.MediationImpl;
import RefOntoUML.impl.MeronymicImpl;
import RefOntoUML.impl.componentOfImpl;
import br.ufes.inf.nemo.oled.draw.Connection;
import br.ufes.inf.nemo.oled.draw.LineConnectMethod;
import br.ufes.inf.nemo.oled.model.ElementType;
import br.ufes.inf.nemo.oled.model.RelationType;
import br.ufes.inf.nemo.oled.model.UmlProject;
import br.ufes.inf.nemo.oled.umldraw.shared.UmlConnection;
import br.ufes.inf.nemo.oled.umldraw.shared.UmlDiagramElement;
import br.ufes.inf.nemo.oled.umldraw.shared.UmlNode;
import br.ufes.inf.nemo.oled.util.ModelHelper;

/**
 * Implementation of the DiagramElementFactory interface. A
 * DiagramElementFactory instance belongs to a particular UmlDiagram instance,
 * so it can automatically associate allElements to the diagram they belong to.
 *
 * @author Wei-ju Wu
 * @version 1.0
 */
public class DiagramElementFactoryImpl implements DiagramElementFactory {

  private Map<ElementType, UmlDiagramElement> elementPrototypes = new HashMap<ElementType, UmlDiagramElement>();
  private Map<RelationType, UmlConnection> relationPrototypes = new HashMap<RelationType, UmlConnection>();
  private Map<ElementType, Integer> elementCounters = new HashMap<ElementType, Integer>();
  private Map<RelationType, Integer> relationCounters = new HashMap<RelationType, Integer>();
  private StructureDiagram diagram;
  private RefOntoUMLFactory factory;
  
  /**
   * Constructor.
   * @param aDiagram the diagram this factory belongs to
   */
  public DiagramElementFactoryImpl(StructureDiagram aDiagram) {
    diagram = aDiagram;
    setupElementMaps();
    setupConnectionMaps();
  }
  
  public void load(UmlProject project)
  {
//	  OntoUMLParser refparser = ProjectBrowser.getParserFor(project);
//	  for(RefOntoUML.NamedElement elem: refparser.getAllInstances(RefOntoUML.NamedElement)){
//		  if (elem instanceof )
//	  }
	  
  }
  
  public void setDiagram(StructureDiagram diagram){
	  this.diagram = diagram;
  }

  /**
   * Initializes the element map with the element prototypes.
   */

  private void setupElementMaps() {
	
	factory = ModelHelper.getFactory();
	
	elementCounters.put(ElementType.PACKAGE, 0);
	
	NoteElement notePrototype = (NoteElement)
    NoteElement.getPrototype().clone();
    elementPrototypes.put(ElementType.NOTE, notePrototype);
    elementCounters.put(ElementType.NOTE, 0);
    
    Kind kind = (RefOntoUML.Kind)ModelHelper.createType(ElementType.KIND,"Kind");
    ClassElement kindElement = (ClassElement) ClassElement.getPrototype().clone();
    kindElement.setClassifier(kind);
    elementPrototypes.put(ElementType.KIND, kindElement);
    elementCounters.put(ElementType.KIND, 0);
    
    Quantity quantity = (RefOntoUML.Quantity)ModelHelper.createType(ElementType.QUANTITY,"Quantity");
    ClassElement quantityElement = (ClassElement) ClassElement.getPrototype().clone();
    quantityElement.setClassifier(quantity);
    elementPrototypes.put(ElementType.QUANTITY, quantityElement);
    elementCounters.put(ElementType.QUANTITY, 0);
        
    Collective collective = (RefOntoUML.Collective)ModelHelper.createType(ElementType.COLLECTIVE,"Collective");   
    ClassElement collectiveElement = (ClassElement) ClassElement.getPrototype().clone();
    collectiveElement.setClassifier(collective);
    elementPrototypes.put(ElementType.COLLECTIVE, collectiveElement);
    elementCounters.put(ElementType.COLLECTIVE, 0);
        
    SubKind subkind = (RefOntoUML.SubKind)ModelHelper.createType(ElementType.SUBKIND,"SubKind");   
    ClassElement subkindElement = (ClassElement) ClassElement.getPrototype().clone();
    subkindElement.setClassifier(subkind);
    elementPrototypes.put(ElementType.SUBKIND, subkindElement);
    elementCounters.put(ElementType.SUBKIND, 0);
        
    Phase phase = (RefOntoUML.Phase)ModelHelper.createType(ElementType.PHASE,"Phase");    
    ClassElement phaseElement = (ClassElement) ClassElement.getPrototype().clone();
    phaseElement.setClassifier(phase);
    elementPrototypes.put(ElementType.PHASE, phaseElement);
    elementCounters.put(ElementType.PHASE, 0);
        
    Role role = (RefOntoUML.Role)ModelHelper.createType(ElementType.ROLE,"Role");  
    ClassElement roleElement = (ClassElement) ClassElement.getPrototype().clone();
    roleElement.setClassifier(role);
    elementPrototypes.put(ElementType.ROLE, roleElement);
    elementCounters.put(ElementType.ROLE, 0);
        
    Category category = (RefOntoUML.Category)ModelHelper.createType(ElementType.CATEGORY,"Category");  
    ClassElement categoryElement = (ClassElement) ClassElement.getPrototype().clone();
    categoryElement.setClassifier(category);
    elementPrototypes.put(ElementType.CATEGORY, categoryElement);
    elementCounters.put(ElementType.CATEGORY, 0);
        
    RoleMixin rolemixin = (RefOntoUML.RoleMixin)ModelHelper.createType(ElementType.ROLEMIXIN,"RoleMixin");   
    ClassElement rolemixinElement = (ClassElement) ClassElement.getPrototype().clone();
    rolemixinElement.setClassifier(rolemixin);
    elementPrototypes.put(ElementType.ROLEMIXIN, rolemixinElement);
    elementCounters.put(ElementType.ROLEMIXIN, 0);
        
    Mixin mixin = (RefOntoUML.Mixin)ModelHelper.createType(ElementType.MIXIN,"Mixin");   
    ClassElement mixinElement = (ClassElement) ClassElement.getPrototype().clone();
    mixinElement.setClassifier(mixin);
    elementPrototypes.put(ElementType.MIXIN, mixinElement);
    elementCounters.put(ElementType.MIXIN, 0);
        
    Mode mode = (RefOntoUML.Mode)ModelHelper.createType(ElementType.MODE,"Mode");   
    ClassElement modeElement = (ClassElement) ClassElement.getPrototype().clone();
    modeElement.setClassifier(mode);
    elementPrototypes.put(ElementType.MODE, modeElement);
    elementCounters.put(ElementType.MODE, 0);
        
    Relator relator = (RefOntoUML.Relator)ModelHelper.createType(ElementType.RELATOR,"Relator");   
    ClassElement relatorElement = (ClassElement) ClassElement.getPrototype().clone();
    relatorElement.setClassifier(relator);
    elementPrototypes.put(ElementType.RELATOR, relatorElement);
    elementCounters.put(ElementType.RELATOR, 0);
        
    DataType datatype = (RefOntoUML.DataType)ModelHelper.createType(ElementType.DATATYPE,"Datatype");
    ClassElement datatypeElement = (ClassElement) ClassElement.getPrototype().clone();
    datatypeElement.setClassifier(datatype);
    datatypeElement.setShowAttributes(true);
    elementPrototypes.put(ElementType.DATATYPE, datatypeElement);
    elementCounters.put(ElementType.DATATYPE, 0);
  }

  public void createPropertiesByDefault(Association association)
  {
		Property node1Property, node2Property;	    		
		node1Property = ModelHelper.createDefaultOwnedEnd(null, 1, 1);	    		
		//If the association is a ComponentOf, set the default cardinality to 2..*, to help in validation
		if(association instanceof componentOfImpl) node2Property = ModelHelper.createDefaultOwnedEnd(null, 2, -1);
		else node2Property = ModelHelper.createDefaultOwnedEnd(null, 1, 1);
		
		if(association instanceof MeronymicImpl)
		{
			if(((Meronymic)association).isIsShareable()) node1Property.setAggregation(AggregationKind.SHARED);	    			
			else node1Property.setAggregation(AggregationKind.COMPOSITE);	    				
		}
		
		String node1Name  = new String();		
		if(node1Property.getType()!=null)
		{ 
			node1Name = node1Property.getType().getName();	    		
			if(node1Name==null || node1Name.trim().isEmpty()) node1Name = "source";
			else node1Name = node1Name.trim().toLowerCase();
		}
		String node2Name  = new String();
		if(node2Property.getType()!=null)
		{ 
			node2Name = node2Property.getType().getName();	    		
			if(node2Name==null || node2Name.trim().isEmpty()) node2Name = "target";
			else node2Name = node2Name.trim().toLowerCase();
		}
		
		node1Property.setName(node1Name);
		node2Property.setName(node2Name);
		
		association.getOwnedEnd().add(node1Property);
		association.getOwnedEnd().add(node2Property);	    		
		association.getMemberEnd().add(node1Property);
		association.getMemberEnd().add(node2Property);
		
		if(association instanceof DirectedBinaryAssociationImpl || association instanceof FormalAssociationImpl || association instanceof MaterialAssociationImpl)
		{
			association.getNavigableOwnedEnd().add(node1Property);
			association.getNavigableOwnedEnd().add(node2Property);	    			
			//If the association is Mediation or Characterization, set target readonly to help in validation
			if(association instanceof MediationImpl || association instanceof CharacterizationImpl || association instanceof DerivationImpl) node2Property.setIsReadOnly(true);
		}
		else
		{
			if(node1Property.getType() instanceof DataTypeImpl) association.getNavigableOwnedEnd().add(node1Property);	    		
			if(node2Property.getType() instanceof DataTypeImpl) association.getNavigableOwnedEnd().add(node2Property);
		}		
  }
	
  /**
   * Initializes the map with the connection prototypes.
   */
  private void setupConnectionMaps() {

	factory = RefOntoUMLFactory.eINSTANCE;
		
    Generalization generalization = (RefOntoUML.Generalization)ModelHelper.createRelationship(RelationType.GENERALIZATION, "Generalization");    
    GeneralizationElement generalizationElement = (GeneralizationElement) GeneralizationElement.getPrototype().clone();
    generalizationElement.setRelationship(generalization);
    relationPrototypes.put(RelationType.GENERALIZATION, generalizationElement);
    relationCounters.put(RelationType.GENERALIZATION, 0);
        
    Characterization characterization = (RefOntoUML.Characterization)ModelHelper.createRelationship(RelationType.CHARACTERIZATION, "Characterization");   
    AssociationElement characterizationElement = (AssociationElement) AssociationElement.getPrototype().clone();
    characterizationElement.setRelationship(characterization);
    characterizationElement.setAssociationType(RelationType.CHARACTERIZATION);
    characterizationElement.setShowOntoUmlStereotype(true);
    relationPrototypes.put(RelationType.CHARACTERIZATION, characterizationElement);
    relationCounters.put(RelationType.CHARACTERIZATION, 0);
    createPropertiesByDefault(characterization);
    
    FormalAssociation formalAssociation = (RefOntoUML.FormalAssociation)ModelHelper.createRelationship(RelationType.FORMAL, "Formal");
    AssociationElement formalAssociationElement = (AssociationElement) AssociationElement.getPrototype().clone();
    formalAssociationElement.setRelationship(formalAssociation);
    formalAssociationElement.setAssociationType(RelationType.FORMAL);
    formalAssociationElement.setShowOntoUmlStereotype(true);
    relationPrototypes.put(RelationType.FORMAL, formalAssociationElement);
    relationCounters.put(RelationType.FORMAL, 0);
    createPropertiesByDefault(formalAssociation);
          
    MaterialAssociation materialAssociation = (RefOntoUML.MaterialAssociation)ModelHelper.createRelationship(RelationType.MATERIAL, "Material");    
    AssociationElement materialAssociationElement = (AssociationElement) AssociationElement.getPrototype().clone();
    materialAssociationElement.setRelationship(materialAssociation);
    materialAssociationElement.setAssociationType(RelationType.MATERIAL);
    materialAssociationElement.setShowOntoUmlStereotype(true);
    relationPrototypes.put(RelationType.MATERIAL, materialAssociationElement);
    relationCounters.put(RelationType.MATERIAL, 0);
    createPropertiesByDefault(materialAssociation);
    
    Mediation mediation = (RefOntoUML.Mediation)ModelHelper.createRelationship(RelationType.MEDIATION, "Mediation");   
    AssociationElement mediationElement = (AssociationElement) AssociationElement.getPrototype().clone();
    mediationElement.setRelationship(mediation);
    mediationElement.setAssociationType(RelationType.MEDIATION);
    mediationElement.setShowOntoUmlStereotype(true);
    relationPrototypes.put(RelationType.MEDIATION, mediationElement);
    relationCounters.put(RelationType.MEDIATION, 0);
    createPropertiesByDefault(mediation);
        
    memberOf memberof = (RefOntoUML.memberOf)ModelHelper.createRelationship(RelationType.MEMBEROF, "MemberOf");   
    AssociationElement memberofElement = (AssociationElement) AssociationElement.getPrototype().clone();
    memberofElement.setAssociationType(RelationType.MEMBEROF);
    memberofElement.setRelationship(memberof);
    relationPrototypes.put(RelationType.MEMBEROF, memberofElement);
    relationCounters.put(RelationType.MEMBEROF, 0);
    createPropertiesByDefault(memberof);
    
    subQuantityOf subquantityof = (RefOntoUML.subQuantityOf)ModelHelper.createRelationship(RelationType.SUBQUANTITYOF, "SubQuantityOf");    
    AssociationElement subquantityofElement = (AssociationElement) AssociationElement.getPrototype().clone();
    subquantityofElement.setAssociationType(RelationType.SUBQUANTITYOF);
    subquantityofElement.setRelationship(subquantityof);
    relationPrototypes.put(RelationType.SUBQUANTITYOF, subquantityofElement);
    relationCounters.put(RelationType.SUBQUANTITYOF, 0);  
    createPropertiesByDefault(subquantityof);    
    
    subCollectionOf subcollectionof = (RefOntoUML.subCollectionOf)ModelHelper.createRelationship(RelationType.SUBCOLLECTIONOF, "SubCollectionOf");    
    AssociationElement subcollectionofElement = (AssociationElement) AssociationElement.getPrototype().clone();
    subcollectionofElement.setAssociationType(RelationType.SUBCOLLECTIONOF);
    subcollectionofElement.setRelationship(subcollectionof);
    relationPrototypes.put(RelationType.SUBCOLLECTIONOF, subcollectionofElement);
    relationCounters.put(RelationType.SUBCOLLECTIONOF, 0);
    createPropertiesByDefault(subcollectionof);     
    
    componentOf componentof = (RefOntoUML.componentOf)ModelHelper.createRelationship(RelationType.COMPONENTOF, "ComponentOf");    
    AssociationElement componentofElement = (AssociationElement) AssociationElement.getPrototype().clone();
    componentofElement.setAssociationType(RelationType.COMPONENTOF);
    componentofElement.setRelationship(componentof);
    relationPrototypes.put(RelationType.COMPONENTOF, componentofElement);
    relationCounters.put(RelationType.COMPONENTOF, 0);
    createPropertiesByDefault(componentof); 
    
    Derivation derivation = (RefOntoUML.Derivation)ModelHelper.createRelationship(RelationType.DERIVATION, "Derivation"); 
    AssociationElement derivationeElement = (AssociationElement) AssociationElement.getPrototype().clone();
    derivationeElement.setAssociationType(RelationType.DERIVATION);
    derivationeElement.setRelationship(derivation);
    derivationeElement.setIsDashed(true);
    relationPrototypes.put(RelationType.DERIVATION, derivationeElement);
    relationCounters.put(RelationType.DERIVATION, 0); 
    createPropertiesByDefault(derivation); 
    
    Association datatyperelationship = (RefOntoUML.Association)ModelHelper.createRelationship(RelationType.ASSOCIATION, "Association");
    AssociationElement datatyperelationshipElement = (AssociationElement) AssociationElement.getPrototype().clone();
    datatyperelationshipElement.setRelationship(datatyperelationship);
    datatyperelationshipElement.setAssociationType(RelationType.ASSOCIATION);
    relationPrototypes.put(RelationType.ASSOCIATION, datatyperelationshipElement);     
    relationCounters.put(RelationType.ASSOCIATION, 0);
    createPropertiesByDefault(datatyperelationship); 
    
    relationPrototypes.put(RelationType.NOTE_CONNECTOR, NoteConnection.getPrototype());
    relationCounters.put(RelationType.NOTE_CONNECTOR, 0);  
  }
  
  public RefOntoUML.Package createPackage()
  {
	  RefOntoUML.Package pack = factory.createPackage();
	  pack.setName("Package"+nextElementCount(ElementType.PACKAGE));	  
	  return pack;
  }
  
  public RefOntoUMLFactory getFactory(){
	  return factory;
  }
  
  /**
   * {@inheritDoc} This method also create the referred RefOntoUML Type of the UmlNode. 
   */
  public UmlNode createNode(ElementType elementType) 
  {
    UmlNode umlnode = (UmlNode) elementPrototypes.get(elementType).clone();
    
    if(umlnode.getClassifier() != null) umlnode.getClassifier().setName(umlnode.getClassifier().getName() + nextElementCount(elementType));
    
    umlnode.addNodeChangeListener(diagram);
    
    return umlnode;
  }
  
  public UmlNode createNode(RefOntoUML.Type type) 
  {
    UmlNode umlnode = (UmlNode) elementPrototypes.get(ElementType.valueOf(type.eClass().getName().toUpperCase())).clone();
    
    ((ClassElement)umlnode).setClassifier((RefOntoUML.Classifier)type);
    
    umlnode.addNodeChangeListener(diagram);
    
    return umlnode;
  }
  
  public String nextElementCount(ElementType elementType)
  {
	  int count = elementCounters.get(elementType);
	  count += 1;
	  elementCounters.put(elementType, count);
	  return Integer.toString(count);
  }
  
  /**
   * {@inheritDoc}  This method also create the referred RefOntoUML Relationship of the UmlConnection. 
   */
  public UmlConnection createConnection(RelationType relationType, UmlNode node1, UmlNode node2) 
  {
    UmlConnection prototype = relationPrototypes.get(relationType);    
    
    UmlConnection conn = null;
    if (prototype != null) 
    {
      conn = (UmlConnection) prototype.clone();
      bindConnection(conn, node1, node2);
      if(conn.getRelationship() != null && conn.getRelationship() instanceof AssociationImpl)
      {
    	  Association association = (Association) conn.getRelationship();
    	  association.setName(association.getName() + nextRelationCount(relationType));
      }
    }
    
    return conn;
  }
  
  /**
   * {@inheritDoc} This method also create the referred RefOntoUML Relationship of the UmlConnection. 
   */
  @Override
  public UmlConnection createConnectionFromCon(RelationType relationType, UmlConnection c1, UmlNode node2) 
  {
	  UmlConnection prototype = relationPrototypes.get(relationType);
	  
      UmlConnection conn = null;
      if (prototype != null) 
      {
	      conn = (UmlConnection) prototype.clone();
	      bindConnection(conn, c1, node2);
	      if(conn.getRelationship() != null && conn.getRelationship() instanceof AssociationImpl)
	      {
	    	  Association association = (Association) conn.getRelationship();
	    	  association.setName(association.getName() + nextRelationCount(relationType));
	      }
      }	    
      return conn;
  }

  /**
   * {@inheritDoc} This method also create the referred RefOntoUML Relationship of the UmlConnection. 
   */
  public UmlConnection createConnectionToCon(RelationType relationType, UmlNode node1, UmlConnection c2) 
  {
    UmlConnection prototype = relationPrototypes.get(relationType);    
    
    UmlConnection conn = null;
    if (prototype != null) 
    {
      conn = (UmlConnection) prototype.clone();
      bindConnection(conn, node1, c2);
      if(conn.getRelationship() != null && conn.getRelationship() instanceof AssociationImpl)
      {
    	  Association association = (Association) conn.getRelationship();
    	  association.setName(association.getName() + nextRelationCount(relationType));
      }
    }
    
    return conn;
  }

  public String nextRelationCount(RelationType relationType)
  {
	  int count = relationCounters.get(relationType);
	  count += 1;
	  relationCounters.put(relationType, count);
	  return Integer.toString(count);
  }
  
  /**
   * {@inheritDoc}
   */
  public LineConnectMethod getConnectMethod(RelationType relationType)
  {
    UmlConnection conn = relationPrototypes.get(relationType);
    return (conn == null) ? null : conn.getConnectMethod();
  }

  /**
   * Binds the UmlConnection to the nodes.
   * @param conn the Connection
   * @param node1 the Node 1
   * @param node2 the Node 2
   */
  public void bindConnection(UmlConnection conn, UmlNode node1, UmlNode node2) {
    conn.setNode1(node1);
    conn.setNode2(node2);
    if(node1!=null) node1.addConnection(conn);
    if(node2!=null) node2.addConnection(conn);
  }
  
  public void bindConnection(UmlConnection conn, UmlNode node1, Connection c2) {
	  conn.setNode1(node1);
	  conn.setConnection2(c2);
	  if(node1!=null) node1.addConnection(conn);
	  if(c2!=null) c2.addConnection(conn);  
  }
  
  public void bindConnection(UmlConnection conn, Connection c1, UmlNode node2) {
	  conn.setNode2(node2);
	  conn.setConnection1(c1);
	  if(node2!=null) node2.addConnection(conn);
	  if(c1!=null) c1.addConnection(conn);  
  }

}