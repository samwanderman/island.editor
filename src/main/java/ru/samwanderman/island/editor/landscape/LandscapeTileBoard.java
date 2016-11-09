/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.samwanderman.island.editor.landscape;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import ru.samwanderman.island.common.core.Const;
import ru.samwanderman.island.common.core.object.Tile;
import ru.samwanderman.rtwf.io.Resources;
import ru.samwanderman.rtwf.view.Image;

/**
 * Tile board
 */
public final class LandscapeTileBoard extends JFrame {
	private static final long serialVersionUID = 7133632713919859075L;
	
	// default width
	private final int width = 400;
	// default height
	private final int height = 300;
	
	// current tile config
	private Tile tile = new Tile();
	
	/**
	 * Constructor
	 * 
	 */
	public LandscapeTileBoard() {
		setSize(new Dimension(width, height));
		setTitle(Resources.getString("title.game.editor.tile"));
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setFocusable(true);
		
		// Set main panel
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		// image panel
		final JImage imgPanel = new JImage();
		imgPanel.setBackground(Color.BLACK);
		imgPanel.setPreferredSize(new Dimension(width / 2, height));
		
		// Set controls panel
		final JPanel controlsPanel = new JPanel();
		controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
		controlsPanel.setPreferredSize(new Dimension(width / 2, height));
		// id
		controlsPanel.add(new JLabel(Resources.getString("str.id") + ":"));
		final JTextField idField = new JTextField();
		idField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(final DocumentEvent e) {
				try {
					tile.setId(e.getDocument().getText(0, e.getDocument().getLength()));
				} catch (final BadLocationException err) { }
			}
			
			@Override
			public void removeUpdate(final DocumentEvent e) { }

			@Override
			public void changedUpdate(final DocumentEvent e) {}
		});
		controlsPanel.add(idField);
		// name
		controlsPanel.add(new JLabel(Resources.getString("str.name") + ":"));
		final JTextField nameField = new JTextField();
		nameField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(final DocumentEvent e) {
				try {
					tile.setName(e.getDocument().getText(0, e.getDocument().getLength()));
				} catch (final BadLocationException err) { }
			}
			
			@Override
			public void removeUpdate(final DocumentEvent e) { }

			@Override
			public void changedUpdate(final DocumentEvent e) {}
		});
		controlsPanel.add(nameField);
		// z
		controlsPanel.add(new JLabel("z:"));
		final JTextField zField = new JTextField("0");
		zField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(final DocumentEvent e) {
				try {
					tile.setZ(Integer.parseInt(e.getDocument().getText(0, e.getDocument().getLength())));
				} catch (final BadLocationException err) { }
			}
			
			@Override
			public void removeUpdate(final DocumentEvent e) { }

			@Override
			public void changedUpdate(final DocumentEvent e) {}
		});
		controlsPanel.add(zField);
		// weight
		controlsPanel.add(new JLabel(Resources.getString("str.weight") + ":"));
		final JTextField weightField = new JTextField("0");
		weightField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(final DocumentEvent e) {
				try {
					tile.setWeight(Integer.parseInt(e.getDocument().getText(0, e.getDocument().getLength())));
				} catch (final BadLocationException err) { }
			}
			
			@Override
			public void removeUpdate(final DocumentEvent e) { }

			@Override
			public void changedUpdate(final DocumentEvent e) {}
		});
		controlsPanel.add(weightField);
		// image selector button
		controlsPanel.add(new JLabel(Resources.getString("str.image") + ":"));
		final JButton btnImage = new JButton(Resources.getString("str.load"));
		btnImage.addActionListener(new ActionListener() {
			@Override
			public final void actionPerformed(final ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File("./"));
				if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					try {
						final Image image = new Image(ImageIO.read(fc.getSelectedFile()));
						imgPanel.setImage(image);
					} catch (final IOException err) { }
				}
			}
		});
		controlsPanel.add(btnImage);
		
		panel.add(controlsPanel);
		panel.add(imgPanel);
		
		getContentPane().add(panel);
		
		final JMenuBar menuBar = new JMenuBar();
		final JMenu menu = new JMenu(Resources.getString("str.file"));
		final JMenuItem menuItemNew = new JMenuItem(Resources.getString("str.new")), 
					menuItemOpen = new JMenuItem(Resources.getString("str.open")), 
					menuItemSave = new JMenuItem(Resources.getString("str.save"));
		// new tile
		menuItemNew.addActionListener(new ActionListener() {
			@Override
			public final void actionPerformed(final ActionEvent e) {
				tile = new Tile();
				idField.setText("");
				nameField.setText("");
				weightField.setText("0");
				zField.setText("0");
				imgPanel.setImage(null);
			}
		});
		menu.add(menuItemNew);
		
		// open existing tile
		menuItemOpen.addActionListener(new ActionListener() {
			@Override
			public final void actionPerformed(final ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File("./"));
				if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					try {
						final Tile tile = Resources.loadObject(fc.getSelectedFile().getAbsolutePath(), Tile.class, true);
						idField.setText(tile.getId());
						nameField.setText(tile.getName());
						zField.setText(Integer.toString(tile.getZ()));
						weightField.setText(Integer.toString(tile.getWeight()));
						final Image image = Resources.loadImage(tile.getImage());
						imgPanel.setImage(image);
					} catch (final IOException err) { }
				}
			}
		});
		menu.add(menuItemOpen);
		
		// save tile
		final JFrame self = this;
		menuItemSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				try {
					final String imagePath = "./tiles/landscape/" + idField.getText() + ".png"; 
					Resources.saveImage(imagePath, imgPanel.getImage());
					tile.setImage(imagePath);
					Resources.saveObject("./tiles/landscape/" + idField.getText() + ".json", tile);
					JOptionPane.showMessageDialog(self, Resources.getString("str.tile_saved"));
					tile = new Tile();
					idField.setText("");
					nameField.setText("");
					weightField.setText("0");
					zField.setText("0");
					imgPanel.setImage(null);
				} catch (final IOException err) { }
			}
		});
		menu.add(menuItemSave);
		menuBar.add(menu);
		setJMenuBar(menuBar);
		
		setVisible(true);
	}
	
	/**
	 * Simple class for image displaying
	 * 
	 */
	private static class JImage extends JPanel {
		private static final long serialVersionUID = -7184020191194754008L;
		// image
		private Image image;
		
		/**
		 * Set image
		 * 
		 * @param image
		 */
		public final void setImage(final Image image) {
			this.image = image;
			repaint();
		}
		
		/**
		 * Get image
		 * 
		 * @return
		 */
		public final Image getImage() {
			return image;
		}
		
		@Override
		public final void paint(final Graphics g) {
			super.paint(g);
			final Graphics2D g2d = (Graphics2D) g;
			
			if (image != null) {
				g2d.drawImage(image.getContent(), (getWidth() - image.getWidth()) / 2, (getHeight() - image.getHeight()) / 2, null);
			} else {
				g2d.setColor(Color.WHITE);
				g2d.fillRect((getWidth() - Const.TILE_WIDTH) / 2, (getHeight() - Const.TILE_HEIGHT) / 2, Const.TILE_WIDTH, Const.TILE_HEIGHT);
			}
		}
	}
}
