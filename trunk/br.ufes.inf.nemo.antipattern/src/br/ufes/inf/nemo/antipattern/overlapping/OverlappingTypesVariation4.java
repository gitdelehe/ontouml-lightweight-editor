package br.ufes.inf.nemo.antipattern.overlapping;

import java.util.ArrayList;

import RefOntoUML.Classifier;
import RefOntoUML.Generalization;
import RefOntoUML.GeneralizationSet;
import RefOntoUML.Phase;
import RefOntoUML.Property;
import RefOntoUML.Role;
import RefOntoUML.SortalClass;
import RefOntoUML.SubKind;
import br.ufes.inf.nemo.antipattern.OverlappingTypesIdentificator;

//partEnds with the same type
public class OverlappingTypesVariation4 extends OverlappingTypesVariation {
	
	boolean hasOverlappingGS;
	Classifier identityProvider,closestSupertpe;
	ArrayList<Classifier> commonSupertypes;
	ArrayList<GeneralizationSet> genSets;
	
	public OverlappingTypesVariation4 (OverlappingOccurrence wholeOver, ArrayList<Property> sortalOverlappingProperties)throws Exception {
		super(wholeOver, sortalOverlappingProperties);
		
		//all types must be subkinds, phases or roles
		for (Classifier type : super.overlappingTypes) {
			if(!(type instanceof SubKind) && !(type instanceof Phase) && !(type instanceof Role))
				throw new Exception("VAR4: All parts must be subkinds, phases or roles.");
		}
		
		//all types must be have the same identity provider or none at all (in case of partial models)
		identityProvider = null;
		for (Property p : sortalOverlappingProperties) {
			Classifier currentIdentityProvider = OverlappingTypesIdentificator.getIdentityProvider((SortalClass) p.getType());

			if(identityProvider==null)
				identityProvider=currentIdentityProvider;
			else if (!identityProvider.equals(currentIdentityProvider))
				throw new Exception("VAR4: Different identity providers.");
		}
		
		this.genSets = new ArrayList<GeneralizationSet>();
		this.commonSupertypes = new ArrayList<Classifier>();
		if (!OverlappingTypesIdentificator.allTypesOverlap(super.overlappingTypes, commonSupertypes,genSets))
			throw new Exception("VAR: Disjoint by supertypes.");
		
		if(genSets.size()>0)
			hasOverlappingGS = true;
		
		getClosestSupertype();
		
		super.validVariation = true;
	}

	private void getClosestSupertype(){
		
		closestSupertpe = null;
		
		for (Classifier supertype : this.commonSupertypes) {
			if(closestSupertpe == null)
				closestSupertpe = supertype;
			else if (supertype.allParents().contains(closestSupertpe))
				closestSupertpe = supertype;				
		}
	}
	
	
	@Override
	public String toString(){
		String result =	"VAR4" +
						"\nIdentity Provider: "+occurrence.getParser().getStringRepresentation(identityProvider)+
						"\nCommon Supertypes: ";
		
		for (Classifier parent : this.commonSupertypes) {
			result+="\n\t"+occurrence.getParser().getStringRepresentation(parent);
		}
		
		result += "\nPart Ends: ";
		
		for (Property p : overlappingProperties) {
			result+="\n\t"+occurrence.getParser().getStringRepresentation(p);
		}
		
		return result;
	}


	@Override
	public boolean makeEndsDisjoint(ArrayList<Property> partEnds) {
		
		if(!this.overlappingProperties.containsAll(partEnds))
			return false;
		
		ArrayList<GeneralizationSet> gss = OverlappingTypesIdentificator.getSubtypesGeneralizationSets(closestSupertpe);
		
		ArrayList<Classifier> subtypes = new ArrayList<> ();
		for (Property property : partEnds) {
			subtypes.add((Classifier) property.getType());
		}
		
		if(gss.size()==0)
			occurrence.getFix().addAll(occurrence.getFixer().createGeneralizationSet(closestSupertpe, subtypes));
		else{
			boolean isFixed = false;
			for (GeneralizationSet gs : gss) {
				ArrayList<Classifier> allGsChildren = new ArrayList<Classifier>();
				
				for (Generalization g : gs.getGeneralization()) {
					allGsChildren.add(g.getSpecific());
					allGsChildren.addAll(g.getSpecific().allChildren());
				}
				
				if (allGsChildren.containsAll(subtypes)){
					gs.setIsDisjoint(true);
					isFixed = true;
					break;
				}
			}
			
			if(!isFixed)
				occurrence.getFix().addAll(occurrence.getFixer().createGeneralizationSet(closestSupertpe, subtypes));
		}
			
		return true;
	}
	
}
