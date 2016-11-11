package io.timedrop.ui.components.selector;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.text.DefaultEditorKit;

public class LightComboBox<E> extends JComboBox<E>
{
	private static final long serialVersionUID = 4346608501332022122L;

	public LightComboBox()
	{
		super();

		JTextField textField = (JTextField) getEditor().getEditorComponent();
		textField.setCaretColor(Color.decode("#4A4A4A"));
		textField.setForeground(Color.decode("#4A4A4A"));
		textField.setBackground(Color.decode("#F3F3F3"));
		setEditable(true);

		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("mac") >= 0)
		{
			InputMap im = (InputMap) UIManager.get("TextField.focusInputMap");
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.META_DOWN_MASK), DefaultEditorKit.copyAction);
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.META_DOWN_MASK), DefaultEditorKit.pasteAction);
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.META_DOWN_MASK), DefaultEditorKit.cutAction);
		}
	}

	@Override
	public void updateUI()
	{
		super.updateUI();

		setFont(UIManager.getFont("TextField.font").deriveFont(20f));
		setForeground(Color.decode("#4A4A4A"));
		setBackground(Color.decode("#F3F3F3"));

		Border lines = BorderFactory.createMatteBorder(1, 0, 1, 0, Color.decode("#DCDCDC"));
		Border empty = new EmptyBorder(0, 10, 0, 10);
		setBorder(new CompoundBorder(lines, empty));

		((BasicComboBoxRenderer) getRenderer()).setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		UIManager.put("ComboBox.squareButton", Boolean.FALSE);
		setUI(new BasicComboBoxUI()
		{
			@Override
			protected JButton createArrowButton()
			{
				JButton b = new JButton();
				b.setBorder(BorderFactory.createEmptyBorder());
				b.setVisible(false);
				return b;
			}
		});

		Object o = (JComponent) getAccessibleContext().getAccessibleChild(0);
		if (o instanceof JComponent)
		{
			JComponent component = (JComponent) o;
			component.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		}

		UIManager.put("ComboBox.selectionForeground", Color.decode("#4A4A4A"));
		UIManager.put("ComboBox.selectionBackground", Color.decode("#086ada"));

		this.setRenderer(new DefaultListCellRenderer()
		{
			private static final long serialVersionUID = -5495299095091044033L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
			{
				Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				c.setFont(UIManager.getFont("TextField.font").deriveFont(16f));
				JPanel p = new JPanel();
				p.setLayout(new BorderLayout());
				p.setBorder(new EmptyBorder(0, 10, 0, 10));
				if (isSelected)
				{
					c.setForeground(Color.decode("#ffffff"));
					c.setBackground(Color.decode("#086ada"));
					p.setBackground(Color.decode("#086ada"));
				}
				else
				{
					c.setForeground(Color.decode("#4A4A4A"));
					c.setBackground(Color.decode("#F3F3F3"));
					p.setBackground(Color.decode("#F3F3F3"));
				}
				p.add(c, BorderLayout.WEST);
				return p;
			}
		});
	}
}