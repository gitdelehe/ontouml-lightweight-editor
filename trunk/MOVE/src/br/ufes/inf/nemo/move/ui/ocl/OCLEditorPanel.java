package br.ufes.inf.nemo.move.ui.ocl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.TemplateCompletion;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import br.ufes.inf.nemo.move.ui.TheFrame;
import br.ufes.inf.nemo.move.utilities.ColorPalette;
import br.ufes.inf.nemo.move.utilities.ColorPalette.ThemeColor;

/**
 * @author John Guerson
 */

public class OCLEditorPanel extends JPanel {

	private static final long serialVersionUID = 1277358682337723759L;
	
	public TheFrame frame;
	public RSyntaxTextArea textArea;
	public OCLTokenMaker tokenMaker;
	public DefaultCompletionProvider provider;
	public AutoCompletion ac;
	public RTextScrollPane scrollPane;
	
	public void setParent (TheFrame frame)
	{
		this.frame = frame;
	}
	
	public void setText(String text)
	{
		textArea.setText(text);
	}
	
	public void getText()
	{
		textArea.getText();
	}
	
	/**
    * Set the font for all token types.
    * 
    * @param textArea
    *           The text area to modify.
    * @param font
    *           The font to use.
    */
   public void setFont(RSyntaxTextArea textArea, Font font) {
      if (font != null) {
         SyntaxScheme ss = textArea.getSyntaxScheme();
         ss = (SyntaxScheme) ss.clone();
         for (int i = 0; i < ss.getStyleCount(); i++) {
            if (ss.getStyle(i) != null) {
               ss.getStyle(i).font = font;
            }
         }
         textArea.setSyntaxScheme(ss);
         textArea.setFont(font);
      }
   }
	   
   /**
    * Constructor
    * 
    * @param frame
    */
   public OCLEditorPanel(TheFrame frame)
   {
	   this();
	   
	   this.frame = frame;
   }
   
   /**
    * Set Eclipse Theme on text area.
    * 
    * @param textArea
    * @param xmlPath
    */
   public void setTheme(RSyntaxTextArea textArea, String xmlPath)
   {
	   Theme theme;
		try {
			theme = Theme.load(getClass().getResourceAsStream(xmlPath));
			theme.apply(textArea);
		} catch (IOException e) {
			e.printStackTrace();
		}   
   }
   
	/**
	 * Constructor.
	 */
	public OCLEditorPanel ()
	{
		textArea = new RSyntaxTextArea(5, 30);		
	    textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
	    textArea.setAntiAliasingEnabled(true);
	    textArea.setCodeFoldingEnabled(false);
		textArea.setForeground(Color.BLACK);
		textArea.setBackground(new Color(255, 255, 255));				
		textArea.setCurrentLineHighlightColor(ColorPalette.getInstance().getColor(ThemeColor.GREEN_LIGHTEST));		
		setFont(textArea,new Font("Consolas", Font.PLAIN, 11));		
		setTheme(textArea,"/br/ufes/inf/nemo/move/ui/ocl/eclipse.xml");
			         
		tokenMaker = new OCLTokenMaker();	    
	    ((RSyntaxDocument)textArea.getDocument()).setSyntaxStyle(tokenMaker);
	       
	    provider = new DefaultCompletionProvider();
	    provider.addCompletion(new TemplateCompletion(provider, "for", "for-loop", "for (int ${i} = 0s"));

	    ac = new AutoCompletion(provider);      	
	    ac.install(textArea);
	    ac.setAutoActivationEnabled(true);
      	ac.setShowDescWindow(true);      	
      	setLayout(new BorderLayout(0, 0));
      	
      	scrollPane = new RTextScrollPane(textArea);
      	scrollPane.getGutter().setLineNumberColor(Color.GRAY);
      	scrollPane.getTextArea().setRows(5);
      	scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
      	
      	setPreferredSize(new Dimension(400, 100));
      	add(scrollPane); 
	}
	    
	public class OCLCompletionProvider extends DefaultCompletionProvider
	{		
		@Override
		protected boolean isValidChar(char ch) 
		{
			return super.isValidChar(ch) || ch=='.' || ch==':'|| ch=='-';
		}		
	}
			
    /**
     * Create a simple provider that adds some related completions.
     * 
     * @return The completion provider.
     */
    //private void addOCLCompletions() 
    //{  
       //provider.addCompletion(new ShorthandCompletion(provider, "Context","context type\ninv : true","context declaration"));
      // provider.addCompletion(new ShorthandCompletion(provider, "Property Context","context type::property : propertyType\nderive: null","derive property context declaration"));
       
       //provider.addCompletion(new ShorthandCompletion(provider, "oclIsKindOf","oclIsKindOf(","Evaluates to true if the type of self conforms to t. That is, self is of type t or a subtype of t."));
       
      // String template =
    	//	   "for (int ${i} = 0; ${i} &lt; ${array}.length; ${i}++) {${cursor}";
    			   //
       //provider.addCompletion(new TemplateCompletion(provider, "for", "for-loop", "for (int ${i} = 0; ${i} &lt; ${array}.length; ${i}++) {${cursor}"));
    		   
       /*
       provider.addCompletion(new ShorthandCompletion(provider, "."));
       provider.addCompletion(new ShorthandCompletion(provider, "->"));
       provider.addCompletion(new ShorthandCompletion(provider, "::"));
       
       provider.addCompletion(new ShorthandCompletion(provider, "=","True if self is the same object as object2. Infix operator."));
       provider.addCompletion(new ShorthandCompletion(provider, "<>","True if self is a different object from object2. Infix operator."));
       
       provider.addCompletion(new BasicCompletion(provider, "or","True if either self or b is true."));
       provider.addCompletion(new BasicCompletion(provider, "and","True if both b1 and b are true."));
       provider.addCompletion(new BasicCompletion(provider, "not","True if self is false."));
       provider.addCompletion(new BasicCompletion(provider, "implies","True if self is false, or if self is true and b is true."));
       provider.addCompletion(new BasicCompletion(provider, "xor","True if either self or b is true, but not both."));
       
       //
       provider.addCompletion(new BasicCompletion(provider, "oclIsTypeOf","Evaluates to true if self is of the type t but not a subtype of t"));
       provider.addCompletion(new BasicCompletion(provider, "oclIsUndefined()","Evaluates to true if the self is equal to invalid or equal to null."));
       
       provider.addCompletion(new BasicCompletion(provider, "<","True if self is less than i."));
       provider.addCompletion(new BasicCompletion(provider, ">","True if self is greater than i."));
       provider.addCompletion(new BasicCompletion(provider, "<=","True if self is less than or equal to i."));
       provider.addCompletion(new BasicCompletion(provider, ">=","True if self is greater than or equal to i."));
       
       provider.addCompletion(new BasicCompletion(provider, "-","The negative value of self."));
       provider.addCompletion(new BasicCompletion(provider, "-","The value of the subtraction of i from self."));
       provider.addCompletion(new BasicCompletion(provider, "+","The value of the addition of self and i."));
       provider.addCompletion(new BasicCompletion(provider, "max","The maximum of self an i."));
       provider.addCompletion(new BasicCompletion(provider, "min","The minimum of self an i."));
       provider.addCompletion(new BasicCompletion(provider, "abs()","The absolute value of self."));
       
       provider.addCompletion(new BasicCompletion(provider, "size()","The number of elements in the collection self."));
       
       provider.addCompletion(new BasicCompletion(provider, "includesAll","Does self contain all the elements of c2 ?"));
       provider.addCompletion(new BasicCompletion(provider, "excludesAll","Does self contain none of the elements of c2 ?"));
       
       provider.addCompletion(new BasicCompletion(provider, "includes","True if object is an element of self, false otherwise."));
       provider.addCompletion(new BasicCompletion(provider, "excludes","True if object is not an element of self, false otherwise."));
       
       provider.addCompletion(new BasicCompletion(provider, "isEmpty()","Is self the empty collection?"));
       provider.addCompletion(new BasicCompletion(provider, "notEmpty()","Is self not the empty collection?"));
       
       provider.addCompletion(new BasicCompletion(provider, "asSet()","The Set containing all the elements from self, with duplicates removed."));
       
       provider.addCompletion(new BasicCompletion(provider, "union","The union of self and s."));
       provider.addCompletion(new BasicCompletion(provider, "intersection","The intersection of self and s (i.e., the set of all elements that are in both self and s)."));       
       provider.addCompletion(new BasicCompletion(provider, "-","The elements of self, which are not in s."));
       
       provider.addCompletion(new BasicCompletion(provider, "including","The set containing all elements of self plus object."));
       provider.addCompletion(new BasicCompletion(provider, "excluding","The set containing all elements of self without object."));
       provider.addCompletion(new BasicCompletion(provider, "symmetricDifference","The sets containing all the elements that are in self or s, but not in both."));
              
       provider.addCompletion(new BasicCompletion(provider, "allInstances()"));
       
       provider.addCompletion(new BasicCompletion(provider, "exists"));
       provider.addCompletion(new BasicCompletion(provider, "forAll"));
       provider.addCompletion(new BasicCompletion(provider, "one"));
       provider.addCompletion(new BasicCompletion(provider, "select"));
       provider.addCompletion(new BasicCompletion(provider, "reject"));
       provider.addCompletion(new BasicCompletion(provider, "isUnique"));
       provider.addCompletion(new BasicCompletion(provider, "collect"));
       
       provider.addCompletion(new BasicCompletion(provider, "let x = in "));
       
       provider.addCompletion(new BasicCompletion(provider, "if then else "));
       */       
  //  }

}
