package br.ufes.inf.nemo.ontouml.transformation.v2.base;

import java.io.Writer;
import java.io.IOException;


public interface AlloySigRelationshipParent extends AlloyStaticRelationshipParent
{
	boolean targetIsReadOnly();
	public AlloyProperty getStaticProperty();
	public void writeTemporalCoExistenceConstraint(Writer out) throws IOException;
}
