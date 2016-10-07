/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.editor.core;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import ru.swg.island.common.core.Const;
import ru.swg.island.common.core.object.LandscapeTile;
import ru.swg.island.common.core.object.Level;
import ru.swg.island.common.core.object.ObjectTile;
import ru.swg.island.common.core.object.Tile;
import ru.swg.island.common.core.object.UnitTile;
import ru.swg.island.common.io.IO;
import ru.swg.island.common.view.GuiLandscapeTile;
import ru.swg.island.common.view.GuiObjectTile;
import ru.swg.island.common.view.GuiTile;
import ru.swg.island.common.view.GuiUnitTile;
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
	private final JFrame self = this;
	
	private static final long serialVersionUID = 2068791030277854673L;
	
	private static final int SCREEN_WIDTH = 800;
	private static final int SCREEN_HEIGHT = 600;
	
	private final GameBoard gameBoard;
	
	/**
	 * Starter
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static final void main(final String[] args) 
			throws FileNotFoundException, IOException {
		Resources.init();
		new Editor(SCREEN_WIDTH, SCREEN_HEIGHT);
	}
	
	/**
	 * Constructor
	 * 
	 * @param width
	 * @param height
	 * @throws IOException
	 */
	private Editor(final int width, final int height) 
			throws IOException {
		
		// FIXME - calculate right sizes here
		gameBoard = new GameBoard(width, height);
    	final Component frameworkAdapter = new FrameworkAdapter(gameBoard, width, height);
    	setLayout(new BorderLayout());
		setTitle(Resources.getString("title.game.editor"));
		getContentPane().add(frameworkAdapter, BorderLayout.CENTER);
		getContentPane().add(new JScrollPane(getTilesPanel()), BorderLayout.NORTH);
		getContentPane().add(new JScrollPane(getSettingsPanel()), BorderLayout.WEST);
		getContentPane().setBackground(Color.BLACK);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setFocusable(true);
		setJMenuBar(getMenu());
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
		// mouse events listener
		final MouseAdapter mouseAdapter = new MouseAdapter(gameBoard);
		frameworkAdapter.addMouseListener(mouseAdapter);
		frameworkAdapter.addMouseMotionListener(mouseAdapter);
		frameworkAdapter.addMouseWheelListener(mouseAdapter);

		// keyboard events listener
		addKeyListener(new KeyAdapter());
	}
	
	/**
	 * Initialize menu
	 * 
	 * @return
	 */
	private final JMenuBar getMenu() {
		final JMenuBar menuBar = new JMenuBar();
		
		// File Menu
		final JMenu fileMenu = new JMenu(Resources.getString("str.file"));
		final JMenuItem newFile = new JMenuItem(Resources.getString("str.new"));
		newFile.addActionListener(new ActionListener() {
			@Override
			public final void actionPerformed(final ActionEvent e) {
				new LevelInfoPanel(new ObjectListener<Level>() {
					@Override
					public final void on(final Level newLevel) {
						final Level level = newLevel;
						try {
							gameBoard.loadLevel(level);
						} catch (final IOException e) {
							Log.error("Failed to load new level");
						}
					}
				});
			}
		});
		fileMenu.add(newFile);
		final JMenuItem openFile = new JMenuItem(Resources.getString("str.open"));
		openFile.addActionListener(new ActionListener() {
			@Override
			public final void actionPerformed(final ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File("./"));
				if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					try {
						final Level level = IO.loadLevel(fc.getSelectedFile().getAbsolutePath());
						gameBoard.loadLevel(level);
					} catch (final IOException err) { 
						Log.error(err.getMessage());
					}
				}
			}
		});
		fileMenu.add(openFile);
		final JMenuItem saveFile = new JMenuItem(Resources.getString("str.save"));
		saveFile.addActionListener(new ActionListener() {
			@Override
			public final void actionPerformed(final ActionEvent e) {
				try {
					final JFileChooser fc = new JFileChooser();
					fc.setCurrentDirectory(new File("./"));
					if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
						try {
							IO.saveLevel(fc.getSelectedFile().getAbsolutePath(), gameBoard.getLevel());
							JOptionPane.showMessageDialog(self, Resources.getString("str.level_saved"));
						} catch (final IOException err) {
							Log.error(err.getMessage());
						}
					}
					
					final String path = "./tiles/levels/" + gameBoard.getLevel().getId() + ".json";
					IO.saveLevel(path, gameBoard.getLevel());
					JOptionPane.showMessageDialog(self, Resources.getString("str.tile_saved"));
				} catch (final IOException err) { }
			}
		});
		fileMenu.add(saveFile);
		final JMenuItem settings = new JMenuItem(Resources.getString("str.settings"));
		fileMenu.add(settings);
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
	
	/**
	 * Tiles panel
	 * 
	 * @return
	 */
	private final JTabbedPane getTilesPanel() {
		final JTabbedPane panel = new JTabbedPane();
		
		final JPanel landscapePanel = new JPanel();
		landscapePanel.setLayout(new BoxLayout(landscapePanel, BoxLayout.X_AXIS));
		try {
			final List<LandscapeTile> landscapeTiles = IO.loadTiles("landscape", LandscapeTile.class);
			for (final LandscapeTile tile: landscapeTiles) {
				landscapePanel.add(makeButton(tile, Const.LANDSCAPE_TILE));
			}
		} catch (final IOException e) { }
		panel.addTab(Resources.getString("str.landscape_tile"), landscapePanel);
		
		final JPanel objectsPanel = new JPanel();
		objectsPanel.setLayout(new BoxLayout(objectsPanel, BoxLayout.X_AXIS));
		try {
			final List<ObjectTile> objectTiles = IO.loadTiles("objects", ObjectTile.class);
			for (final ObjectTile tile: objectTiles) {
				objectsPanel.add(makeButton(tile, Const.OBJECT_TILE));
			}
		} catch (final IOException e) { }
		panel.addTab(Resources.getString("str.object_tile"), objectsPanel);
		
		final JPanel unitsPanel = new JPanel();
		unitsPanel.setLayout(new BoxLayout(unitsPanel, BoxLayout.X_AXIS));
		try {
			final List<UnitTile> unitTiles = IO.loadTiles("units", UnitTile.class);
			for (final UnitTile tile: unitTiles) {
				unitsPanel.add(makeButton(tile, Const.UNIT_TILE));
			}
		} catch (final IOException e) { }
		panel.addTab(Resources.getString("str.unit_tile"), unitsPanel);
		
		return panel;
	}
	
	/**
	 * Make tile button
	 * 
	 * @param tile
	 * @return
	 * @throws IOException
	 */
	private final <T extends Tile> JButton makeButton(final T tile, final int type) 
			throws IOException {
		final Image image = Resources.loadImage(tile.getImage());
		final ImageIcon icon = new ImageIcon(image);
		final JButton button = new JButton(icon);
		button.addActionListener(new ActionListener() {
			@Override
			public final void actionPerformed(final ActionEvent e) {
				try {
					if (gameBoard != null) {
						GuiTile _tile = null;
						switch (type) {
						case Const.LANDSCAPE_TILE:
							_tile = new GuiLandscapeTile((LandscapeTile) tile);
							break;
						case Const.OBJECT_TILE:
							_tile = new GuiObjectTile((ObjectTile) tile);
							break;
						case Const.UNIT_TILE:
							_tile = new GuiUnitTile((UnitTile) tile);
							break;
						}
						gameBoard.setIntentTile(_tile);
					}
				} catch (final IOException err) { }
			}
		});
		button.setFocusable(false);
		return button;
	}
	
	/**
	 * Settings panel
	 * 
	 * @return
	 */
	private final JPanel getSettingsPanel() {
		final JPanel panel = new JPanel();
		panel.add(new JLabel(Resources.getString("str.tile_settings") + ":"));
		return panel;
	}
}
