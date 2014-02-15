package br.ufes.inf.nemo.assistant.wizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import br.ufes.inf.nemo.assistant.graph.GraphAssistant;
import br.ufes.inf.nemo.assistant.graph.NodeAssistant;
import br.ufes.inf.nemo.assistant.manager.ManagerPattern;
import br.ufes.inf.nemo.assistant.manager.PageProcessor;
import br.ufes.inf.nemo.assistant.util.StereotypeOntoUMLEnum;
import br.ufes.inf.nemo.assistant.wizard.pageassistant.NewGeneralizationSet;
import br.ufes.inf.nemo.assistant.wizard.pageassistant.Question;
import br.ufes.inf.nemo.assistant.wizard.pageassistant.WizardPageAssistant;

public class WizardAssitant extends Wizard {

	private GraphAssistant graph;

	public WizardAssitant(GraphAssistant graph) {
		super();
		setNeedsProgressMonitor(true);
		setHelpAvailable(true);

		this.graph = graph;
	}

	@Override
	public void addPages() {
		for(NodeAssistant node:graph.getNodeList()){
			if(!node.isAction())
				addPage(node.getPage());
		}
	}

	private boolean canFinish = false;
	@Override
	public boolean performFinish() {
		if(graph.getCurrentNode().isEndNode()){
			graph.getManagerPattern().run(graph.getCurrentNode().getPage());
		}
		return canFinish;
	}

	/**
	 * Used to prevent the back button perform
	 * */
	@Override
	public IWizardPage getPreviousPage(IWizardPage page) {
		return page;
	}

	@Override
	public IWizardPage getNextPage(IWizardPage currentPage) {
		NodeAssistant nextNode = null;
		WizardPageAssistant nextPage = null;

		if(graph.getCurrentNode().getPage().next()){
			nextNode = graph.getCurrentNode().getNextNode();
		}else if(graph.getCurrentNode().getPage().nextTrue()){
			nextNode = graph.getCurrentNode().getTrueNode();
		}else if(graph.getCurrentNode().getPage().nextFalse()){
			nextNode = graph.getCurrentNode().getFalseNode();
		}

		if(nextNode.isAction()){
			//The ActionNode treat is doing in getNextNode method
			nextNode = nextNode.getNextNode();
		}

		//Set the next page
		nextPage = nextNode.getPage();

		//Set currentNode
		graph.setCurrentNode(nextNode);

		//Special treats
		if(nextPage instanceof Question){
			PageProcessor.treatPage(nextNode, (Question)nextPage);
		}else if(nextPage instanceof NewGeneralizationSet){
			PageProcessor.treatPage(nextNode, (NewGeneralizationSet)nextPage);
		}

		//Execute all alterations in RefOntoUML object from the Manager Pattern 
		graph.getManagerPattern().run(currentPage);

		if(graph.getCurrentNode().isEndNode()){
			canFinish = true;
		}

		/*
		 * All variables are static fields in WizardPageAssistant class than,
		 * we do not need to get the current variables from the current page
		 * and set in the next page, we just need to override setVisible from
		 * the pages to update the for the current value.
		 * 
		 * Implicit actions:
		 * nextPage.setVariables(currentPage.getVariables());
		 * 
		 */

		return nextPage;
	} 

}