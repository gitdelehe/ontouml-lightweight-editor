/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package obj;

import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import RefOntoUML.Classifier;
import RefOntoUML.Phase;
import RefOntoUML.Role;
import RefOntoUML.RoleMixin;
import RefOntoUML.provider.RefOntoUMLEditPlugin;
import br.ufes.inf.nemo.common.ontoumlparser.*;
import br.ufes.inf.nemo.common.resource.*;

/**
 *
 * @author Mauricio
 */
public class XMLFile {
    private ArrayList<Atom> atomList;
    private ArrayList<Sig> sigList;
    private ArrayList<Field> fieldList;
    private ArrayList<Skolem> skolemList;
    
    private ArrayList<Atom> worldList;
    private ArrayList<Atom> objectList;
    private ArrayList<Atom> propertyList;
    private ArrayList<Atom> dataTypeList;
    
    private OntoUMLParser ontoUmlParser;
    
    public XMLFile(java.io.File file, OntoUMLParser onto) throws ParserConfigurationException, SAXException, IOException {
        
        // Initialization of parameters
        skolemList = new ArrayList();
        atomList = new ArrayList();
        sigList = new ArrayList();
        fieldList = new ArrayList();
        
        worldList = new ArrayList();
        objectList = new ArrayList();
        propertyList = new ArrayList();
        dataTypeList = new ArrayList();
        
        ontoUmlParser = onto;
        
        //integers = new ArrayList();
        
        // Auxiliary variables
        int i, j, k;
        ReadXML xml = new ReadXML(file);
        Sig sig;
        Field field;
        org.w3c.dom.Element aux;
        
        // Getting elements from .xml file
        NodeList sigl = xml.getDoc().getElementsByTagName("sig");
        NodeList atoml;	// This one will change on each sig
        NodeList fieldl = xml.getDoc().getElementsByTagName("field");
        
        // Getting sigs
        for(i=0; i<sigl.getLength(); i++) {
            //System.out.println("NUM");
            aux = (org.w3c.dom.Element) sigl.item(i);
            sig = new Sig(aux);
            atoml = aux.getElementsByTagName("atom");
            
            sigList.add(sig);
            
            System.out.println(sig.getLabel());
            for(j=0; j<atoml.getLength(); j++) {
                aux = (org.w3c.dom.Element) atoml.item(j);
                //System.out.println(atoml.getLength());
                Atom atom = new Atom(aux, sig);
                System.out.println("    " + atom.getLabel());
                if(!sig.isBuiltin()){
                	atomList.add(atom);
                }
                if(sig.isWorld()) {
                    // Could be a world atom... if it is, adding it to worldList
                    worldList.add(atom);
                }
                //System.out.println(aux.getAttribute("label"));
            }
        }
        
        // Getting fields and its tuples. 
        for(i=0; i<fieldl.getLength(); i++) {
            //System.out.println("NUM2");
            fieldList.add(new Field((org.w3c.dom.Element) fieldl.item(i), atomList));
        }
        
        // Getting parent ids:
        for(i=0; i<sigList.size(); i++){
        	for(j=0; j<sigList.size(); j++){
        		if(sigList.get(i).getParentId() == sigList.get(j).getId()) {
        			sigList.get(i).setParentSig(sigList.get(j));
        			break;
        		}
        	}
        	if(sigList.get(i).getParentSig() != null) {
    			break;
    		}
        	for(j=0; j<fieldList.size(); j++){
        		if(sigList.get(i).getParentId() == fieldList.get(j).getId()) {
        			sigList.get(i).setParentSig(fieldList.get(j));
        			break;
        		}
        	}
        }
        
        for(i=0; i<fieldList.size(); i++){
        	for(j=0; j<fieldList.size(); j++){
        		if(fieldList.get(i).getParentId() == sigList.get(j).getId()) {
        			fieldList.get(i).setParentSig(sigList.get(j));
        			break;
        		}
        	}
        	if(fieldList.get(i).getParentSig() != null) {
    			break;
    		}
        	for(j=0; j<fieldList.size(); j++){
        		if(fieldList.get(i).getParentId() == fieldList.get(j).getId()) {
        			fieldList.get(i).setParentSig(fieldList.get(j));
        			break;
        		}
        	}
        }
        
    }
    
    public ArrayList<String> getAtomTypeOnWorld(String atomLabel, String worldLabel) {
        ArrayList<String> stringList = new ArrayList();	// The list of types. One atom can have multiple types.
        int i, j, k;
        for(i=0; i<fieldList.size(); i++) {
        	//System.out.println("FIELD NAME: " + fieldList.get(i).getLabel());
            if(!fieldList.get(i).getTuples().isEmpty()) {
                if(fieldList.get(i).getTuple(0).size() == 2) {
                    //System.out.println("FOI");
                    for(j=0; j<fieldList.get(i).getTuples().size(); j++) {
                        //System.out.println("FOI2");
                        //for(k=0; k<fieldList.get(i).getTuple(j).size(); k++) {
                            //System.out.println("FOI3");
                            if(fieldList.get(i).getTuple(j).get(0).equals(worldLabel)
                                    && fieldList.get(i).getTuple(j).get(1).equals(atomLabel)
                                    	&& !fieldList.get(i).getLabel().equals("exists")) {
                                stringList.add(fieldList.get(i).getLabel());
                            }
                        //}
                    }
                }
            }
        }
        return stringList;
    }
    
    public ArrayList<Classifier> getAtomClassifiersOnWorld(String atomLabel, String worldLabel) {
        ArrayList<Classifier> classifList = new ArrayList();	// The list of types. One atom can have multiple types.
        int i, j, k;
        for(i=0; i<fieldList.size(); i++) {
        	//System.out.println("FIELD NAME: " + fieldList.get(i).getLabel());
            if(!fieldList.get(i).getTuples().isEmpty()) {
                if(fieldList.get(i).getTuple(0).size() == 2) {
                    //System.out.println("FOI");
                    for(j=0; j<fieldList.get(i).getTuples().size(); j++) {
                        //System.out.println("FOI2");
                        //for(k=0; k<fieldList.get(i).getTuple(j).size(); k++) {
                            //System.out.println("FOI3");
                            if(fieldList.get(i).getTuple(j).get(0).equals(worldLabel)
                                    && fieldList.get(i).getTuple(j).get(1).equals(atomLabel)
                                    	&& !fieldList.get(i).getLabel().equals("exists")) {
                                classifList.add((Classifier) ontoUmlParser.getElement(fieldList.get(i).getLabel()));
                            }
                        //}
                    }
                }
            }
        }
        return classifList;
    }
    
    public Atom findAtom(String label) {
        int i;
        for(i=0; i<atomList.size(); i++) {
            if(atomList.get(i).getLabel().equals(label)) {
                return atomList.get(i);
            }
        }
        return null;
    }
    
    public Sig findSigById(int id) {
        int i;
        for(i=0; i<sigList.size(); i++) {
            if(sigList.get(i).getId() == id) {
                return sigList.get(i);
            }
        }
        return null;
    }

    public Classifier getAtomMainType(String atomLabel, String worldLabel) {
    	ArrayList<Classifier> classifList = getAtomClassifiersOnWorld(atomLabel, worldLabel);
    	for(int i=0; i<classifList.size(); i++) {
        	String type = TypeName.getTypeName(classifList.get(i));
        	if(type.matches("Kind|Quantity|Collective")) {
        		return classifList.get(i);
        	}
        }
    	/*
    	(Classifier) ontoUmlParser.getElement(typeList.get(i));
    	System.out.println(ontoUmlParser.getAllParents(la).toString());
    	*/
    	
    	ArrayList<Classifier> candidates = new ArrayList(); 
    	
    	for(int i=0; i<classifList.size(); i++) {
    		Classifier classif = classifList.get(i);
    		if(classif.allParents().isEmpty()) {
    			candidates.add(classif);
    		}
    	}
    	return candidates.get(0);
    }
    
    public ArrayList<Classifier> getAtomSecondayTypes(String atomLabel, String worldLabel) {
    	ArrayList<Classifier> classifList = getAtomClassifiersOnWorld(atomLabel, worldLabel);
    	classifList.remove(getAtomMainType(atomLabel, worldLabel));
    	ArrayList<Classifier> secondaries = new ArrayList();
    	
    	ArrayList<Classifier> classList = new ArrayList();
    	for(int i=0; i<classifList.size(); i++) {
    		Classifier classif = classifList.get(i);
    		if(!interscedes(classif.allChildren(), classifList) && (classif instanceof Role || classif instanceof Phase  || classif instanceof RoleMixin)) {
    			secondaries.add(classif);
    		}
    	}
    	return secondaries;
    }
    
    /**
     * Get the list of DataTypes/PrimitiveTypes related to an atom on a given world.
     * @param atomLabel the atom's label.
     * @param worldLabel the world's label.
     * @return an ArrayList<String> with the first element being the relation name and the rest being its values
     */
    public ArrayList<String> getDataTypesOnWorld(String atomLabel, String worldLabel) {
    	ArrayList<String> stringList = new ArrayList();	// The list of types. One atom can have multiple types.
        int i, j, k;
        for(i=0; i<fieldList.size(); i++) {
            if(!fieldList.get(i).getTuples().isEmpty() &&
            		fieldList.get(i).getTuple(0).size() == 3 && 
            			findSigById(fieldList.get(i).getTypes().get(0).get(2)).isBuiltin()) {
            	System.out.println("FOI");
            	stringList.add(fieldList.get(i).getLabel());
            	for(j=0; j<fieldList.get(i).getTuples().size(); j++) {
            		if(fieldList.get(i).getTuple(j).get(0).equals(worldLabel)
            				&& fieldList.get(i).getTuple(j).get(1).equals(atomLabel)
            					&& !fieldList.get(i).getLabel().equals("exists")) {
            			System.out.println("STRLIST:"+fieldList.get(i).getTuple(j).get(2));
            			stringList.add(fieldList.get(i).getTuple(j).get(2));
            		}
                        //}
                    //}
                }
            }
        }
        return stringList;
    }
    
    private static boolean interscedes(EList x, ArrayList y) {
		for(int i=0; i<x.size(); i++) {
			if(y.contains(x.get(i))) {
				return true;
			}
		}
    	return false;
    }
    
    public ArrayList<String> getTypeParents(String type) {
    	return null;
    }
    
    public ArrayList<Atom> getAtomList() {
        return atomList;
    }

    public void setAtomList(ArrayList<Atom> atomList) {
        this.atomList = atomList;
    }

    public ArrayList<Sig> getSigList() {
        return sigList;
    }

    public void setSigList(ArrayList<Sig> sigList) {
        this.sigList = sigList;
    }

    public ArrayList<Field> getFieldList() {
        return fieldList;
    }

    public void setFieldList(ArrayList<Field> fieldList) {
        this.fieldList = fieldList;
    }

    public ArrayList<Skolem> getSkolemList() {
        return skolemList;
    }

    public void setSkolemList(ArrayList<Skolem> skolemList) {
        this.skolemList = skolemList;
    }

    public ArrayList<Atom> getWorldList() {
        return worldList;
    }

    public void setWorldList(ArrayList<Atom> worldList) {
        this.worldList = worldList;
    }

    public ArrayList<Atom> getObjectList() {
        return objectList;
    }

    public void setObjectList(ArrayList<Atom> objectList) {
        this.objectList = objectList;
    }

    public ArrayList<Atom> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(ArrayList<Atom> propertyList) {
        this.propertyList = propertyList;
    }

    public ArrayList<Atom> getDataTypeList() {
        return dataTypeList;
    }

    public void setDataTypeList(ArrayList<Atom> dataTypeList) {
        this.dataTypeList = dataTypeList;
    }
    
    
    
}