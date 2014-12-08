package br.ufes.inf.nemo.pattern.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import RefOntoUML.Classifier;
import RefOntoUML.Collective;
import RefOntoUML.Generalization;
import RefOntoUML.Kind;
import RefOntoUML.Mixin;
import RefOntoUML.Package;
import RefOntoUML.Phase;
import RefOntoUML.Quantity;
import RefOntoUML.Role;
import RefOntoUML.parser.OntoUMLParser;
import br.ufes.inf.nemo.assistant.util.UtilAssistant;
import br.ufes.inf.nemo.common.ontoumlfixer.Fix;
import br.ufes.inf.nemo.common.ontoumlfixer.OutcomeFixer;

public class MixinPattern extends AbstractPattern{

	public MixinPattern(OntoUMLParser parser, double x, double y) {
		super(parser, x, y, "/resource/MixinPattern.png", "Mixin Pattern");
	}

	@Override
	public void runPattern() {
		HashMap<String, String[]> hashTree = new HashMap<>();
		Set<? extends Classifier> set;

		set = parser.getAllInstances(Kind.class);
		if(!set.isEmpty())
			hashTree.put("Kind", UtilAssistant.getStringRepresentationClass(set));

		set = parser.getAllInstances(Collective.class);
		if(!set.isEmpty())
			hashTree.put("Collective", UtilAssistant.getStringRepresentationClass(set));

		set = parser.getAllInstances(Quantity.class);
		if(!set.isEmpty())
			hashTree.put("Quantity", UtilAssistant.getStringRepresentationClass(set));

		set = parser.getAllInstances(Phase.class);
		if(!set.isEmpty())
			hashTree.put("Phase", UtilAssistant.getStringRepresentationClass(set));

		set = parser.getAllInstances(Role.class);
		if(!set.isEmpty())
			hashTree.put("Role", UtilAssistant.getStringRepresentationClass(set));

		set = parser.getAllInstances(Mixin.class);
		if(!set.isEmpty())
			hashTree.put("Mixin", UtilAssistant.getStringRepresentationClass(set));


		dym.addHashTree(hashTree);

		dym.addTableLine("mixin", "Mixin", new String[] {"Mixin"});

		dym.addTableLine("sortal", "Sortal", new String[] {"Kind","Collective", "Quantity"});

		dym.addTableLine("antirigidsortal", "Anti Rigid Sortal", new String[] {"Role","Phase"});

		dm.open();

	}

	@Override
	public Fix getFix(){
		try{
			Package root = parser.getModel();
			outcomeFixer = new OutcomeFixer(root);
			fix = new Fix();
			Fix _fix = new Fix();

			ArrayList<Generalization> generalizationList = new ArrayList<>();
			
			ArrayList<Object[]> mixins = dym.getRowsOf("mixin");
			ArrayList<Object[]> sortals = dym.getRowsOf("sortal");
			ArrayList<Object[]> antirigids = dym.getRowsOf("antirigidsortal");

			Classifier mixin 	= getClassifier(mixins.get(0), x, y);
			Classifier sortal 	= getClassifier(sortals.get(0), x, y+horizontalDistance);
			Classifier antirigid 	= getClassifier(antirigids.get(0),x+(1*verticalDistance)/3, y+horizontalDistance);

			
			if(mixin != null){
				if(sortal != null){
					_fix = outcomeFixer.createGeneralization(sortal, mixin);
					Generalization generalization = (Generalization) _fix.getAdded().get(_fix.getAdded().size()-1);
					generalizationList.add(generalization);
				}

				if(antirigid != null){
					_fix = outcomeFixer.createGeneralization(antirigid, mixin);
					Generalization generalization = (Generalization) _fix.getAdded().get(_fix.getAdded().size()-1);
					generalizationList.add(generalization);
				}
				if(sortal != null && antirigid != null){
					fix.addAll(_fix);
					fix.addAll(outcomeFixer.createGeneralizationSet(generalizationList, true, true, "partition"+UtilAssistant.getCont()));
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return fix;
	}

}
