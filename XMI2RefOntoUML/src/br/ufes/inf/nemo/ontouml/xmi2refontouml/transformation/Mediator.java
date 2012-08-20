package br.ufes.inf.nemo.ontouml.xmi2refontouml.transformation;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTree;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Diagnostician;

import br.ufes.inf.nemo.ontouml.xmi2refontouml.transformation.RefOntoCreator.RefOntoUMLException;
import br.ufes.inf.nemo.ontouml.xmi2refontouml.util.ChckBoxTreeNodeElem;
import br.ufes.inf.nemo.ontouml.xmi2refontouml.util.ElementType;
import br.ufes.inf.nemo.ontouml.xmi2refontouml.util.Mapper;
import br.ufes.inf.nemo.ontouml.xmi2refontouml.util.MapperFactory;
import br.ufes.inf.nemo.ontouml.xmi2refontouml.util.RefOntoUMLUtil;

import RefOntoUML.util.ValidationMessage;


/**
 * This is the transformation main class, responsible for mediating
 * all the transformation. It communicates with the Mapper to get
 * the specific elements that will become RefOntoUML elements,
 * according to the metamodel rules.
 * 
 * @author Vinicius
 *
 */

public class Mediator {
	
	// File addresses
	public String READ_FILE_ADDRESS;
	public String SAVE_FILE_ADDRESS;
	// Mapeia o id dos elementos para elementos RefOntoUML. � utilizado como auxiliar para se criar refer�ncias,
	// como por exemplo em generalizations e associations.
	private static HashMap<String, RefOntoUML.Element> elemMap = new HashMap<String, RefOntoUML.Element>();
	// Leitor de arquivo XMI que conhece as tags espec�ficas do programa que exportou o arquivo
	private Mapper mapper;
	// Inst�ncia da classe que cria os objetos RefOntoUML
    private RefOntoCreator refcreator = new RefOntoCreator();
    // Root of the CheckboxTree. Will be displayed in the API.
    private DefaultMutableTreeNode actualParent;
    // Auxiliary string to stack the path of the element with error
    private String errorPath;
    public static String errorLog;
    public static String warningLog;
    
    public void initialize() {
        //Call the factory to read the Document and decide which Mapper
        //to create, depending on the program/exporter of the XMI
    	MapperFactory mapperFactory = new MapperFactory();
    	mapper = mapperFactory.createMapper(new File(READ_FILE_ADDRESS));
    	if (mapper == null) {
    		errorLog += "Exporter not identified or not supported.\n";
    		return;
    	}
    	
    	//Resets the element map
    	elemMap = new HashMap<String, RefOntoUML.Element>();
    	
    	//Resets the CheckboxTree
    	actualParent = null;
    	
    	errorLog = "";
    	errorPath = "";
    	warningLog = "";
    }
    
    public void save() {
        try {
        	System.out.println("Saving file...");
			refcreator.saveXMI();
			System.out.println("File saved.");
		} catch (IOException e) {
			errorLog += "Error saving file.\n";
			e.printStackTrace();
		}
    }
    
    public void filter(CheckboxTree modelTree) {
    	TreePath[] treepathList = modelTree.getCheckingPaths();
    	for (TreePath treepath : treepathList) {
    		DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)treepath.getLastPathComponent();
    		ChckBoxTreeNodeElem chckNode = (ChckBoxTreeNodeElem) childNode.getUserObject();
    		
    		RefOntoUML.Element oldElem = chckNode.getElement();
    		
    		RefOntoUMLUtil.delete(oldElem, true);
    	}
    	
    	removeExcludedNodes(actualParent, modelTree);
    }
    
    public void removeExcludedNodes(DefaultMutableTreeNode treeNode, CheckboxTree modelTree) {
    	
    	if (treeNode == null) {
    		return;
    	}
    	
    	if (!treeNode.isLeaf()) {
    		removeExcludedNodes((DefaultMutableTreeNode)treeNode.getFirstChild(), modelTree);
    	}
    	
    	removeExcludedNodes(treeNode.getNextSibling(), modelTree);
    	
		ChckBoxTreeNodeElem chckNode = (ChckBoxTreeNodeElem) treeNode.getUserObject();
		
		RefOntoUML.Element oldElem = chckNode.getElement();
		if (oldElem.eResource() == null) {
			DefaultTreeModel treeModel = (DefaultTreeModel)modelTree.getModel();
			treeModel.removeNodeFromParent(treeNode);
		}
    	
    }
    
    public void validate() {
    	Diagnostician validator = Diagnostician.INSTANCE;

    	// (Opcional, apenas para inicializar mais r�pido) 
    	// As the first validation takes long due to initialization process,
    	// we start it here so the user doesn't get the initialization hit
    	//validator.validate(factory.createClass());

    	Map<Object, Object> context = new HashMap<Object, Object>();
    	BasicDiagnostic diag = new BasicDiagnostic();
    	ChckBoxTreeNodeElem chckNode = (ChckBoxTreeNodeElem) actualParent.getUserObject();
		RefOntoUML.Model model = (RefOntoUML.Model) chckNode.getElement();

    	//Retorna true se o modelo for v�lido
    	if (validator.validate(model, diag, context)){
    		System.out.println("Valid model.");
    	} else {
    		System.out.println("Invalid model.");
    	}
    	
    	for (Diagnostic item : diag.getChildren()) {	
	    	//PackageableElement element = (PackageableElement) item.getData().get(0);
	    	System.out.println(ValidationMessage.getFinalMessage(item.getMessage()));
	    	//System.out.println(item.getChildren());
    	}
    }
    
    public DefaultMutableTreeNode parse() throws Exception {
    	
    	initialize();
    	if (mapper == null) {
    		errorLog += "Problem identifiyng exporter.\n";
    		Exception e = new Exception(errorLog);
        	throw e;
    	}
    	
    	//Initializes the model
        refcreator.intialize(SAVE_FILE_ADDRESS);
        
        // Cria o modelo propriamente dito
        Object modelelement = mapper.getModelElement();
        createElement(modelelement, ElementType.MODEL);
        dealModel();
        return actualParent;
    }
    
    protected RefOntoUML.Element createElement(Object element, ElementType elemType) throws Exception {
    	RefOntoUML.Element elem1 = null;
    	String stereotype = null;
    	
    	DefaultMutableTreeNode newTreeNode;
    	
    	switch (elemType) {
    		case MODEL:
    			elem1 = refcreator.createModel();
    			if (doPackage((RefOntoUML.Model)elem1, element) == null)
    				return null;
    			break;
    			
    		case PACKAGE:
    			elem1 = refcreator.createPackage();
    			if (doPackage((RefOntoUML.Package)elem1, element) == null)
    				return null;
    			break;
    			
    		case PRIMITIVE:
    			if (mapper.getName(element).equals("int") || mapper.getName(element).equals("integer")) {
    				elem1 = RefOntoCreator.INTEGER_PRIMITIVE;
            	} else if (mapper.getName(element).equals("bool") || mapper.getName(element).equals("boolean")) {
            		elem1 = RefOntoCreator.BOOLEAN_PRIMITIVE;
            	} else if (mapper.getName(element).equals("str") || mapper.getName(element).equals("string")) {
            		elem1 = RefOntoCreator.STRING_PRIMITIVE;
            	} else if (mapper.getName(element).contains("unlimited")) {
            		elem1 = RefOntoCreator.UNLIMITED_NATURAL_PRIMITIVE;
            	} else {
            		elem1 = refcreator.createPrimitiveType();
            	}
    			break;
    			
    		case DATATYPE:
    			elem1 = refcreator.createDataType();
    			break;
    			
    		case CLASS:
				stereotype = mapper.getStereotype(element);
	    		elem1 = refcreator.createClass(stereotype);
    			
    			if (elem1 == null) {
    				errorPath = mapper.getName(element);
    				break;
    			}
    			break;
    			
    		case PROPERTY:
    			elem1 = refcreator.createProperty();
    			break;
    			
    		case ENUMERATION:
    			elem1 = refcreator.createEnumeraion();
    			break;
    			
    		case ENUMLITERAL:
    			elem1 = refcreator.createEnumLiteral();
    			break;
    			
    		case ASSOCIATION:
				stereotype = mapper.getStereotype(element);
    			elem1 = refcreator.createAssociation(stereotype);
    			
    			if (elem1 == null) {
    				List<Object> listProp = mapper.getElements(element, ElementType.PROPERTY);
    				errorPath = "Ends: ";
    		    	for (Object e : listProp) {
    		    		Map<String, Object> hashProp = mapper.getProperties(e);
    		    		errorPath += mapper.getName(mapper.getElementById((String)hashProp.get("type"))) + ", ";
    		    	}
    		    	errorPath = mapper.getName(element) + "\n" + errorPath;
    				break;
    			}
    			break;
    			
    		case GENERALIZATION:
    			elem1 = refcreator.createGeneralization();
    			break;
    			
    		case GENERALIZATIONSET:
    			elem1 = refcreator.createGeneralizationSet();
    			break;
    			
//    		case DEPENDENCY:
//    			elem1 = refcreator.createDependency();
//    			break;
    			
    		case COMMENT:
    			elem1 = refcreator.createComment();
    			break;
    	}
    	
    	if (elem1 instanceof RefOntoUML.Classifier && !(elem1 instanceof RefOntoUML.PrimitiveType)) {
    		doClassifier(element, (RefOntoUML.Classifier)elem1);
			//Add in the tree
			newTreeNode = new DefaultMutableTreeNode(new ChckBoxTreeNodeElem(elem1));
			actualParent.add(newTreeNode);
    	}
    	
    	//Every Element can have a Comment
    	if (elem1 != null) {
    		String name = mapper.getName(element);
        	if (elem1 instanceof RefOntoUML.NamedElement) {
        		refcreator.setName((RefOntoUML.NamedElement)elem1, name);
        	}
        	
	    	List<Object> listComment = mapper.getElements(element, ElementType.COMMENT);
	        for (Object e : listComment) {
		        RefOntoUML.Comment comment1 = (RefOntoUML.Comment) createElement(e, ElementType.COMMENT);
		        refcreator.addComment(elem1, comment1);
	        }
	        elemMap.put(mapper.getID(element), elem1);
    	}
    	
    	return elem1;
    }
    
    protected void dealModel () {
    	for (Entry<String, RefOntoUML.Element> entry : elemMap.entrySet()) {
    		RefOntoUML.Element modelElem = entry.getValue();
    		String id = entry.getKey();
    		Object domElem = mapper.getElementById(id);
    		// Get the properties of the element (the ones defined in the metamodel)
        	Map<String, Object> hashProp = mapper.getProperties(domElem);
        	
    		if (modelElem instanceof RefOntoUML.Model) {
    			refcreator.dealModel((RefOntoUML.Model)modelElem, hashProp);
    			
    		} else if (modelElem instanceof RefOntoUML.Package) {
    			refcreator.dealPackage((RefOntoUML.Package)modelElem, hashProp);
    			
    		} else if (modelElem instanceof RefOntoUML.Class) {
	    		refcreator.dealClass((RefOntoUML.Class)modelElem, hashProp);
	    		
    		} else if (modelElem instanceof RefOntoUML.DataType) {
    			refcreator.dealDataType((RefOntoUML.DataType)modelElem, hashProp);
    			
    		} else if (modelElem instanceof RefOntoUML.Property) {
    			doProperty(hashProp);
    			refcreator.dealProperty((RefOntoUML.Property)modelElem, hashProp);
    			
    		} else if (modelElem instanceof RefOntoUML.EnumerationLiteral) {
    			refcreator.dealEnumLiteral((RefOntoUML.EnumerationLiteral)modelElem, hashProp);
    			
    		} else if (modelElem instanceof RefOntoUML.Association) {
    			doAssociation(hashProp);
    			refcreator.dealAssociation((RefOntoUML.Association)modelElem, hashProp);
    			
    		} else if (modelElem instanceof RefOntoUML.Generalization) {
    			doGeneralization(hashProp);
    			refcreator.dealGeneralization((RefOntoUML.Generalization)modelElem, hashProp);
    			
    		} else if (modelElem instanceof RefOntoUML.GeneralizationSet) {
    			doGeneralizationSet(hashProp);
    			refcreator.dealGeneralizationSet((RefOntoUML.GeneralizationSet)modelElem, hashProp);
    			
//    		} else if (modelElem instanceof RefOntoUML.Dependency) {
//    			doDependency(hashProp);
//    			refcreator.dealDependency((RefOntoUML.Dependency)modelElem, hashProp);
//    			
    		} else if (modelElem instanceof RefOntoUML.Comment) {
    			doComment(hashProp);
    			refcreator.dealComment((RefOntoUML.Comment)modelElem, hashProp);
    			
    		}
    	}
    }
    
    protected RefOntoUML.Package doPackage(RefOntoUML.Package pack, Object element) throws Exception {
    	DefaultMutableTreeNode newTreeNode = new DefaultMutableTreeNode(new ChckBoxTreeNodeElem(pack));
    	if (actualParent != null) {
    		actualParent.add(newTreeNode);
    	}
    	actualParent = newTreeNode;
    	// Create the Packageable Elements Inside the package
    	try {
    		// Create Primitive Types first
    		packageIterator(pack, element, ElementType.PRIMITIVE);
    		
            // Create nested Packages
            packageIterator(pack, element, ElementType.PACKAGE);
            
	        // Create Classes
            packageIterator(pack, element, ElementType.CLASS);

	        // Create DataTypes
            packageIterator(pack, element, ElementType.DATATYPE);
            
	        // Create Enumerations
            packageIterator(pack, element, ElementType.ENUMERATION);

	        // Create Associations
            packageIterator(pack, element, ElementType.ASSOCIATION);

	        // Create Generalization Sets
            packageIterator(pack, element, ElementType.GENERALIZATIONSET);

            // Create Dependencies
            //packageIterator(pack, element, ElementType.DEPENDENCY);

    	} catch (NullPointerException npe) {
    		npe.printStackTrace();
    		Mediator.warningLog += "Warning: Empty package '" + pack.getName() + "'.\n";
    		
    	}
        
        return pack;
    }
    
    protected void packageIterator(RefOntoUML.Package pack, Object domParent, ElementType type) throws Exception {
		List<Object> listPacks = mapper.getElements(domParent, type);
        for (Object e : listPacks) {
        	try {
        		
        		Object refOntoElement = createElement(e, type);
				refcreator.addPackagedElement(pack, (RefOntoUML.PackageableElement) refOntoElement);
				
				if (refOntoElement instanceof RefOntoUML.Package) {
					actualParent = (DefaultMutableTreeNode) actualParent.getParent();
				} else if (refOntoElement instanceof RefOntoUML.MaterialAssociation) {
	        		doMaterial((RefOntoUML.MaterialAssociation)refOntoElement, e, pack);
	        	}
				
        	} catch (RefOntoUMLException refe) {
        		errorLog = refe.getError();
        		errorPath = mapper.getName(domParent) + " -> " + mapper.getName(e);
        		for (EObject eObj = pack.eContainer(); eObj != null; eObj = eObj.eContainer()) {
        			errorPath = ((RefOntoUML.Package)eObj).getName() + " -> " + errorPath;
        		}
        		errorLog += "Path of the element with error: " + errorPath + "\n";
            	Exception ex = new Exception(errorLog);
            	throw ex;
        	}
        }
    }
    
    protected void doClassifier(Object classelement, RefOntoUML.Classifier class1) throws Exception {
    	
		// Get the classifier properties (they can be attributes or association ends)
    	if (class1 instanceof RefOntoUML.Enumeration) {
    		List<Object> listLit = mapper.getElements(classelement, ElementType.ENUMLITERAL);
        	for (Object e : listLit) {
        		RefOntoUML.EnumerationLiteral lit1 = (RefOntoUML.EnumerationLiteral) createElement(e, ElementType.ENUMLITERAL);
        		refcreator.addEnumLiteral((RefOntoUML.Enumeration)class1, lit1);
        	}
    	} else {
	    	List<Object> listProp = mapper.getElements(classelement, ElementType.PROPERTY);
	    	for (Object e : listProp) {
	    		RefOntoUML.Property prop1 = (RefOntoUML.Property) createElement(e, ElementType.PROPERTY);
	    		refcreator.addProperty((RefOntoUML.Classifier)class1, prop1);
	    	}
    	}
    	// Get the classifier generalizations
    	List<Object> listGen = mapper.getElements(classelement, ElementType.GENERALIZATION);
    	for (Object e : listGen) {
        	RefOntoUML.Generalization gen1 = (RefOntoUML.Generalization) createElement(e, ElementType.GENERALIZATION);
        	refcreator.addGeneralization(class1, gen1);
        }
    }
    
    protected void doAssociation (Map<String, Object> hashProp) {
    	
    	List<RefOntoUML.Property> memberEnds = new ArrayList<RefOntoUML.Property>();
    	for (Object memberEnd : (List<?>)hashProp.get("memberend")) {
    		memberEnds.add((RefOntoUML.Property)elemMap.get((String)memberEnd));
		}
    	hashProp.put("memberend", memberEnds);
    }
    
    protected void doProperty (Map<String, Object> hashProp) {
    	
    	// Gets the RefOntoUML.Element that is the 'type' of the property
		hashProp.put("type", elemMap.get(hashProp.get("type")));
    }
    
    protected void doGeneralization (Map<String, Object> hashProp) {
    	
    	// Gets the RefOntoUML.Element that is the 'general' of the generalization
    	hashProp.put("general", elemMap.get(hashProp.get("general")));
    	hashProp.put("specific", elemMap.get(hashProp.get("specific")));
    }
    
    protected void doGeneralizationSet (Map<String, Object> hashProp) {
    	
    	// Gets the RefOntoUML.Element that is the 'general' of the generalization
    	List<RefOntoUML.Generalization> generalizations = new ArrayList<RefOntoUML.Generalization>();
    	for (Object gen : (List<?>)hashProp.get("generalization")) {
    		generalizations.add((RefOntoUML.Generalization)elemMap.get((String)gen));
		}
    	hashProp.put("generalization", generalizations);
    }
    
//    protected void doDependency(Map<String, Object> hashProp) {
//    	
//    	List<RefOntoUML.NamedElement> clients = new ArrayList<RefOntoUML.NamedElement>();
//    	for (Object client : (List<?>)hashProp.get("client")) {
//    		clients.add((RefOntoUML.NamedElement)elemMap.get((String)client));
//		}
//    	hashProp.put("client", clients);
//    	
//    	List<RefOntoUML.NamedElement> suppliers = new ArrayList<RefOntoUML.NamedElement>();
//    	for (Object supplier : (List<?>)hashProp.get("supplier")) {
//    		suppliers.add((RefOntoUML.NamedElement)elemMap.get((String)supplier));
//		}
//    	hashProp.put("supplier", suppliers);
//    }
    
    protected void doComment(Map<String, Object> hashProp) {
    	List<RefOntoUML.Element> annotatedElements = new ArrayList<RefOntoUML.Element>();
    	for (Object annotElem : (List<?>)hashProp.get("annotatedelement")) {
    		annotatedElements.add((RefOntoUML.Element)elemMap.get((String)annotElem));
		}
    	hashProp.put("annotatedelement", annotatedElements);
    }
    
    protected void doMaterial(RefOntoUML.MaterialAssociation material, Object materialObject, 
    		RefOntoUML.Package pack) throws RefOntoUMLException {
    	String relatorID = mapper.getRelatorfromMaterial(materialObject);
    	if (relatorID != null && relatorID != "") {
    		RefOntoUML.Derivation der = (RefOntoUML.Derivation) refcreator.createAssociation("Derivation");
    		RefOntoUML.Relator relator = (RefOntoUML.Relator) elemMap.get(relatorID);
    		
        	Map<String, Object> hashProp = new HashMap<String, Object>();
//        	hashProp.put("name", "Derivation_" + material.getName() + "_" + relator.getName());
//        	refcreator.dealAssociation(der, hashProp);
        	
        	RefOntoUML.Property prop1 = refcreator.createProperty();
        	hashProp.put("type", relator);
        	hashProp.put("lower", "1");
        	hashProp.put("upper", "1");
        	refcreator.dealProperty(prop1, hashProp);
        	refcreator.addProperty(der, prop1);
        	
        	RefOntoUML.Property prop2 = refcreator.createProperty();
        	hashProp.put("type", material);
        	refcreator.dealProperty(prop2, hashProp);
        	refcreator.addProperty(der, prop2);
        	
        	refcreator.addPackagedElement(pack, der);
    	}
    }
}
