/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.editor.core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import ru.swg.island.common.core.object.Level;
import ru.swg.wheelframework.event.listener.ObjectListener;
import ru.swg.wheelframework.io.Resources;

/**
 * Level Info Panel
 */
public class LevelInfoPanel extends JFrame {
	private static final long serialVersionUID = -6323803925892391611L;

	private final Level level;
	private boolean edit = false; 
	private final ObjectListener<Level> listener;
	
	public LevelInfoPanel(final ObjectListener<Level> listener) {
		level = new Level();
		this.listener = listener;
		initFrame();
	}
	
	public LevelInfoPanel(final Level level, final ObjectListener<Level> listener) {
		this.level = level;
		this.listener = listener;
		edit = true;
		initFrame();
	}
	
	private final void initFrame() {
		setTitle(Resources.getString("str.edit_level_info"));
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setFocusable(true);		
		getContentPane().add(getPanel());
		pack();
		setVisible(true);		
	}
	
	private final JPanel getPanel() {
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel(Resources.getString("str.id")));
		final JTextField idField = new JTextField();
		idField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(final DocumentEvent e) {
				try {
					level.setId(e.getDocument().getText(0, e.getDocument().getLength()));
				} catch (final BadLocationException err) { }
			}
			
			@Override
			public void removeUpdate(final DocumentEvent e) { }

			@Override
			public void changedUpdate(final DocumentEvent e) {}
		});
		panel.add(idField);
		panel.add(new JLabel(Resources.getString("str.name")));
		final JTextField nameField = new JTextField();
		nameField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(final DocumentEvent e) {
				try {
					level.setName(e.getDocument().getText(0, e.getDocument().getLength()));
				} catch (final BadLocationException err) { }
			}
			
			@Override
			public void removeUpdate(final DocumentEvent e) { }

			@Override
			public void changedUpdate(final DocumentEvent e) {}
		});
		panel.add(nameField);
		panel.add(new JLabel(Resources.getString("str.description")));
		final JTextField descriptionField = new JTextField();
		descriptionField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(final DocumentEvent e) {
				try {
					level.setDescription(e.getDocument().getText(0, e.getDocument().getLength()));
				} catch (final BadLocationException err) { }
			}
			
			@Override
			public void removeUpdate(final DocumentEvent e) { }

			@Override
			public void changedUpdate(final DocumentEvent e) {}
		});
		panel.add(descriptionField);
		
		if (!edit) {
			panel.add(new JLabel(Resources.getString("str.width")));
			final JTextField widthField = new JTextField();
			widthField.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void insertUpdate(final DocumentEvent e) {
					try {
						level.setWidth(Integer.parseInt(e.getDocument().getText(0, e.getDocument().getLength())));
					} catch (final BadLocationException err) { }
				}
				
				@Override
				public void removeUpdate(final DocumentEvent e) { }

				@Override
				public void changedUpdate(final DocumentEvent e) {}
			});
			panel.add(widthField);

			panel.add(new JLabel(Resources.getString("str.height")));
			final JTextField heightField = new JTextField();
			heightField.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void insertUpdate(final DocumentEvent e) {
					try {
						level.setHeight(Integer.parseInt(e.getDocument().getText(0, e.getDocument().getLength())));
					} catch (final BadLocationException err) { }
				}
				
				@Override
				public void removeUpdate(final DocumentEvent e) { }

				@Override
				public void changedUpdate(final DocumentEvent e) {}
			});
			panel.add(heightField);
		}
		
		// buttons panel
		final JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		final JButton btnOk = new JButton(Resources.getString("str.ok"));
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				level.build();
				listener.on(level);
				setVisible(false);	
			}
		});
		buttonsPanel.add(btnOk);
		final JButton btnCancel = new JButton(Resources.getString("str.cancel"));
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				setVisible(false);
			}
		});
		buttonsPanel.add(btnCancel);
		panel.add(buttonsPanel);
		
		return panel;
	}
}
