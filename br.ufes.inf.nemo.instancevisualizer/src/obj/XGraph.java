/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package obj;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.layout.HierarchicalLayout;
import org.graphstream.ui.layout.springbox.implementations.LinLog;
import org.graphstream.ui.layout.springbox.implementations.SpringBox;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.ViewerPipe;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import RefOntoUML.Classifier;
import br.ufes.inf.nemo.common.ontoumlparser.OntoUMLParser;
import br.ufes.inf.nemo.common.resource.TypeName;

/**
 *
 * @author Mauricio
 */
public class XGraph {
    private XMLFile xmlFile;
    private Graph worldGraph;
    private Graph selectedGraph;
    private Viewer viewer;
    private View view;
    private Viewer viewer1;
    private View view1;
    private OntoUMLParser ontoUmlParser;
    private int edgeId;
    protected ArrayList<String> allTypes;
    protected ArrayList<String> typeImages;
    protected ArrayList<Integer> typeAmount;
    
    public XGraph(XMLFile xmlGraph, OntoUMLParser onto, int mode) throws ParserConfigurationException, SAXException, IOException {
        xmlFile = xmlGraph;
        worldGraph = null;
        selectedGraph = null;
        viewer = null;
        view = null;
        ontoUmlParser = onto;
        edgeId = 0;
        allTypes = new ArrayList();
        typeImages = new ArrayList();
        typeAmount = new ArrayList();
    }
    
    /**
	   * Creates the world map graph based on the input file.
	   */
	public org.graphstream.graph.Graph createWorldMap() {
        ArrayList<Skolem> skolemList = xmlFile.getSkolemList();
        ArrayList<Atom> atomList = xmlFile.getAtomList();
        ArrayList<Sig> sigList = xmlFile.getSigList();
        ArrayList<Field> fieldList = xmlFile.getFieldList();
        
        int i, j;
        worldGraph = new MultiGraph("World Map");
        ArrayList<ArrayList<String>> tuplesList;
        ArrayList<String> tuple;
        int current=1, past=0, future=0, temporal=0;
        
        // Getting only world atoms.
        for(i=0; i<atomList.size(); i++) {
        	if(atomList.get(i).isWorld()) {
        		System.out.println(atomList.get(i).getLabel().substring(0, 15));
        		org.graphstream.graph.Node nodeAux = worldGraph.addNode(atomList.get(i).getLabel());
        		/*
        		if(atomList.get(i).getWorldType().equals("Current")) {
        			nodeAux.addAttribute("ui.style", "size: 15px; shadow-mode: gradient-radial; shadow-width: 10px; shadow-color: black, white; shadow-offset: 0px;");
        			nodeAux.addAttribute("ui.label", "C");
        			nodeAux.addAttribute("xy", 0, 0);
        		}else{
        			nodeAux.addAttribute("ui.style", "size: 10px; fill-color: white;");
        			nodeAux.addAttribute("xy", i, 0);
        		}
        		*/
                	
                    switch(atomList.get(i).getWorldType()) {
                        case "Past":
                        	nodeAux.addAttribute("ui.style", "size: 10px; fill-color: white;");
                			nodeAux.addAttribute("xy", -1, past);
                			past=-1;
                            break;
                        case "Current":
                        	nodeAux.addAttribute("ui.style", "size: 15px; fill-color: green; shadow-mode: gradient-radial; shadow-width: 10px; shadow-color: black, white; shadow-offset: 0px;");
                			nodeAux.addAttribute("ui.label", "C");
                			nodeAux.addAttribute("xy", 0, 0);
                            break;
                        case "Future":
                        	nodeAux.addAttribute("ui.style", "size: 10px; fill-color: white;");
                			nodeAux.addAttribute("xy", 1, future);
                			future=-1;
                            break;
                        case "Counterfactual":
                        	nodeAux.addAttribute("ui.style", "size: 10px; fill-color: white;");
                			nodeAux.addAttribute("xy", 0, current);
                			current=-1;
                            break;
                        case "Temporal":
                            
                            break;
                        default:
                            
                            System.exit(1);
                    }
                    
        	}
        }
        
        // Getting only "next" associations - the only thing we need in a worlds graph.
        for(i=0; i<fieldList.size(); i++) {
            if(fieldList.get(i).getLabel().equals("next")) {
                tuplesList = fieldList.get(i).getTuples();
                for(j=0; j<tuplesList.size(); j++) {
                    tuple = tuplesList.get(j);
                    //for(k=0; k<tuple.size() - 1; k++) {	// 0=k; 1=k+1
                        try {
                            //String edgeName = tuple.get(0) + "_" + fieldList.get(i).getLabel() + "_" + tuple.get(1);
                            //edgeName = edgeName.concat(tuple.get(k+1).getLabel());
                            System.out.println(tuple.get(0));
                            System.out.println(tuple.get(1));
                            worldGraph.addEdge(fieldList.get(i).getLabel() + "$" + j, tuple.get(0), tuple.get(1), true);//.addAttribute("ui.label", fieldList.get(i).getLabel());
                        } catch (Exception e) {
                        	
                        }
                    //}
                }
            }
            break;
        }
        
        worldGraph.addAttribute("ui.antialias");
        
        worldGraph.addAttribute("ui.stylesheet", "graph {\n" +
"padding: 40px;\n" +
"}\n" +
"node { size-mode: normal; text-size: 12; size: 10px; fill-color: white; stroke-mode: plain;} edge { shape: blob; size: 10px; }");

        return worldGraph;
    }

    
	/**
	   * Creates the 
	   * @param world the atom that represents the world
	   */
    public org.graphstream.graph.Graph createSelectedWorld(String worldLabel) {
        ArrayList<Skolem> skolemList = xmlFile.getSkolemList();
        ArrayList<Atom> atomList = xmlFile.getAtomList();
        ArrayList<Sig> sigList = xmlFile.getSigList();
        ArrayList<Field> fieldList = xmlFile.getFieldList();
        
        ArrayList<Atom> objectList = xmlFile.getObjectList();
        ArrayList<Atom> propertyList = xmlFile.getPropertyList();
        ArrayList<Atom> dataTypeList = xmlFile.getDataTypeList();
        
        int i, j, k;
        
        Graph graph = new MultiGraph(worldLabel);
        ArrayList<ArrayList<String>> tuplesList;
        ArrayList<String> tuple;
        ArrayList<ArrayList<Integer>> typesList;
        
        // Creating nodes - we need the "exists" tuples:
        for(i=0; i<fieldList.size(); i++) {
        	if(fieldList.get(i).getLabel().equals("exists")) {
        		obj.Field exists = fieldList.get(i);
        		// After we find the "exists" field, we don't need to search anymore
		        for(i=0; i<exists.getTuples().size(); i++) {
		            if(exists.getTuple(i).get(0).equals(worldLabel)) {	// The first world to display is the current world.
		            	String atomLabel = exists.getTuple(i).get(1);
		                Node nodeAux = graph.addNode(atomLabel);
		                String mainType = xmlFile.getAtomMainType(atomLabel, worldLabel).getName();
		                if(!allTypes.contains(mainType)) {
		                	allTypes.add(mainType);
		                	typeAmount.add(0);
		                	File image = new File("./resources/gur project");
		                	String imagePath = image.getPath() + "\\" + image.list()[allTypes.indexOf(mainType)];
		                	typeImages.add(imagePath);
		                	System.out.println(imagePath);
		                	nodeAux.addAttribute("ui.style", "shape: circle; text-background-mode: plain; text-background-color: rgba(255,255,255,192); stroke-mode: none; size: 32px; fill-mode: image-scaled; fill-image: url('" + imagePath + "');");
		                }else{
		                	nodeAux.addAttribute("ui.style", "shape: circle; text-background-mode: plain; text-background-color: rgba(255,255,255,192); stroke-mode: none; size: 32px; fill-mode: image-scaled; fill-image: url('" + typeImages.get(allTypes.indexOf(mainType)) + "');");
		                }
		                //ontoUmlParser.getElement("");
		                nodeAux.addAttribute("ui.label", createLabel(mainType));
		                
		                ArrayList<Classifier> secList = xmlFile.getAtomSecondayTypes(atomLabel, worldLabel); 
		                for(j=0; j<secList.size(); j++) {
		                	String satLabel = atomLabel + "_sat$" + j;
		                	String orbitLabel = atomLabel + "_orbit$" + j;
	                    	org.graphstream.graph.Node sat = graph.addNode(atomLabel + "_sat$" + j);
	                    	org.graphstream.graph.Edge orbit = graph.addEdge(atomLabel + "_orbit$" + j, atomLabel, satLabel, false);
	                    	//sat.addAttribute("ui.label", TypeName.getTypeName(ontoUmlParser.getElement(typeList.get(j))));
	                    	sat.addAttribute("ui.style", "text-background-mode: plain; text-background-color: rgba(255,255,255,192); size: 12px; stroke-mode: none; fill-mode: image-scaled; fill-image: url('./resources/Heart-icon.png');");
	                    	sat.addAttribute("z", 1);
	                    	orbit.addAttribute("layout.weight", 1);
	                    	orbit.addAttribute("ui.style", "fill-color: rgba(0,0,0,0);");
	                    }
		                
		                /*
		                if(xmlFile.findAtom(atomLabel).isObject()) {
		                    nodeAux.addAttribute("ui.style", "shape: circle; text-background-mode: plain; text-background-color: rgba(255,255,255,192); stroke-mode: none; size: 32px; fill-mode: image-scaled; fill-image: url('./resources/Gamble-Clubs-icon.png');");
		                    nodeAux.addAttribute("z", 2);
		                    nodeAux.addAttribute("layout.weight", 0);
		                    //ArrayList<String> typeList = xmlFile.getAtomTypeOnWorld(atomLabel,"world_structure/CurrentWorld$0");
		                    
		                    for(j=0; j<typeList.size(); j++) {
		                    	if(TypeName.getTypeName(ontoUmlParser.getElement(typeList.get(j))).equals("Role")) {
		                    		org.graphstream.graph.Node sat = graph.addNode(atomLabel + "$" + j);
		                    		org.graphstream.graph.Edge orbit = graph.addEdge("orbit" + "$" + edgeId, atomLabel, atomLabel + "$" + j, false);
		                    		edgeId++;
		                    		//sat.addAttribute("ui.label", TypeName.getTypeName(ontoUmlParser.getElement(typeList.get(j))));
		                    		orbit.addAttribute("layout.weight", 1);
		                    		orbit.addAttribute("ui.style", "fill-color: rgba(0,0,0,0);");
		                    		sat.addAttribute("ui.style", "text-background-mode: plain; text-background-color: rgba(255,255,255,192); size: 12px; stroke-mode: none; fill-mode: image-scaled; fill-image: url('./resources/Heart-icon.png');");
		                    		sat.addAttribute("z", 1);
		                    		
		                    	}
		                    }
		                }
		                if(xmlFile.findAtom(atomLabel).isProperty()) {
		                	nodeAux.addAttribute("ui.style", "shape: circle; stroke-mode: none; size: 32px; fill-mode: image-scaled; fill-image: url('./resources/Gamble-Diamonds-icon.png');");
		                    //graph.getNode(exists.getTuples().get(i).get(1)).addAttribute("ui.style", "shape: box; size: 90px;");
		                }
		                if(xmlFile.findAtom(atomLabel).isDataType()) {
		                    graph.getNode(exists.getTuples().get(i).get(1)).addAttribute("ui.style", "shape: diamond; size: 90px;");
		                }
		                */
		            }
		        }
		        
		        break;
	        }
    	}
        
        for(i=0; i<fieldList.size(); i++) {
        	typesList = fieldList.get(i).getTypes();
            if(!fieldList.get(i).getTuples().isEmpty()) {
                System.out.println(fieldList.get(i).getTuples().get(0).get(0));
                if(fieldList.get(i).getTuples().get(0).size() > 2) {	// relation field has 3 atoms; 
                    tuplesList = fieldList.get(i).getTuples();
                    for(j=0; j<tuplesList.size(); j++) {
                        tuple = tuplesList.get(j);
                        if(worldLabel.equals(tuple.get(0))) {
                        	if(!xmlFile.findSigById(typesList.get(0).get(2)).isBuiltin()) {
                                //try {
                                	//String edgeType = TypeName.getTypeName(ontoUmlParser.getElement(fieldList.get(i).getLabel()));
                                String edgeName = fieldList.get(i).getLabel() + "$" + edgeId;
                                edgeId++;
                                
                                String satOutcomingLabel = tuple.get(1) + "_satOut$" + edgeId;
                                org.graphstream.graph.Node satOutcoming = graph.addNode(satOutcomingLabel);
                                satOutcoming.addAttribute("ui.style", "fill-color: green;");
                                edgeId++;
                                
                                String satIncomingLabel = tuple.get(2) + "_satIn$" + edgeId;
                                org.graphstream.graph.Node satIncoming = graph.addNode(satIncomingLabel);
		                    	satIncoming.addAttribute("ui.style", "fill-color: green;");
		                    	edgeId++;
		                    	
		                    	org.graphstream.graph.Edge orbitOutcoming = graph.addEdge(tuple.get(1) + "_orbitOut" + "$" + edgeId, tuple.get(1), satOutcomingLabel, false);
		                    	orbitOutcoming.addAttribute("layout.weight", 1);
		                    	orbitOutcoming.addAttribute("ui.style", "fill-color: rgba(0,0,0,0);");
		                    	edgeId++;
		                    		
		                    	org.graphstream.graph.Edge orbitIncoming = graph.addEdge(tuple.get(2) + "_orbitIn" + "$" + edgeId, tuple.get(2), satIncomingLabel, false);
		                    	orbitIncoming.addAttribute("ui.style", "fill-color: rgba(0,0,0,0);");
		                    	orbitIncoming.addAttribute("layout.weight", 1);
		                    	edgeId++;
		                    		
		                    	graph.addEdge(edgeName, satOutcomingLabel, satIncomingLabel, true).addAttribute("layout.weight", 10);
                                	/*
                                	edgeType = TypeName.getTypeName(ontoUmlParser.getElement(fieldList.get(i).getLabel()));
                                	String edgeName = fieldList.get(i).getLabel() + "$" + edgeId;//"[" + tuple.get(1) + "->" + tuple.get(2) + "]";
                                	System.out.println("NAME: " + edgeName);
                                	edgeId++;
                                	*/
                                	/*
                                    if(xmlFile.findSigById(typesList.get(0).get(2)).isBuiltin()) {
                                    	graph.addNode(tuple.get(2)).addAttribute("ui.label", tuple.get(2));
                                    	graph.getNode(tuple.get(2)).addAttribute("ui.style", "shape: circle; size: 10px; fill-color: black; size-mode: normal; text-alignment: at-left;");
                                    	//graph.getNode(tuple.get(2)).addAttribute("layout.weight", 0);
                                    	graph.getNode(tuple.get(2)).addAttribute("xyz", 1000, 1000, 10000);
                                    	graph.addEdge(edgeName, tuple.get(1), tuple.get(2), true);//.addAttribute("ui.label", fieldList.get(i).getLabel());
                                    	//graph.getEdge(edgeName).addAttribute("layout.weight", 1);
                                    }else{
                                    */
                                    	//graph.addEdge(edgeName, tuple.get(1), tuple.get(2), true).addAttribute("layout.weight", 5);//.addAttribute("ui.label", fieldList.get(i).getLabel() + " (" + edgeType + ")");
                                    //}
                                    //graph.getEdge(edgeName).addAttribute("layout.weight", 2);
                                //} catch (Exception e) {

                                //}
                        	}else{
                        		String attrName = "attr$";
                        		int attrId = 0;
                        		while(graph.getNode(tuple.get(1)).hasAttribute(attrName + attrId)) {
                        			attrId++;
                        		}
                        		System.out.println("ADDEDATTRTO" + graph.getNode(tuple.get(1)).getAttribute("ui.label"));
                        		graph.getNode(tuple.get(1)).addAttribute(attrName + attrId, fieldList.get(i).getLabel() + "\n" + tuple.get(2));
                        	}
                        }
                    }
                }
            }else{
            	if(fieldList.get(i).getTypes().get(0).size() > 2) {
            		
            	}
            }
        }
        
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.quality");
        graph.addAttribute("layout.quality", 4);
        //graph.addAttribute("layout.force", 2);
        graph.addAttribute("ui.stylesheet", "graph {\n" +
"    padding: 20px, 30px, 0px;\n" +
"}\n" +
"node {\n" +
//"    size: 300px, 50px;\n" +
//"    shape: box;\n" +
//"    fill-color: rgba(255,255,255,255);\n" +
"    text-size: 12;\n" +
"	 text-alignment: under;\n" +
//"    text-mode: truncated;\n" +
"    stroke-mode: plain;\n" +
"    stroke-color: black;\n" +
"}\n" +
"edge {\n" +
"    fill-color: black;\n" +
"    arrow-size: 4px, 4px;\n" +
"}\n" +
                /*
"node#Object$0 {\n" +
"    fill-color: blue;\n" +
"}\n" +
*/
"node:clicked {\n" +
"    size-mode: fit;\n" +
"    fill-color: red;\n" +
"}");
        
        this.selectedGraph = graph;
        return graph;
    }
    
    public org.graphstream.graph.Graph changeSelectedWorld(String worldLabel) {
    	ArrayList<Skolem> skolemList = xmlFile.getSkolemList();
        ArrayList<Atom> atomList = xmlFile.getAtomList();
        ArrayList<Sig> sigList = xmlFile.getSigList();
        ArrayList<Field> fieldList = xmlFile.getFieldList();
        
        ArrayList<Atom> objectList = xmlFile.getObjectList();
        ArrayList<Atom> propertyList = xmlFile.getPropertyList();
        ArrayList<Atom> dataTypeList = xmlFile.getDataTypeList();
        
        int i, j, k;
        
        //Graph graph = new MultiGraph(worldLabel);
        ArrayList<ArrayList<String>> tuplesList;
        ArrayList<String> tuple;
        ArrayList<ArrayList<Integer>> typesList;
        
        /*
        while(edgeKill.hasNext()) {
        	Edge ed = (Edge)edgeKill.next();
        	ed.addAttribute("kill", "");
        }
        
        while(nodeKill.hasNext()) {
        	Node no = (Node)nodeKill.next();
        	no.addAttribute("kill", "");
        }
        */
        while(selectedGraph.getNodeCount()!=0 || selectedGraph.getEdgeCount()!=0) {
	        Iterator nodeKill = selectedGraph.getNodeIterator();
	        Iterator edgeKill = selectedGraph.getEdgeIterator();
	        while(edgeKill.hasNext()) {
	        	selectedGraph.removeEdge((Edge)edgeKill.next());
	        }
	        
	        while(nodeKill.hasNext()) {
	        	selectedGraph.removeNode((Node)nodeKill.next());
	        }
	        System.out.println(selectedGraph.getNodeCount() + " and " + selectedGraph.getEdgeCount());
	        
        }
        
        // Creating nodes - we need the "exists" tuples:
        for(i=0; i<fieldList.size(); i++) {
        	if(fieldList.get(i).getLabel().equals("exists")) {
        		obj.Field exists = fieldList.get(i);
        		// After we find the "exists" field, we don't need to search anymore
		        for(i=0; i<exists.getTuples().size(); i++) {
		            if(exists.getTuple(i).get(0).equals(worldLabel)) {	// The first world to display is the current world.
		            	String atomLabel = exists.getTuple(i).get(1);
		                Node nodeAux = selectedGraph.addNode(atomLabel);
		                String mainType = xmlFile.getAtomMainType(atomLabel, worldLabel).getName();
		                if(!allTypes.contains(mainType)) {
		                	allTypes.add(mainType);
		                	typeAmount.add(0);
		                	File image = new File("./resources/gur project");
		                	String imagePath = image.getPath() + "\\" + image.list()[allTypes.indexOf(mainType)];
		                	typeImages.add(imagePath);
		                	System.out.println(imagePath);
		                	nodeAux.addAttribute("ui.style", "shape: circle; text-background-mode: plain; text-background-color: rgba(255,255,255,192); stroke-mode: none; size: 32px; fill-mode: image-scaled; fill-image: url('" + imagePath + "');");
		                }else{
		                	nodeAux.addAttribute("ui.style", "shape: circle; text-background-mode: plain; text-background-color: rgba(255,255,255,192); stroke-mode: none; size: 32px; fill-mode: image-scaled; fill-image: url('" + typeImages.get(allTypes.indexOf(mainType)) + "');");
		                }
		                //ontoUmlParser.getElement("");
		                nodeAux.addAttribute("ui.label", createLabel(mainType));
		                
		                ArrayList<Classifier> secList = xmlFile.getAtomSecondayTypes(atomLabel, worldLabel); 
		                for(j=0; j<secList.size(); j++) {
		                	String satLabel = atomLabel + "_sat$" + j;
		                	String orbitLabel = atomLabel + "_orbit$" + j;
	                    	org.graphstream.graph.Node sat = selectedGraph.addNode(atomLabel + "_sat$" + j);
	                    	org.graphstream.graph.Edge orbit = selectedGraph.addEdge(atomLabel + "_orbit$" + j, atomLabel, satLabel, false);
	                    	//sat.addAttribute("ui.label", TypeName.getTypeName(ontoUmlParser.getElement(typeList.get(j))));
	                    	sat.addAttribute("ui.style", "text-background-mode: plain; text-background-color: rgba(255,255,255,192); size: 12px; stroke-mode: none; fill-mode: image-scaled; fill-image: url('./resources/Heart-icon.png');");
	                    	sat.addAttribute("z", 1);
	                    	orbit.addAttribute("layout.weight", 1);
	                    	orbit.addAttribute("ui.style", "fill-color: rgba(0,0,0,0);");
	                    }
		                
		                /*
		                if(xmlFile.findAtom(atomLabel).isObject()) {
		                    nodeAux.addAttribute("ui.style", "shape: circle; text-background-mode: plain; text-background-color: rgba(255,255,255,192); stroke-mode: none; size: 32px; fill-mode: image-scaled; fill-image: url('./resources/Gamble-Clubs-icon.png');");
		                    nodeAux.addAttribute("z", 2);
		                    nodeAux.addAttribute("layout.weight", 0);
		                    //ArrayList<String> typeList = xmlFile.getAtomTypeOnWorld(atomLabel,"world_structure/CurrentWorld$0");
		                    
		                    for(j=0; j<typeList.size(); j++) {
		                    	if(TypeName.getTypeName(ontoUmlParser.getElement(typeList.get(j))).equals("Role")) {
		                    		org.graphstream.graph.Node sat = graph.addNode(atomLabel + "$" + j);
		                    		org.graphstream.graph.Edge orbit = graph.addEdge("orbit" + "$" + edgeId, atomLabel, atomLabel + "$" + j, false);
		                    		edgeId++;
		                    		//sat.addAttribute("ui.label", TypeName.getTypeName(ontoUmlParser.getElement(typeList.get(j))));
		                    		orbit.addAttribute("layout.weight", 1);
		                    		orbit.addAttribute("ui.style", "fill-color: rgba(0,0,0,0);");
		                    		sat.addAttribute("ui.style", "text-background-mode: plain; text-background-color: rgba(255,255,255,192); size: 12px; stroke-mode: none; fill-mode: image-scaled; fill-image: url('./resources/Heart-icon.png');");
		                    		sat.addAttribute("z", 1);
		                    		
		                    	}
		                    }
		                }
		                if(xmlFile.findAtom(atomLabel).isProperty()) {
		                	nodeAux.addAttribute("ui.style", "shape: circle; stroke-mode: none; size: 32px; fill-mode: image-scaled; fill-image: url('./resources/Gamble-Diamonds-icon.png');");
		                    //graph.getNode(exists.getTuples().get(i).get(1)).addAttribute("ui.style", "shape: box; size: 90px;");
		                }
		                if(xmlFile.findAtom(atomLabel).isDataType()) {
		                    graph.getNode(exists.getTuples().get(i).get(1)).addAttribute("ui.style", "shape: diamond; size: 90px;");
		                }
		                */
		            }
		        }
		        
		        break;
	        }
    	}
        
        for(i=0; i<fieldList.size(); i++) {
        	typesList = fieldList.get(i).getTypes();
            if(!fieldList.get(i).getTuples().isEmpty()) {
                System.out.println(fieldList.get(i).getTuples().get(0).get(0));
                if(fieldList.get(i).getTuples().get(0).size() > 2) {	// relation field has 3 atoms; 
                    tuplesList = fieldList.get(i).getTuples();
                    for(j=0; j<tuplesList.size(); j++) {
                        tuple = tuplesList.get(j);
                        if(worldLabel.equals(tuple.get(0))) {
                        	if(!xmlFile.findSigById(typesList.get(0).get(2)).isBuiltin()) {
                                //try {
                                	//String edgeType = TypeName.getTypeName(ontoUmlParser.getElement(fieldList.get(i).getLabel()));
                                String edgeName = fieldList.get(i).getLabel() + "$" + edgeId;
                                edgeId++;
                                
                                String satOutcomingLabel = tuple.get(1) + "_satOut$" + edgeId;
                                org.graphstream.graph.Node satOutcoming = selectedGraph.addNode(satOutcomingLabel);
                                satOutcoming.addAttribute("ui.style", "fill-color: green;");
                                edgeId++;
                                
                                String satIncomingLabel = tuple.get(2) + "_satIn$" + edgeId;
                                org.graphstream.graph.Node satIncoming = selectedGraph.addNode(satIncomingLabel);
		                    	satIncoming.addAttribute("ui.style", "fill-color: green;");
		                    	edgeId++;
		                    	
		                    	org.graphstream.graph.Edge orbitOutcoming = selectedGraph.addEdge(tuple.get(1) + "_orbitOut" + "$" + edgeId, tuple.get(1), satOutcomingLabel, false);
		                    	orbitOutcoming.addAttribute("layout.weight", 1);
		                    	orbitOutcoming.addAttribute("ui.style", "fill-color: rgba(0,0,0,0);");
		                    	edgeId++;
		                    		
		                    	org.graphstream.graph.Edge orbitIncoming = selectedGraph.addEdge(tuple.get(2) + "_orbitIn" + "$" + edgeId, tuple.get(2), satIncomingLabel, false);
		                    	orbitIncoming.addAttribute("ui.style", "fill-color: rgba(0,0,0,0);");
		                    	orbitIncoming.addAttribute("layout.weight", 1);
		                    	edgeId++;
		                    		
		                    	selectedGraph.addEdge(edgeName, satOutcomingLabel, satIncomingLabel, true).addAttribute("layout.weight", 10);
                                	/*
                                	edgeType = TypeName.getTypeName(ontoUmlParser.getElement(fieldList.get(i).getLabel()));
                                	String edgeName = fieldList.get(i).getLabel() + "$" + edgeId;//"[" + tuple.get(1) + "->" + tuple.get(2) + "]";
                                	System.out.println("NAME: " + edgeName);
                                	edgeId++;
                                	*/
                                	/*
                                    if(xmlFile.findSigById(typesList.get(0).get(2)).isBuiltin()) {
                                    	graph.addNode(tuple.get(2)).addAttribute("ui.label", tuple.get(2));
                                    	graph.getNode(tuple.get(2)).addAttribute("ui.style", "shape: circle; size: 10px; fill-color: black; size-mode: normal; text-alignment: at-left;");
                                    	//graph.getNode(tuple.get(2)).addAttribute("layout.weight", 0);
                                    	graph.getNode(tuple.get(2)).addAttribute("xyz", 1000, 1000, 10000);
                                    	graph.addEdge(edgeName, tuple.get(1), tuple.get(2), true);//.addAttribute("ui.label", fieldList.get(i).getLabel());
                                    	//graph.getEdge(edgeName).addAttribute("layout.weight", 1);
                                    }else{
                                    */
                                    	//graph.addEdge(edgeName, tuple.get(1), tuple.get(2), true).addAttribute("layout.weight", 5);//.addAttribute("ui.label", fieldList.get(i).getLabel() + " (" + edgeType + ")");
                                    //}
                                    //graph.getEdge(edgeName).addAttribute("layout.weight", 2);
                                //} catch (Exception e) {

                                //}
                        	}else{
                        		String attrName = "attr$";
                        		int attrId = 0;
                        		while(selectedGraph.getNode(tuple.get(1)).hasAttribute(attrName + attrId)) {
                        			attrId++;
                        		}
                        		System.out.println("ADDEDATTRTO" + selectedGraph.getNode(tuple.get(1)).getAttribute("ui.label"));
                        		selectedGraph.getNode(tuple.get(1)).addAttribute(attrName + attrId, fieldList.get(i).getLabel() + "\n" + tuple.get(2));
                        	}
                        }
                    }
                }
            }else{
            	if(fieldList.get(i).getTypes().get(0).size() > 2) {
            		
            	}
            }
        }
        
        selectedGraph.addAttribute("ui.antialias");
        selectedGraph.addAttribute("ui.quality");
        selectedGraph.addAttribute("layout.quality", 4);
        //graph.addAttribute("layout.force", 2);
        selectedGraph.addAttribute("ui.stylesheet", "graph {\n" +
"    padding: 20px, 30px, 0px;\n" +
"}\n" +
"node {\n" +
//"    size: 300px, 50px;\n" +
//"    shape: box;\n" +
//"    fill-color: rgba(255,255,255,255);\n" +
"    text-size: 12;\n" +
"	 text-alignment: under;\n" +
//"    text-mode: truncated;\n" +
"    stroke-mode: plain;\n" +
"    stroke-color: black;\n" +
"}\n" +
"edge {\n" +
"    fill-color: black;\n" +
"    arrow-size: 4px, 4px;\n" +
"}\n" +
                /*
"node#Object$0 {\n" +
"    fill-color: blue;\n" +
"}\n" +
*/
"node:clicked {\n" +
"    size-mode: fit;\n" +
"    fill-color: red;\n" +
"}");
    	return selectedGraph;
    }
    
    public View showWorldGraph() {
        viewer1 = new Viewer(worldGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer1.disableAutoLayout();
        view1 = viewer1.addDefaultView(false);   // false indicates "no JFrame".
        view1.getCamera().setViewCenter(0, 0, 0);

        return view1;
    }
    
    public View showSelectedGraph() {
        viewer = new Viewer(selectedGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        //viewer.enableAutoLayout();
        //Viewer v = new Viewer(g, Viewer.TheadingModel.GRAPH_IN_ANOTHER_THREAD);
        SpringBox layout = new SpringBox();
        layout.freezeNode("Object$1", true);
        //layout.moveNode("Object$1", 0, 0, 0);
        layout.setForce(1);
        //layout.configure(2, 2, false, 1);
        viewer.enableAutoLayout(layout);
        view = viewer.addDefaultView(false);   // false indicates "no JFrame".
        //You can find two layouts in gs-core, LinLog and SpringBox. There is also an adaptation of the OpenOrd layout here : https://github.com/gsavin/gs-openord
        return view;
    }

    private String createLabel(String label) {
    	int id = typeAmount.get(allTypes.indexOf(label));
    	if(label.length() <= 1) {
    		typeAmount.set(allTypes.indexOf(label), id+1);
    		return label + id;
    	}
    	String newLabel = "" + label.charAt(0);
    	
    	for(int i=1; i<label.length(); i++) {
    		String verify = "" + label.charAt(i);
    		if(!verify.matches("a|e|i|o|u|A|E|I|O|U")) {
    			newLabel = newLabel + verify;
    		}
    	}
    	newLabel = newLabel + id;
    	typeAmount.set(allTypes.indexOf(label), id+1);
    	return newLabel;
    }
    
    public XMLFile getXmlFile() {
        return xmlFile;
    }

    public void setXmlFile(XMLFile xmlFile) {
        this.xmlFile = xmlFile;
    }

    public Graph getSelectedGraph() {
        return selectedGraph;
    }

    public void setSelectedGraph(Graph selectedGraph) {
        this.selectedGraph = selectedGraph;
    }
    
    public Graph getWorldGraph() {
        return worldGraph;
    }

    public void setWorldGraph(Graph worldGraph) {
        this.worldGraph = worldGraph;
    }

    public Viewer getViewer() {
        return viewer;
    }

    public void setViewer(Viewer viewer) {
        this.viewer = viewer;
    }
    
    public Viewer getViewer1() {
        return viewer1;
    }

    public void setViewer1(Viewer viewer1) {
        this.viewer1 = viewer1;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
    
    public View getView1() {
        return view1;
    }

    public void setView1(View view1) {
        this.view1 = view1;
    }

	public OntoUMLParser getOntoUmlParser() {
		return ontoUmlParser;
	}

	public void setOntoUmlParser(OntoUMLParser ontoUmlParser) {
		this.ontoUmlParser = ontoUmlParser;
	}

	public ArrayList<String> getAllTypes() {
		return allTypes;
	}

	public void setAllTypes(ArrayList<String> allTypes) {
		this.allTypes = allTypes;
	}

	public ArrayList<String> getTypeImages() {
		return typeImages;
	}

	public void setTypeImages(ArrayList<String> typeImages) {
		this.typeImages = typeImages;
	}

	public ArrayList<Integer> getTypeAmount() {
		return typeAmount;
	}

	public void setTypeAmount(ArrayList<Integer> typeAmount) {
		this.typeAmount = typeAmount;
	}
    
    
    
}
