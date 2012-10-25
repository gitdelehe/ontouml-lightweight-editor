package br.ufes.inf.nemo.ontouml.antipattern;

import java.util.ArrayList;
import java.util.HashMap;

import br.ufes.inf.nemo.ontouml.antipattern.mapper.NamesMapper;
import br.ufes.inf.nemo.ontouml.antipattern.util.AlloyConstructor;
import br.ufes.inf.nemo.ontouml.antipattern.util.ArrayListOperations;
import br.ufes.inf.nemo.ontouml.antipattern.util.Combination;

import RefOntoUML.Classifier;
import RefOntoUML.Mediation;
import RefOntoUML.Relator;

public class RWORAntiPattern {
	private Relator relator;
	private Classifier supertype;
	private HashMap<Mediation, Classifier> mediations;
	
	public RWORAntiPattern(Relator relator) throws Exception {
		this.setRelator(relator);
	}
	
	public String generateExclusivePredicate(NamesMapper mapper){
		String predicate, rules, predicateName, relatorName;
		ArrayList<Object> saida, mediations = new ArrayList<>();
		Combination comb1;
		
		HashMap<String, String> mediationsName = new HashMap<>();
				
		relatorName=mapper.elementsMap.get(relator);
		for (Mediation med : this.mediations.keySet())
			mediationsName.put(mapper.elementsMap.get(med), mapper.elementsMap.get(med.mediated()));
		
		comb1 = new Combination(mediations, 2);
		
		predicateName = "exclusiveRole_"+relatorName;
		rules = "some w:World | some x:w."+relatorName+" | ";
		
		// Combinacoes de mediations agrupadas 2 a 2
        while (comb1.hasNext()) {
            saida = comb1.next();
            rules+="# (x.(w."+saida.get(0)+") & x.(w."+saida.get(1)+")) = 0";
            
            if(comb1.hasNext())
            	rules+=" and ";
        }
		
		predicate = AlloyConstructor.AlloyParagraph(predicateName, rules, AlloyConstructor.PRED);
		predicate += AlloyConstructor.RunCheckCommand(predicateName, "10", "1", AlloyConstructor.PRED)+"\n";
		
		return predicate;
	}
	
	public String generateNonExclusivePredicate(NamesMapper mapper){
		String predicate, rules, predicateName, relatorName;
		HashMap<String, String> mediationsName = new HashMap<>();
		int i=0;
		
		relatorName=mapper.elementsMap.get(relator);
		for (Mediation med : this.mediations.keySet())
			mediationsName.put(mapper.elementsMap.get(med), mapper.elementsMap.get(med.mediated()));
		
		predicateName = "nonExclusiveRole_"+relatorName;
		rules = "some w:World | some x:w."+relatorName+" | # (";
				
		for (String med : mediationsName.keySet()) {
			rules+="x.(w."+med+")";
			
			if (i < mediationsName.keySet().size()-1)
				 rules += " & ";
			
			i++;
		}
			
		rules+=") > 0";
		predicate = AlloyConstructor.AlloyParagraph(predicateName, rules, AlloyConstructor.PRED);
		predicate += AlloyConstructor.RunCheckCommand(predicateName, "10", "1", AlloyConstructor.PRED)+"\n";
		
		return predicate;
	}
		
	public String generateAllMultipleExclusivePredicate(NamesMapper mapper){
		String predicates="", rules, predicateName, relatorName;
		ArrayList<Object>  mediations = new ArrayList<>(), output = new ArrayList<>(), output2, aux;
		HashMap<String, String> mediationsName = new HashMap<>();
		Combination comb1, comb2;
		
		relatorName=mapper.elementsMap.get(relator);
		for (Mediation med : this.mediations.keySet())
			mediationsName.put(mapper.elementsMap.get(med), mapper.elementsMap.get(med.mediated()));
		
		comb1 = new Combination(mediations, 2);
		
		// Generate all the combinations of the mediation 2 by 2
        while (comb1.hasNext()) {
            output.add(comb1.next());
        }
        
        /*Generates all the combinations of any size with the comb1 result*/ 
        comb2 = new Combination(output,0);
        
        while (comb2.hasNext()) {
            output2=(comb2.next());
            
            predicateName = "MultipleExclusiveRole_"+relatorName;
            rules = "some w:World | some x:w."+relatorName+" | "; 

            for (int n=0;n<output2.size();n++){
            	predicateName += n;
            	rules+="# (";
            	for (int n2=0; n2<((ArrayList<Object>)(output2).get(n)).size();n2++) {
	            	
            		rules+="x.(w."+((ArrayList<Object>) output2.get(n)).get(n2)+")";
	            	
	            	if (n2==((ArrayList<Object>)(output2).get(n)).size()-1)
	            		rules+=") = 0";
	            	else
	            		rules+=" & ";
            	}
            	
            	if(n<output2.size()-1)
            		rules += " and ";
            }
            predicates += AlloyConstructor.AlloyParagraph(predicateName, rules, AlloyConstructor.PRED);
    		predicates += AlloyConstructor.RunCheckCommand(predicateName, "10", "1", AlloyConstructor.PRED)+"\n\n";
        }
        
        return predicates;
	}
	
	public Classifier getSupertype() {
		return supertype;
	}
	
	public HashMap<Mediation, Classifier> getMediations() {
		return mediations;
	}
	
	public Relator getRelator() {
		return relator;
	}
	
	public void setRelator(Relator relator) throws Exception {
		/*ArrayList which will contain all the mediations of the relator and its supertypes*/
		ArrayList<Mediation> mediations = new ArrayList<Mediation>();
		
		/*List that will be used to identify the common supertype*/
		ArrayList<Classifier> mediatedTypesAllParents = new ArrayList<>();
		
		/*ArrayList which will contain the relator that was the foundation of the anti-pattern with all its supertypes*/
		ArrayList<Classifier> allRelators = new ArrayList<>();
		Relator aux;
		
		if(relator==null)
			throw new NullPointerException("The provided relator is null.");
		if(!(relator instanceof Relator))
			throw new Exception("The provided object is not a Relator.");	
		
		this.relator=relator;
		
		
		//add the input relator to the list
		allRelators.add(relator);
		//add all supertypes to the list
		allRelators.addAll(relator.allParents());
			
		/*TODO considers that all mediations are accordingly to the metamodel wrt its source and target. The mediation operation of the metamodel returns the second member end only.*/
		//creates the list that contains all the mediations of the input relator and its supertypes.
		for (Classifier rel : allRelators) {
			aux = (Relator)rel;
			mediations.addAll(aux.mediations());
		}
		
		//if there are not at least two mediations, the anti-pattern is not characterized
		if(mediations.size()<2)
			throw new Exception("The input relator does not have enough mediations to characterize the Anti-Pattern");
		
		this.mediations = new HashMap<>();
		
		//Adds all supertypes to the ArrayList
		for (Mediation med : mediations)
			mediatedTypesAllParents.addAll(med.mediated().allParents());
		
		for (Mediation med : mediations) {
			this.mediations.put(med, med.mediated());
			
			if(!ArrayListOperations.intersection(mediatedTypesAllParents, med.mediated().allParents()).isEmpty())
				mediatedTypesAllParents.retainAll(med.mediated().allParents());
		}
		
		if(mediatedTypesAllParents.isEmpty())
			throw new NullPointerException("There is no common supertype of the mediated entities");
		
		this.supertype = mediatedTypesAllParents.iterator().next();
		
	}
	
	
	@Override
	public String toString() {
		String result;
		
		result = "Relator: "+relator.getName()+"\n";
		result += "Supertype: "+supertype.getName()+"\n";
		
		for (Mediation med : mediations.keySet()) {
			result += mediations.get(med).getName() + " - " + med + "\n";
		}
		
		return result;
	}
	
}
