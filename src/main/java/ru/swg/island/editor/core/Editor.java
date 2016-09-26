/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.editor.core;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import ru.swg.island.common.core.object.Level;
import ru.swg.island.editor.landscape.LandscapeTileBoard;
import ru.swg.island.editor.unit.UnitTileBoard;
import ru.swg.wheelframework.event.listener.ObjectListener;
import ru.swg.wheelframework.io.KeyAdapter;
import ru.swg.wheelframework.io.MouseAdapter;
import ru.swg.wheelframework.io.Resources;
import ru.swg.wheelframework.log.Log;
import ru.swg.wheelframework.view.FrameworkAdapter;

/**
 * Launcher
 */
public final class Editor extends JFrame {
	private static final long serialVersionUID = 2068791030277854673L;
	
	private static final int SCREEN_WIDTH = 400;
	private static final int SCREEN_HEIGHT = 300;
	
	private final GameBoard gameBoard;
	
	public static final void main(final String[] args) 
			throws FileNotFoundException, IOException {
		Resources.init();
		new Editor(SCREEN_WIDTH, SCREEN_HEIGHT);
	}
	
	private Editor(final int width, final int height) 
			throws IOException {
		
		// FIXME - calculate right sizes here
		gameBoard = new GameBoard(width, height - 40);
    	final Component frameworkAdapter = new FrameworkAdapter(gameBoard, width, height - 40);
		setSize(new Dimension(width, height));
		setTitle(Resources.getString("title.game.editor"));
		getContentPane().add(frameworkAdapter);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setFocusable(true);
		setJMenuBar(getMenu());
		setVisible(true);
		
		// mouse events listener
		final MouseAdapter mouseAdapter = new MouseAdapter(gameBoard);
		frameworkAdapter.addMouseListener(mouseAdapter);
		frameworkAdapter.addMouseMotionListener(mouseAdapter);
		frameworkAdapter.addMouseWheelListener(mouseAdapter);

		// keyboard events listener
		addKeyListener(new KeyAdapter());
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
					public void on(final Level newLevel) {
						final Level level = newLevel;
						try {
							gameBoard.loadLevel(level);
						} catch (IOException e) {
							Log.error("Failed to load new level");
						}
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
}
