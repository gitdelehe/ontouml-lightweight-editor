package br.ufes.inf.nemo.oled;

import javax.swing.JTabbedPane;

import br.ufes.inf.nemo.oled.ui.DiagramEditorCommandDispatcher;
import br.ufes.inf.nemo.oled.ui.Palette;
import br.ufes.inf.nemo.oled.ui.PaletteAccordion;

public class ToolManager extends JTabbedPane {

	private static final long serialVersionUID = 1752050268631906319L;
	@SuppressWarnings("unused")
	private AppFrame frame;
	private DiagramEditorCommandDispatcher editorDispatcher;
	private PaletteAccordion palettes;	

	public ToolManager(AppFrame frame, DiagramEditorCommandDispatcher editorDispatcher)
	{
		super();
		
		this.frame = frame;
		this.editorDispatcher = editorDispatcher;
		
		setFocusable(false);
				
		palettes = new PaletteAccordion(frame);
		palettes.createStaticStructurePalettes(editorDispatcher);
		//Assistent assistent = new Assistent();
		//Assistent patternsPanel = new Assistent();
				
		addTab("Toolbox", palettes); //TODO Localize these		
		//this.addTab("Assistent", assistent);
		//this.addTab("Patterns", patternsPanel);
	}
	
	public DiagramEditorCommandDispatcher getEditorDispatcher() {
		return editorDispatcher;
	}
	
	public Palette getOpenPalette() {
		return palettes.getOpenPalette();
	}

	public PaletteAccordion getPalleteAccordion()
	{
		return palettes;
	}


}
