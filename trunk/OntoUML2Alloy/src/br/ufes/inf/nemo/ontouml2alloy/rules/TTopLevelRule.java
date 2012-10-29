package br.ufes.inf.nemo.ontouml2alloy.rules;

import java.util.ArrayList;
import java.util.HashMap;

import RefOntoUML.Classifier;
import br.ufes.inf.nemo.alloy.AlloyFactory;
import br.ufes.inf.nemo.alloy.BinaryOperation;
import br.ufes.inf.nemo.alloy.DisjointExpression;
import br.ufes.inf.nemo.common.parser.Parser;
import br.ufes.inf.nemo.ontouml2alloy.api.AlloyAPI;
import br.ufes.inf.nemo.ontouml2alloy.api.OntoUMLAPI;

public class TTopLevelRule {
	
	/**
	 *  Create Rule for Top Levels Classifiers in the model.
	 * 
	 *  Should be more of a description here....
	 */
	public static ArrayList<DisjointExpression> createTopLevelDisjointRules (Parser ontoparser, AlloyFactory factory, ArrayList<Classifier> toplevels)
	{
		HashMap< ArrayList<Classifier>, Integer > listsHashMap = new HashMap< ArrayList<Classifier>,Integer >();
		ArrayList<DisjointExpression> result = new ArrayList<DisjointExpression>();
		
		for (Classifier c1: toplevels)
		{			
			ArrayList<Classifier> descendants1 = new ArrayList<Classifier>();
			OntoUMLAPI.getDescendants(ontoparser, descendants1, c1);

			// creates a single List containing the topLevel classifier 'c1' 
			// and the top levels classifiers that have their descendants overlapping 
			// with the descendants of the classifier 'c1'
			
			ArrayList<Classifier> singleList = new ArrayList<Classifier>();
			singleList.add(c1);
			
			for (Classifier c2: toplevels)
			{
				if (!c2.equals(c1)) 
				{
					ArrayList<Classifier> descendants2 = new ArrayList<Classifier>();
					OntoUMLAPI.getDescendants(ontoparser, descendants2, c2);
										
					Classifier overlap = OntoUMLAPI.isOverlapping(descendants1, descendants2);
					if (overlap == null) singleList.add(c2);						
				}
			}
						
			listsHashMap.put(singleList,0);
		}
				
		// create a union (+) String expression for every singleList 
		// starting at the second element
				
		for (ArrayList<Classifier> singleList : listsHashMap.keySet())
		{
			if (listsHashMap.get(singleList)==0)
			{
				// create a list of the elements starting at the second element
				int count = 0;
				ArrayList<String> exprList = new ArrayList<String>();
				for(Classifier c: singleList) 
				{
					if (count>=1) exprList.add(ontoparser.getName(c));					
					count++;
				}
				
				ArrayList<String> paramList = new ArrayList<String>();
				paramList.add(ontoparser.getName(singleList.get(0)));
				
				if (exprList.size()>1) 
				{
					// create a union(+) operation for the exprList
					BinaryOperation bo = AlloyAPI.createUnionExpression(factory, exprList);
					paramList.add(bo.toString());					
				}else{
					paramList.add(ontoparser.getName(singleList.get(1)));					
				}						
				
				//add Top Level Disjoint Expression Rule to to the List
				result.add( AlloyAPI.createDisjointExpression(factory,paramList) );				
			}			
		}
		return result;
	}
}
