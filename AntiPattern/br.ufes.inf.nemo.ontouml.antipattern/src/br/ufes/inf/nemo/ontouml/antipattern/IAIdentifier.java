package br.ufes.inf.nemo.ontouml.antipattern;

import java.util.Collection;

import org.eclipse.ocl.ParserException;
import br.ufes.inf.nemo.ontouml.antipattern.util.OCLQueryExecuter;

import RefOntoUML.Association;
import RefOntoUML.Model;

/*Imprecise Abstraction*/
public class IAIdentifier {
	
	public static String OCLQuery = "Association.allInstances()->select(x:Association | x.memberEnd.type->at(1).oclAsType(Classifier).allChildren()->size()>1 or x.memberEnd.type->at(2).oclAsType(Classifier).allChildren()->size()>1)";
	
	public static Collection<Association> RBOSQuery(Model m) throws ParserException 
	{
		return (Collection<Association>)OCLQueryExecuter.executeQuery(OCLQuery, m.eClass(), m);
	}
}
