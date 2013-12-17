package br.ufes.inf.nemo.antipattern.binover;

import java.util.ArrayList;

import RefOntoUML.Association;
import RefOntoUML.Package;
import br.ufes.inf.nemo.antipattern.AntiPatternIdentifier;
import br.ufes.inf.nemo.antipattern.Antipattern;
import br.ufes.inf.nemo.antipattern.AntipatternInfo;
import br.ufes.inf.nemo.common.ontoumlparser.OntoUMLParser;

public class BinOverVariation1Antipattern extends
		Antipattern<BinOverVariation1Occurrence> {

	public BinOverVariation1Antipattern(OntoUMLParser parser) throws NullPointerException {
		super(parser);
	}

	public BinOverVariation1Antipattern(Package pack) throws NullPointerException {
		this( new OntoUMLParser(pack));
	}

	private static final String oclQuery = "Association.allInstances()->select(x:Association | x.memberEnd.type->forAll(y1,y2:Type | y1=y2))";
	
	private static final AntipatternInfo info = new AntipatternInfo("Binary Relation With Overlapping Ends - Variation 1", 
			"BinOver-Var1", 
			"This anti-pattern occurs when...",
			oclQuery); 
		
	public static AntipatternInfo getAntipatternInfo(){
		return info;
	}
	
	public ArrayList<BinOverVariation1Occurrence> identify() {
		ArrayList<Association> query_result;
		
		query_result = AntiPatternIdentifier.runOCLQuery(parser, oclQuery, Association.class);
		
		for (Association assoc : query_result) 
		{
			try {
				super.occurrence.add(new BinOverVariation1Occurrence(assoc, super.parser));
			} catch (Exception e) {
				System.out.println(info.getAcronym()+": Could not create occurrence!");
			}
		}
		
		return this.getOccurrences();
	}

}