package br.ufes.inf.nemo.antipattern.wizard.gsrig;

import java.text.Normalizer;
import java.util.ArrayList;

import org.eclipse.emf.ecore.EObject;

import RefOntoUML.Classifier;
import br.ufes.inf.nemo.antipattern.GSRig.GSRigOccurrence;
import br.ufes.inf.nemo.antipattern.wizard.AntiPatternAction;
import br.ufes.inf.nemo.common.ontoumlparser.ParsingElement;

public class GSRigAction  extends AntiPatternAction<GSRigOccurrence>{
		
	ArrayList<String> specificsNewStereotypes = new ArrayList<String>();
	ArrayList<String> antirigids = new ArrayList<String>();
	
	public GSRigAction(GSRigOccurrence ap) 
	{
		super(ap);
	}

	public enum Action { DELETE_GS, CREATE_GS_FOR_RIGIDS , CREATE_GS_FOR_ANTIRIGIDS, CREATE_GS_FOR_BOTH, CHANGE_SUPERTYPE_TO_MIXIN, 
						 CREATE_OCL_DERIVATION_BY_NEGATION, CREATE_RIGID_SUBTYPE_FOR_ANTIRIGIDS, CREATE_COMMON_SUBTYPE_FOR_ANTIRIGIDS, 
						 CHANGE_SPECIFICS_STEREOTYPES }
	
	public void setDeleteGS()
	{
		code = Action.DELETE_GS;		
	}
		
	public void setCreateGSForRigids()
	{
		code = Action.CREATE_GS_FOR_RIGIDS;		
	}
	
	public void setCreateRigidSubtypeForAntiRigids()
	{
		code = Action.CREATE_RIGID_SUBTYPE_FOR_ANTIRIGIDS;
	}
	
	public void setCreateRigidSubtypeForAntiRigids(ArrayList<String> antirigids)
	{
		code = Action.CREATE_RIGID_SUBTYPE_FOR_ANTIRIGIDS;
		this.antirigids = antirigids;
	}
	
	public void setCreateGSForAntiRigids()
	{
		code = Action.CREATE_GS_FOR_ANTIRIGIDS;
	}	

	public void setCreateGSForBoth()
	{
		code = Action.CREATE_GS_FOR_BOTH;		
	}
	
	public void setChangeSuperTypeToMixin()
	{
		code = Action.CHANGE_SUPERTYPE_TO_MIXIN;
	}	
	
	public void setCreateDerivationByNegation()
	{
		code = Action.CREATE_OCL_DERIVATION_BY_NEGATION;
	}
	
	public void setCreateCommonSubtypeForAntiRigids()
	{
		code = Action.CREATE_COMMON_SUBTYPE_FOR_ANTIRIGIDS;	
	}
		
	public void setCreateCommonSubtypeForAntiRigids(ArrayList<String> antirigids)
	{
		code = Action.CREATE_COMMON_SUBTYPE_FOR_ANTIRIGIDS;
		this.antirigids = antirigids;
	}
	
	public void setChangeSpecificsStereotypesTo(ArrayList<String> newStereotypes) 
	{		
		code = Action.CHANGE_SPECIFICS_STEREOTYPES;
		specificsNewStereotypes = newStereotypes;
	}
	
	@Override
	public void run() 
	{
		if(code==Action.DELETE_GS) ap.deleteGenSet();
		if(code==Action.CREATE_GS_FOR_RIGIDS) { ap.createGenSetForRigids(); ap.deleteGenSet(); }
		if(code==Action.CREATE_GS_FOR_ANTIRIGIDS) { ap.createGenSetForAntiRigids(); ap.deleteGenSet(); }
		if(code==Action.CREATE_GS_FOR_BOTH) { ap.createGenSetForBoth(); ap.deleteGenSet(); }
		if(code==Action.CHANGE_SUPERTYPE_TO_MIXIN) { ap.changeSuperTypeToMixin(); }
		if(code==Action.CREATE_OCL_DERIVATION_BY_NEGATION) { ap.createOclDerivationByNegation(); }
		if(code==Action.CREATE_RIGID_SUBTYPE_FOR_ANTIRIGIDS) { if(antirigids.size()>0) ap.createRigidSubtypeForAntiRigidsFrom(antirigids); else ap.createRigidSubtypeForAntiRigids(ap.getAntiRigidSpecifics()); }		
		if(code==Action.CREATE_COMMON_SUBTYPE_FOR_ANTIRIGIDS) { if(antirigids.size()>0) ap.createCommonSubtypeForAntiRigidsFrom(antirigids); else ap.createCommonSubtypeForAntiRigids(ap.getAntiRigidSpecifics());}
		if(code==Action.CHANGE_SPECIFICS_STEREOTYPES) { ap.changeSpecificsStereotypes(specificsNewStereotypes); } 
	} 
	
	public static String getStereotype(EObject element)
	{
		String type = element.getClass().toString().replaceAll("class RefOntoUML.impl.","");
	    type = type.replaceAll("Impl","");
	    type = Normalizer.normalize(type, Normalizer.Form.NFD);
	    if (!type.equalsIgnoreCase("association")) type = type.replace("Association","");
	    return type;
	}
	
	@Override
	public String toString(){
		String result = new String();
		
		if (code == Action.DELETE_GS)
		{
			result += "Delete "+(new ParsingElement(ap.getGs(),false,""));
		}
		if (code == Action.CREATE_GS_FOR_RIGIDS)
		{
			result += "Create new <<GeneralizationSet>> for rigids: ";
			int i=0;
			for(Classifier c: ap.getRigidSpecifics()) { 
				if(i==ap.getRigidSpecifics().size()-1) {
					result += getStereotype(c)+" "+c.getName(); 
				}else{
					result += getStereotype(c)+" "+c.getName()+",";
				}
			}
			result += "Delete "+(new ParsingElement(ap.getGs(),false,""));
		}	
		if (code == Action.CREATE_GS_FOR_ANTIRIGIDS)
		{
			result += "Create new <<GeneralizationSet>> for antirigids: ";
			int i=0;
			for(Classifier c: ap.getAntiRigidSpecifics()) { 
				if(i==ap.getAntiRigidSpecifics().size()-1) {
					result += getStereotype(c)+" "+c.getName(); 
				}else{
					result += getStereotype(c)+" "+c.getName()+",";
				}
			}
			result += "Delete "+(new ParsingElement(ap.getGs(),false,""));
		}			
		if (code == Action.CREATE_GS_FOR_BOTH)
		{
			result += "Create new <<GeneralizationSet>> for rigids: ";
			int i=0;
			for(Classifier c: ap.getRigidSpecifics()) { 
				if(i==ap.getRigidSpecifics().size()-1) {
					result += getStereotype(c)+" "+c.getName(); 
				}else{
					result += getStereotype(c)+" "+c.getName()+",";
				}
			}
			
			result += "Create new <<GeneralizationSet>> for antirigids: ";
			i=0;
			for(Classifier c: ap.getAntiRigidSpecifics()) { 
				if(i==ap.getAntiRigidSpecifics().size()-1) {
					result += getStereotype(c)+" "+c.getName(); 
				}else{
					result += getStereotype(c)+" "+c.getName()+",";
				}
			}
			result += "Delete "+(new ParsingElement(ap.getGs(),false,""));
		}
		if(code == Action.CHANGE_SUPERTYPE_TO_MIXIN)
		{
			result += "Change supertype <<"+getStereotype(ap.getGs().getGeneralization().get(0).getGeneral())+">> "+ap.getGs().getGeneralization().get(0).getGeneral().getName()+" to <<Mixin>>";
		}
		if(code == Action.CREATE_OCL_DERIVATION_BY_NEGATION)
		{
			result += "Create OCL derivation by negation";
		}
		if(code == Action.CREATE_RIGID_SUBTYPE_FOR_ANTIRIGIDS)
		{
			result += "Creating rigid subtypes to antirigids: ";
			if(antirigids.size()==0){			
				int i=0;
				for(Classifier c: ap.getAntiRigidSpecifics()){
					if(i==ap.getAntiRigidSpecifics().size()-1) result += getStereotype(c)+" "+c.getName(); 
					else result += getStereotype(c)+" "+c.getName()+", ";
					i++;
				}
			}else{
				int i=0;
				for(String c: antirigids){
					if(i==ap.getAntiRigidSpecifics().size()-1) result += c; 
					else result += c+", ";
					i++;
				}
			}
		}
		if(code == Action.CREATE_COMMON_SUBTYPE_FOR_ANTIRIGIDS)
		{
			result += "Creating common subtype to antirigids: ";
			if(antirigids.size()==0){	
				int i=0;
				for(Classifier c: ap.getAntiRigidSpecifics()){
					if(i==ap.getAntiRigidSpecifics().size()-1) result += getStereotype(c)+" "+c.getName(); 
					else result += getStereotype(c)+" "+c.getName()+", ";
					i++;
				}
			}else{
				int i=0;
				for(String c: antirigids){
					if(i==ap.getAntiRigidSpecifics().size()-1) result += c; 
					else result += c+", ";
					i++;
				}
			}			
		}
		if(code == Action.CHANGE_SPECIFICS_STEREOTYPES)
		{
			result += "Changing stereotypes of specific subtypes to: ";
			for(String str: specificsNewStereotypes){
				result += "<<"+str+">>"+", ";
			}
			result += " respectively.";
		}
		return result;
	}

}

