package br.ufes.inf.nemo.antipattern.wizard.mixrig;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;

import RefOntoUML.Classifier;
import br.ufes.inf.nemo.antipattern.mixrig.MixRigOccurrence;
import br.ufes.inf.nemo.antipattern.wizard.AntiPatternAction;

public class MixRigAction extends AntiPatternAction<MixRigOccurrence>{

	public enum Action {CHANGE_MIXIN_STEREOTYPE, ADD_SUBTYPES};
	
	public ArrayList<Classifier> existingSubtypes;
	public HashMap<String, Class<?>> newTypes;
	
	public MixRigAction(MixRigOccurrence ap) {
		super(ap);
	}

	@Override
	public void run() {
		if(code==Action.CHANGE_MIXIN_STEREOTYPE){
			ap.changeMixinStereotype();
		}
		else if(code==Action.ADD_SUBTYPES) {
			if(existingSubtypes!=null && existingSubtypes.size()>0)
				ap.addExistingSubtypes(existingSubtypes);
			if(newTypes!=null && newTypes.keySet().size()>0)
				ap.addNewSubtypes(newTypes);
		}
	}
	
	public void setChangeMixinStereotype(){
		code = Action.CHANGE_MIXIN_STEREOTYPE;
	}
	
	public void setAddSubtypes(ArrayList<Classifier> existingSubtypes, HashMap<String, Class<?>> newTypes){
		code = Action.ADD_SUBTYPES;
		this.existingSubtypes = existingSubtypes;
		this.newTypes = newTypes;
	}
		
	@Override
	public String toString(){
		String result = new String();
		if(code==Action.CHANGE_MIXIN_STEREOTYPE) {
			result += "Change Class: Change stereotype of <"+ap.getMixin().getName()+"> to ";
			if(ap.rigidSubtypes())
				result += "«Category»";
			else
				result += "«RoleMixin»";
		}
		else if(code==Action.ADD_SUBTYPES){
			
			for (Classifier subtype : existingSubtypes)
				result+="Add Generalization: from "+subtype.getName()+" to "+ap.getMixin().getName()+"\n";
			
			for (String name : newTypes.keySet()) {
				result+="Add Class: «"+getStereotypeName(newTypes.get(name))+"» "+name+"\n";;
				result+="Add Generalization: from "+name+" to "+ap.getMixin().getName()+"\n";;
			}
			
		}
		
		return result;
	}
	
	private String getStereotypeName(Class<?> stereotype){
		String type = stereotype.getName().replaceAll("RefOntoUML.impl.","");
		type = type.replaceAll("class","");
		type = type.replaceAll("Impl","");
	    type = Normalizer.normalize(type, Normalizer.Form.NFD);
	    if (!type.equalsIgnoreCase("association")) type = type.replace("Association","");
	    return type;
	}
}
