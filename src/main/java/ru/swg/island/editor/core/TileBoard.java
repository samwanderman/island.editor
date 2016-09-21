/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.editor.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ru.swg.island.common.core.object.Tile;
import ru.swg.wheelframework.io.Resources;
import ru.swg.wheelframework.log.Log;

/**
 * Tile board
 */
public final class TileBoard extends JFrame {
	private static final long serialVersionUID = 7133632713919859075L;
	
	private final int width = 400;
	private final int height = 300;
	
	private Tile tile;
	
	public TileBoard() {
		setSize(new Dimension(width, height));
		setTitle(Resources.getString("title.game.editor.tile"));
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setFocusable(true);
		
		final JMenuBar menuBar = new JMenuBar();
		final JMenu menu = new JMenu(Resources.getString("str.file"));
		final JMenuItem menuItemNew = new JMenuItem(Resources.getString("str.new")), 
					menuItemOpen = new JMenuItem(Resources.getString("str.open")), 
					menuItemSave = new JMenuItem(Resources.getString("str.save"));
		menuItemNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Log.info("new");
			}
		});
		menu.add(menuItemNew);
		
		menuItemOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Log.info("open");
			}
		});
		menu.add(menuItemOpen);
		
		menuItemSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Log.info("save");
			}
		});
		menu.add(menuItemSave);
		menuBar.add(menu);
		setJMenuBar(menuBar);
		
		// Set main panel
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		// Set controls panel
		final JPanel controlsPanel = new JPanel();
		controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
		controlsPanel.setPreferredSize(new Dimension(width / 2, height));
		// id
		controlsPanel.add(new JLabel(Resources.getString("str.id") + ":"));
		final JTextField idField = new JTextField();
		idField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tile.setId(idField.getText());
			}
		});
		controlsPanel.add(idField);
		// name
		controlsPanel.add(new JLabel(Resources.getString("str.name") + ":"));
		final JTextField nameField = new JTextField();
		nameField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tile.setName(nameField.getText());
			}
		});
		controlsPanel.add(nameField);
		// z
		controlsPanel.add(new JLabel("z:"));
		final JTextField zField = new JTextField();
		zField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tile.setZ(Integer.parseInt(zField.getText()));
			}
		});
		controlsPanel.add(zField);
		// weight
		controlsPanel.add(new JLabel(Resources.getString("str.weight") + ":"));
		final JTextField weightField = new JTextField();
		weightField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tile.setWeight(Integer.parseInt(weightField.getText()));
			}
		});
		controlsPanel.add(weightField);
		controlsPanel.add(new JLabel(Resources.getString("str.image") + ":"));
		final JButton btnImage = new JButton(Resources.getString("str.load"));
		btnImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Log.info("load");
			}
		});
		controlsPanel.add(btnImage);
		panel.add(controlsPanel);
		
		// image panel
		final JPanel imgPanel = new JPanel();
		imgPanel.setBackground(Color.BLACK);
		imgPanel.setPreferredSize(new Dimension(width / 2, height));
		panel.add(imgPanel);
		
		getContentPane().add(panel);
		
		setVisible(true);
		refreshTile();
	}
	
	private void refreshTile() {
		tile = new Tile();
	}
}
