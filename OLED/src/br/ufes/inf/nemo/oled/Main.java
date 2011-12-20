/**
 * Copyright 2011 NEMO (http://nemo.inf.ufes.br/en)
 *
 * This file is part of OLED (OntoUML Lightweight Editor).
 * OLED is based on TinyUML and so is distributed under the same
 * licence terms.
 *
 * OLED is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * OLED is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OLED; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package br.ufes.inf.nemo.oled;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import br.ufes.inf.nemo.oled.ui.AppFrame;

/*
 * TODO
 * 
 * Implementar associa��o entre rela��es
 * Implantar o reposicionamento de labels
 * Permitir auto-relacionamentos
 * Funcionalidade Copiar/Colar
 * Tela Sobre (About) 
 * 
 * Before release
 * 
 * Revis�o Geral do C�dgio - Limpeza
 * Revis�o Geral do C�dgio - Documenta��o
 * Revis�o Geral arquivo de captions
 * 
 * Sem previs�o
 * 
 * Localization
 * 
 * 
 *  ISSUES
 *  
 * - Aberto : Classes n�o est�o redimensionando bem
 * 			: S� est� acontecendo quando a op��o snap to grid est� habilitada
 * - Aberto : Ordena��o dos atributos nas propriedades da classe n�o � respeitada  
 * - Aberto : Incluir os atributos das rela��es meronimicas na label da rela��o
 * - Aberto : Tratar os eventos de mudan�a nas abas do editor para atualizar o redo/undo
 * - Aberto : Habilitar o clique com shift para selecionar ou desselecionar o elemento
 * - Aberto : Tratar a simula��o quando a gera��o de alloy retorna vazia
 * 
 * 
 * - OK : Setas do output pane n�o est�o funcionando
 * - OK : Erro ao transferir dados na janela de edi��o de classe (invalid cast) 
 * - OK : Erro na gera��o de alloy (Bernardo) quando uma category (mixin) � supertipo
 * - OK : Generalizar idioma da janela de generaliza��o
 * - OK : Implementar a op��o abstract na janela de classe
 * - OK : Implantar novo sistema de serializa��o/desserializa��o baseado em Zip
 * - OK : Remover {disjoint, complete} da label das generalizations
 * - OK : Renomear itens de menu para open project e save project
 * - OK : Redimensionar tamanho inicial da janela principal para acomodar resol��es 1024x768
 * - OK : Undo/Redo para a��es visuais como redimensionamento n�o est�o funcionando bem
 * - Aberto para rela��es Ver linehandler : Ao fazer Undo/Redo algumas a��es n�o tem efeito aparente - Isso acontece pois o clique est� sendo tratado como um comando de mover / redimensionar com nenhuma modifica��o.
 * 		: Como saber se o usu�rio est� movendo, redimensionando ou selecionando os nodes? O SelectionHandler informar� isso.
 * 		: Selecionando = node selecionado ou n�o posicao do click release igual posi��o inicial
 * 		: Movendo = node previamente selecionado, posicao do click release diferente da posi��o inicial
 * 		: Redimensionando = node previamente selecionado...
 * 
 */


/**
 * This is the start class of the OLED application. OLED is based on the TinyUML project by Wei-ju Wu.
 */
public final class Main {

	/**
	 * Private constructor.
	 */
	private Main() { }

	/**
	 * The start method for this application.
	 * @param args the command line parameters
	 */
	public static void main(String[] args) {

		// Mac OS X settings
		if (System.getProperty("mrj.version") != null) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.eawt.CocoaComponent.CompatibilityMode",
			"false");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name",
			"OLED");
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * {@inheritDoc}
			 */
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					UIManager.put("TabbedPane.focus", new Color(0, 0, 0, 0));
					JFrame frame = new AppFrame();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}
}
