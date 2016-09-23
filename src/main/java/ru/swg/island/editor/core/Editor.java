/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.editor.core;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import ru.swg.island.common.core.object.Level;
import ru.swg.island.editor.landscape.LandscapeTileBoard;
import ru.swg.island.editor.unit.UnitTileBoard;
import ru.swg.wheelframework.event.listener.ObjectListener;
import ru.swg.wheelframework.io.Resources;
import ru.swg.wheelframework.log.Log;

/**
 * Launcher
 */
public final class Editor extends JFrame {
	private static final long serialVersionUID = 2068791030277854673L;
	
	private static final int SCREEN_WIDTH = 400;
	private static final int SCREEN_HEIGHT = 300;
	
	private Level level;
	
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
		frame.getContentPane().add(getBoard());
		frame.setJMenuBar(getMenu());
		frame.setVisible(true);
	}
	
	private final JMenuBar getMenu() {
		final JMenuBar menuBar = new JMenuBar();
		
		// File Menu
		final JMenu fileMenu = new JMenu(Resources.getString("str.file"));
		final JMenuItem newFile = new JMenuItem(Resources.getString("str.new"));
		newFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new LevelInfoPanel(new ObjectListener<Level>() {
					@Override
					public void on(Level newLevel) {
						level = newLevel;
						Log.info(level.getId());
						Log.info(level.getName());
						Log.info(level.getDescription());
						Log.info(level.getWidth() + "");
						Log.info(level.getHeight() + "");
					}
				});
			}
		});
		fileMenu.add(newFile);
		final JMenuItem openFile = new JMenuItem(Resources.getString("str.open"));
		fileMenu.add(openFile);
		final JMenuItem saveFile = new JMenuItem(Resources.getString("str.save"));
		fileMenu.add(saveFile);
		menuBar.add(fileMenu);
		
		// Tile menu
		final JMenu tileMenu = new JMenu(Resources.getString("str.tile"));
		final JMenuItem landscapeTileMenuItem = new JMenuItem(Resources.getString("str.landscape_tile"));
		landscapeTileMenuItem.addActionListener(new ActionListener() {
			@Override
			public final void actionPerformed(final ActionEvent e) {
				new LandscapeTileBoard();
			}
		});
		tileMenu.add(landscapeTileMenuItem);
		final JMenuItem unitTileMenuItem = new JMenuItem(Resources.getString("str.unit_tile"));
		unitTileMenuItem.addActionListener(new ActionListener() {
			@Override
			public final void actionPerformed(final ActionEvent e) {
				new UnitTileBoard();
			}
		});
		tileMenu.add(unitTileMenuItem);
		menuBar.add(tileMenu);
		
		return menuBar;
	}
	
	private final JPanel getBoard() {
		final JPanel panel = new JPanel();
		
		return panel;
	}
}
