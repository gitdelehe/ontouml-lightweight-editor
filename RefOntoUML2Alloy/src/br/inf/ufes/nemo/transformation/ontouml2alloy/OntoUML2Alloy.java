package br.inf.ufes.nemo.transformation.ontouml2alloy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import RefOntoUML.Model;
import RefOntoUML.impl.RefOntoUMLPackageImpl;

//TODO: documentar!
/*
 * Erros detectados no editor
 * Category pode herdar de tipos n�o rigidos
 * Tipos n�o rigidos podem herdar de category
 * Um role pode herdar de um substance sortal e de um relator ao mesmo tempo (deveria poder s� de um ultimate id provider ou subkind por vez, diretamente ou indiretamente)
 * Generalization sets permitem ter mais de um general (n�o tenho certeza, mas acho que n�o deviam ter)
 * Modes podem ter mais de uma characterization
 * 
 * DONE:
 * Phases 
 * Generalization sets 
 * datatype 
 * Meronymic relationships  
 * heran�a de rela��es 
 * 
 * TODOLIST:
 * isExtensional dos collectives (como fazer? tem que haver restri��es nas rela��es memberOf dele... tem que descobrir quais rela��es ele pertence)
 * qualidades das rela��es todo parte diferentes (transitividade, etc)
 * verificar se as restri��es de cardinalidade para uma rela��o generalizada � impressa com o nome da rela��o general
 * Formal relations: Para sigclasses pode ser rela��o bin�ria... Para stateclasses tern�ria
 * Implementar as gs das sigclasses como um fact separado, para otimizar. (por enquanto t� como sigfact de State)
 * 
 * Ideias:
 * Eliminar a classe Phase, tirar a distribui��o de phases e fazer a transforma��o diretamente pelos gs.
 * (Datatype assosciations frozen & rela��es todo parte immutaveis) para sigclasses podem ser membros da signature
 */

/**
 * Class responsable for tranforming a OntoUML model in Alloy, based on the RefOntoUML metamodel.
 * Currently two transformation approches are supported: 
 * 	V2: Considers a linear timeline, with only one possible world
 *  V3: Considers the existence of more the one posible world
 *  	  
 *  @author Bernardo Braga
 *  @author Antognoni Albuquerque
 *  @version 0.1
 */
public class OntoUML2Alloy {

	/**
	 * The transformation type
	 */
	public enum TransformationType { V2, V3 }
	
	public static void main(String[] args)
	{
		// Create a resource set to hold the resources.
		ResourceSet resourceSet = new ResourceSetImpl();
		
		// Register the appropriate resource factory to handle all file extensions.
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(RefOntoUML.RefOntoUMLPackage.eNS_URI,RefOntoUML.RefOntoUMLPackage.eINSTANCE);
		RefOntoUMLPackageImpl.init();
		
		File sourceFile = new File("C:/conversao/default.ecore");
		FileWriter out = null;

		try {
			
			Resource resource = resourceSet.getResource(URI.createFileURI(sourceFile.getAbsolutePath()), true);
			out = new FileWriter(new File("C:/conversao/default.als"));
			
			String alloy = transformToAlloyString(resource, TransformationType.V2);
			out.write(alloy);
			
		} catch (RuntimeException exception) {
			exception.printStackTrace();
		} catch (IOException exception) {
			exception.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}
	
	/**
	 * Transforms a OntoUML model contained in a {@link Resource} in Alloy.
	 * All the types in the resource should be contained in a top-level {@link Model} package.
	 * 
	 * @param resource the resource which holds the model
	 * @param type the transformation type
	 * @return the alloy transformation
	 */
	public static String transformToAlloyString(Resource resource, TransformationType type)
	{
		Model model = (Model) resource.getContents().get(0);
		
		return transformToAlloyString(model, type);
	}
	
	/**
	 * Transforms a OntoUML model in Alloy.
	 * 
	 * @param model the model to be transformed
	 * @param type the transformation type
	 * @return the alloy transformation
	 */
	public static String transformToAlloyString(Model model, TransformationType type)
	{
		AlloyProcessor processor;
		
		if(type == TransformationType.V2){
			processor = new br.inf.ufes.nemo.transformation.ontouml2alloy.v2.Processor();
			
		}
		else {
			processor = new br.inf.ufes.nemo.transformation.ontouml2alloy.v3.Processor();
		}
		
		return processor.process(model);
	}
	
	/**
	 * Transforms a OntoUML model in Alloy.
	 * 
	 * @param model the model to be transformed
	 * @param type the transformation type
	 * @param fileName the targetFile
	 * @return the alloy transformation
	 */
	public static boolean transformToAlloyFile(Model model, TransformationType type, String fileName)
	{
		String alloy = null;
		FileWriter writer = null;
		AlloyProcessor processor;
		
		if(type == TransformationType.V2){
			processor = new br.inf.ufes.nemo.transformation.ontouml2alloy.v2.Processor();
			
		}
		else {
			processor = new br.inf.ufes.nemo.transformation.ontouml2alloy.v3.Processor();
		}
		
		alloy = processor.process(model);
		
		try {
			if(alloy != null && alloy.equals("") == false)
			{
				writer = new FileWriter(fileName);
				writer.write(alloy);
				writer.flush();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			try {
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
}
