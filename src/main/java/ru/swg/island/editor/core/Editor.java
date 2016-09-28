/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.editor.core;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
import ru.swg.island.editor.landscape.LandscapeTileBoard;
import ru.swg.island.editor.unit.UnitTileBoard;
import ru.swg.wheelframework.event.listener.ObjectListener;
import ru.swg.wheelframework.io.KeyAdapter;
import ru.swg.wheelframework.io.MouseAdapter;
import ru.swg.wheelframework.io.Resources;
import ru.swg.wheelframework.log.Log;
import ru.swg.wheelframework.view.DisplayObject;
import ru.swg.wheelframework.view.FrameworkAdapter;

/**
 * Launcher
 */
public final class Editor extends JFrame {
	private static final long serialVersionUID = 2068791030277854673L;
	
	private static final int SCREEN_WIDTH = 400;
	private static final int SCREEN_HEIGHT = 300;
	
	private final GameBoard gameBoard;
	private Image dndImage;
	private int dndImageX, dndImageY;
	
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
		gameBoard = new GameBoard(width, height - 40);
    	final Component frameworkAdapter = new FrameworkAdapter(gameBoard, width, height - 40);
    	setLayout(new BorderLayout());
		setSize(new Dimension(width, height));
		setTitle(Resources.getString("title.game.editor"));
		getContentPane().add(frameworkAdapter, BorderLayout.CENTER);
		getContentPane().add(new JScrollPane(getTilesPanel()), BorderLayout.EAST);
		getContentPane().add(new JScrollPane(getSettingsPanel()), BorderLayout.WEST);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setFocusable(true);
		setJMenuBar(getMenu());
		setVisible(true);
		
		// mouse events listener
		final MouseAdapterExt mouseAdapter = new MouseAdapterExt(gameBoard);
		frameworkAdapter.addMouseListener(mouseAdapter);
		frameworkAdapter.addMouseMotionListener(mouseAdapter);
		frameworkAdapter.addMouseWheelListener(mouseAdapter);

		// keyboard events listener
		addKeyListener(new KeyAdatperExt());
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
	
	/**
	 * Tiles panel
	 * 
	 * @return
	 */
	private final JTabbedPane getTilesPanel() {
		final JTabbedPane panel = new JTabbedPane();
		
		final JPanel landscapePanel = new JPanel();
		landscapePanel.setLayout(new BoxLayout(landscapePanel, BoxLayout.Y_AXIS));
		try {
			final List<LandscapeTile> landscapeTiles = IO.loadTiles("landscape", LandscapeTile.class);
			for (final LandscapeTile tile: landscapeTiles) {
				landscapePanel.add(makeButton(tile, Const.LANDSCAPE_TILE));
			}
		} catch (final IOException e) { }
		panel.addTab(Resources.getString("str.landscape_tile"), landscapePanel);
		
		final JPanel objectsPanel = new JPanel();
		objectsPanel.setLayout(new BoxLayout(objectsPanel, BoxLayout.Y_AXIS));
		try {
			final List<ObjectTile> objectTiles = IO.loadTiles("objects", ObjectTile.class);
			for (final ObjectTile tile: objectTiles) {
				landscapePanel.add(makeButton(tile, Const.OBJECT_TILE));
			}
		} catch (final IOException e) { }
		panel.addTab(Resources.getString("str.object_tile"), objectsPanel);
		
		final JPanel unitsPanel = new JPanel();
		unitsPanel.setLayout(new BoxLayout(unitsPanel, BoxLayout.Y_AXIS));
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
				dndImage = image;
				repaint();
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
	
	@Override
	public final void paint(final Graphics graphics) {
		super.paint(graphics);
		
		final Graphics2D g2d = (Graphics2D) graphics;
		g2d.drawImage(dndImage, dndImageX, dndImageY, null);
		Log.info("dndImage is null? " + dndImage);
	}

	// extended class
	private final class MouseAdapterExt extends MouseAdapter {
		public MouseAdapterExt(final DisplayObject target) {
			super(target);
		}
		
		@Override
		public final void mouseMoved(final java.awt.event.MouseEvent e) {
			super.mouseMoved(e);
		
			if (dndImage != null) {
				dndImageX = e.getX();
				dndImageY = e.getY();
			}
		}
		
		@Override
		public final void mouseClicked(final java.awt.event.MouseEvent e) {
			super.mouseClicked(e);
			
			if (e.getButton() == MouseEvent.BUTTON2) {
				dndImage = null;
				dndImageX = 0;
				dndImageY = 0;
			}
		}
	}
	
	private final class KeyAdatperExt extends KeyAdapter {
		@Override
		public final void keyTyped(final java.awt.event.KeyEvent e) {
			super.keyTyped(e);

			if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
				dndImage = null;
				dndImageX = 0;
				dndImageY = 0;				
			}
		}
	}
}
