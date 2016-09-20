/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.editor.core;

import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;

import ru.swg.wheelframework.io.Resources;

/**
 * Launcher
 */
public final class Editor extends JFrame {
	private static final long serialVersionUID = 2068791030277854673L;
	
	private static final int SCREEN_WIDTH = 400;
	private static final int SCREEN_HEIGHT = 300;
	
	public static final void main(final String[] args) 
			throws FileNotFoundException, IOException {
		Resources.init();
		new Editor(SCREEN_WIDTH, SCREEN_HEIGHT);
	}
	
	private Editor(final int width, final int height) {
    	JFrame frame = new JFrame();
		frame.setSize(new Dimension(width, height));
		frame.setTitle(Resources.getString("title.game.editor"));
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setFocusable(true);
		frame.setVisible(true);
	}
}
