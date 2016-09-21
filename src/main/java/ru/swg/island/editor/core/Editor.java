/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.editor.core;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ru.swg.wheelframework.io.Resources;

/**
 * Launcher
 */
public final class Editor extends JFrame {
	private static final long serialVersionUID = 2068791030277854673L;
	
	private static final int SCREEN_WIDTH = 100;
	private static final int SCREEN_HEIGHT = 150;
	
	public static final void main(final String[] args) 
			throws FileNotFoundException, IOException {
		Resources.init();
		new Editor(SCREEN_WIDTH, SCREEN_HEIGHT);
	}
	
	private Editor(final int width, final int height) {
    	final JFrame frame = new JFrame();
		frame.setSize(new Dimension(width, height));
		frame.setTitle(Resources.getString("title.game.editor"));
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setFocusable(true);
		
		final JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		
		final JButton btnTile = new JButton(Resources.getString("btn.editor.tiles"));
		btnTile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final TileBoard board = new TileBoard();
			}
		});
		panel.add(btnTile);
		
		frame.setVisible(true);
	}
}
